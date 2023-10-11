package fr.swarmlab.tssparty.presentation.wallet.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.swarmlab.tssparty.presentation.shared.NumberPicker
import fr.swarmlab.tssparty.presentation.wallet.viewmodel.TssKeygenPartyStep
import fr.swarmlab.tssparty.presentation.wallet.viewmodel.TssKeygenViewModel



@Composable
fun mpcWalletReady(step3: TssKeygenPartyStep.TssKeygenStep3,
                   onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "All peers are ready",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        step3.peers.forEach { peer ->
            Text(
                text = peer.id +":" + peer.key,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onStart()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Start!")
        }
    }
}
