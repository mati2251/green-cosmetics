package pl.put.greencosmetics.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.put.greencosmetics.pages.Main


@Composable
fun NavHostMaster(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            Main()
        }
    }
}