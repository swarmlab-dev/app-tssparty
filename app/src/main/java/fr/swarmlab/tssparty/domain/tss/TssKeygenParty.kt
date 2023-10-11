package fr.swarmlab.tssparty.domain.tss

sealed class TssCurve {
    object ECDSA : TssCurve()
    object EDDSA : TssCurve()
}

data class TssKeygenPartyParameters(
    val partyName: String,
    val partyCount: Int = 3,
    val threshold: Int = 1,
    val curve: TssCurve = TssCurve.EDDSA
)

data class KeyId(
    val id: String,
    val key: String
)

data class TssKeyShare(
    val party: TssKeygenPartyParameters,
    val peers: List<KeyId>,
    val keyShareJson: String,
)

