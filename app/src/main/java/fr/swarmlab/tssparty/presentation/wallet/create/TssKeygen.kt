package fr.swarmlab.tssparty.presentation.wallet.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import fr.swarmlab.tssparty.presentation.navigation.Destinations
import fr.swarmlab.tssparty.presentation.wallet.viewmodel.TssKeygenPartyStep
import fr.swarmlab.tssparty.presentation.wallet.viewmodel.TssKeygenViewModel

@Composable
fun MpcWalletCreate(
    nav: Destinations = Destinations(rememberNavController()),
    viewModel: TssKeygenViewModel = hiltViewModel()
) {
    val currentStep = viewModel.currentStep.observeAsState()

    when (val step = currentStep.value!!) {
        is TssKeygenPartyStep.TssKeygenStep1 -> mpcWalletParameters { key,params ->
            viewModel.moveToStep2(key,params)
        }
        is TssKeygenPartyStep.TssKeygenStep2 -> mpcWalletInvitation()
        is TssKeygenPartyStep.TssKeygenStep3 -> mpcWalletReady(step) {
            viewModel.moveToStep4()
        }
        is TssKeygenPartyStep.TssKeygenStep4 -> mpcWalletComputing()
        is TssKeygenPartyStep.TssKeygenStep5 -> mpcWalletDone(step)
    }
}