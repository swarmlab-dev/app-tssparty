package fr.swarmlab.tssparty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.swarmlab.tssparty.presentation.wallet.WalletHome
import fr.swarmlab.tssparty.presentation.wallet.create.MpcWalletCreate


@Composable
fun TssPartyNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.HOME.path) {
        val dst = Destinations(navController)

        composable(Route.HOME.path) {
            WalletHome(dst)
        }
        composable(Route.NEWMPCWALLET.path) {
            MpcWalletCreate(dst)
        }
    }
}

class Destinations(private val navController: NavHostController) {
    private fun goTo(path: String, opts: NavOptionsBuilder.() -> Unit) {
        navController.navigate(path, opts)
    }

    fun Home(opts: NavOptionsBuilder.() -> Unit = {}) {
        goTo(Route.HOME.path, opts)
    }

    fun Create(opts: NavOptionsBuilder.() -> Unit = {}) {
        goTo(Route.NEWMPCWALLET.path, opts)
    }

    fun Settings(opts: NavOptionsBuilder.() -> Unit = {}) {
        goTo(Route.SETTINGS.path, opts)
    }
}
