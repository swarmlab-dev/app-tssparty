package fr.swarmlab.tssparty.presentation.wallet.create

import androidx.compose.runtime.Composable
import fr.swarmlab.tssparty.presentation.wallet.viewmodel.TssKeygenPartyStep
import fr.swarmlab.tssparty.presentation.wallet.viewmodel.TssKeygenViewModel



@Composable
fun mpcWalletDone(step5: TssKeygenPartyStep.TssKeygenStep5) {
    CenteredText(text = step5.tssKeyShare.keyShareJson)
}