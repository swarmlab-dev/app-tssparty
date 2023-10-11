package fr.swarmlab.tssparty.presentation.wallet

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import fr.swarmlab.tssparty.presentation.navigation.Destinations

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletHome(nav : Destinations = Destinations(rememberNavController())) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List with FAB") }
            )
        },
        content = {
            Text(
                text = "WalletHome"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    nav.Create()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New")
            }
        }
    )
}
