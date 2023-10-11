package fr.swarmlab.tssparty.presentation.wallet.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.swarmlab.tssparty.domain.tss.KeyId
import fr.swarmlab.tssparty.domain.tss.TssCurve
import fr.swarmlab.tssparty.domain.tss.TssKeygenPartyParameters
import fr.swarmlab.tssparty.presentation.shared.NumberPicker
import kotlin.random.Random

@Composable
fun mpcWalletParameters(onSubmit: (KeyId, TssKeygenPartyParameters) -> Unit) {
    var partyName by remember { mutableStateOf("My MPC Wallet") }
    var partyCount by remember { mutableIntStateOf(3) }
    var threshold by remember { mutableIntStateOf(2) }
    var localKeyName by remember { mutableStateOf("bob") }
    var useEddsa by remember { mutableStateOf(true) }

    mpcWalletParametersContent(
        partyName = partyName,
        partyCount = partyCount,
        threshold = threshold,
        localKeyName = localKeyName,
        useEddsa = useEddsa,
        onPartyNameChanged = {
            partyName = it
        },
        onPartyCountChanged = {
            if (it >= 1) {
                partyCount = it
            }
            if (threshold > partyCount - 1) {
                threshold = partyCount - 1
            }
        },
        onThresholdChanged = {
            if (it <= partyCount - 1) {
                threshold = it
            }
        },
        onLocalKeyIdChanged = {
            localKeyName = it
        },
        onUseEddsaChanged = {
            useEddsa = it
        }
    ) {
        onSubmit(
            KeyId(
                id = localKeyName,
                key = Random.nextBytes(32).joinToString("") { String.format("%02X", it) }
            ),
            TssKeygenPartyParameters(
                partyName,
                partyCount,
                threshold,
                if (useEddsa) TssCurve.EDDSA else TssCurve.ECDSA
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mpcWalletParametersContent(
    partyName: String,
    partyCount: Int,
    threshold: Int,
    localKeyName: String,
    useEddsa: Boolean,
    onPartyNameChanged: (String) -> Unit,
    onPartyCountChanged: (Int) -> Unit,
    onThresholdChanged: (Int) -> Unit,
    onLocalKeyIdChanged: (String) -> Unit,
    onUseEddsaChanged: (Boolean) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = partyName,
            onValueChange = {
                onPartyNameChanged(it)
            },
            label = { Text("MPC TSS Wallet") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Number of parties for the wallet")
        NumberPicker(value = partyCount, onValueChange = {
            onPartyCountChanged(it)
        })

        Spacer(modifier = Modifier.height(16.dp))

        Text("Number of parties required to sign")
        NumberPicker(value = threshold, onValueChange = {
            onThresholdChanged(it)
        })

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = localKeyName,
            onValueChange = {
                onLocalKeyIdChanged(it)
            },
            label = { Text("bob") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("ECDSA / EDDSA")
        Switch(
            checked = useEddsa,
            onCheckedChange = {
                onUseEddsaChanged(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSubmit()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Submit")
        }
    }
}
