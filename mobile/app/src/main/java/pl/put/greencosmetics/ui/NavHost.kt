package pl.put.greencosmetics.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.put.greencosmetics.pages.CameraScreen
import pl.put.greencosmetics.pages.Main
import pl.put.greencosmetics.pages.NewItem
import pl.put.greencosmetics.pages.Product


@Composable
fun NavHostMaster(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            Main(navController)
        }
        composable("new/{id}") {
            NewItem(navController)
        }
        composable("item/{id}"){
            Product(navController)
        }
        composable("ocr") {
           CameraScreen(navController = navController, barCode = false )
        }
    }
}