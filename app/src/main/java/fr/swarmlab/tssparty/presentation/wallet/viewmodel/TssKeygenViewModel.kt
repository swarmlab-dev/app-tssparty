package fr.swarmlab.tssparty.presentation.wallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.swarmlab.tssparty.core.datastore.AppPreferences
import fr.swarmlab.tssparty.domain.tss.KeyId
import fr.swarmlab.tssparty.domain.tss.TssCurve
import fr.swarmlab.tssparty.domain.tss.TssKeyShare
import fr.swarmlab.tssparty.domain.tss.TssKeygenPartyParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tssparty.KeygenTssParty
import javax.inject.Inject
import kotlin.reflect.KClass

sealed class TssKeygenPartyStep() {
    object TssKeygenStep1 : TssKeygenPartyStep()

    class TssKeygenStep2(
        val keyId: KeyId,
        val tssKeygenParameters: TssKeygenPartyParameters,
        val tssParty: KeygenTssParty,
        val waitingJob: Job
    ) : TssKeygenPartyStep()

    class TssKeygenStep3(
        val keyId: KeyId,
        val tssKeygenParameters: TssKeygenPartyParameters,
        val tssParty: KeygenTssParty,
        val peers: List<KeyId>
    ) : TssKeygenPartyStep()

    class TssKeygenStep4(
        val keyId: KeyId,
        val tssKeygenParameters: TssKeygenPartyParameters,
        val tssParty: KeygenTssParty,
        val peers: List<KeyId>,
        val computingJob: Job,
    ) : TssKeygenPartyStep()

    class TssKeygenStep5(
        val tssKeyShare: TssKeyShare
    ) : TssKeygenPartyStep()
}

@HiltViewModel
class TssKeygenViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {
    private val lock = Any()
    private val _currentStep =
        MutableLiveData<TssKeygenPartyStep>(TssKeygenPartyStep.TssKeygenStep1)
    val currentStep: LiveData<TssKeygenPartyStep> = _currentStep

    private fun <T : TssKeygenPartyStep> stepFunction(
        expectedStep: KClass<T>,
        function: (T) -> TssKeygenPartyStep
    ) {
        synchronized(lock) {
            val step = currentStep.value
            if (step != null && step::class == expectedStep) {
                _currentStep.value = function(step as T)
            }
        }
    }

    fun moveToStep2(keyId: KeyId, tssKeygenParameters: TssKeygenPartyParameters) {
        stepFunction(TssKeygenPartyStep.TssKeygenStep1::class) {
            createTssKeygenPartyAndWaitPeers(keyId, tssKeygenParameters)
        }
    }

    fun moveToStep3(peers: List<KeyId>) {
        stepFunction(TssKeygenPartyStep.TssKeygenStep2::class) {
            TssKeygenPartyStep.TssKeygenStep3(
                it.keyId,
                it.tssKeygenParameters,
                it.tssParty,
                peers
            )
        }
    }

    fun moveToStep4() {
        stepFunction(TssKeygenPartyStep.TssKeygenStep3::class) {
            computeKeyShare(it)
        }
    }

    fun moveToStep5(tssKeyShare: String) {
        stepFunction(TssKeygenPartyStep.TssKeygenStep4::class) {
            TssKeygenPartyStep.TssKeygenStep5(
                TssKeyShare(
                    it.tssKeygenParameters,
                    it.peers,
                    tssKeyShare
                )
            )
        }
    }

    private fun createTssKeygenPartyAndWaitPeers(
        keyId: KeyId,
        tssKeygenParameters: TssKeygenPartyParameters
    ): TssKeygenPartyStep.TssKeygenStep2 {
        val tssParty = when (tssKeygenParameters.curve) {
            TssCurve.ECDSA -> tssparty.Tssparty.newEcdsaKeygenTssPartyWithKey(
                keyId.id,
                keyId.key,
                tssKeygenParameters.partyCount.toLong(),
                tssKeygenParameters.threshold.toLong()
            )

            TssCurve.EDDSA -> tssparty.Tssparty.newEddsaKeygenTssPartyWithKey(
                keyId.id,
                keyId.key,
                tssKeygenParameters.partyCount.toLong(),
                tssKeygenParameters.threshold.toLong()
            )
        }
        tssParty.init()

        val id = java.util.UUID.randomUUID().toString()
        val keygenJob = viewModelScope.launch(Dispatchers.IO) {
            val everyone =
                tssParty.prepareTransport(
                    appPreferences.partyBusUrl.first(),
                    id,
                    tssKeygenParameters.partyCount.toLong()
                ).split(",").map {
                    val key = it.split(":")
                    KeyId(key[0], key[1])
                }
            launch(Dispatchers.Main) {
                moveToStep3(everyone)
            }
        }

        return TssKeygenPartyStep.TssKeygenStep2(keyId, tssKeygenParameters, tssParty, keygenJob)
    }

    private fun computeKeyShare(step3: TssKeygenPartyStep.TssKeygenStep3): TssKeygenPartyStep.TssKeygenStep4 {
        val computeJob = viewModelScope.launch(Dispatchers.IO) {
            val keyShare = step3.tssParty.keyShare
            launch(Dispatchers.Main) {
                moveToStep5(keyShare)
            }
        }

        return TssKeygenPartyStep.TssKeygenStep4(
            step3.keyId,
            step3.tssKeygenParameters,
            step3.tssParty,
            step3.peers,
            computeJob
        )
    }
}