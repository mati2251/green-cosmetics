package pl.put.greencosmetics.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Main(navController: NavController) {
    val state = rememberPermissionState(android.Manifest.permission.CAMERA)
    if (state.status.isGranted) {
        CameraScreen(navController = navController, true)
    } else if (state.status.shouldShowRationale) {
        Text("Camera Permission permanently denied")
    } else {
        SideEffect {
            state.run { launchPermissionRequest() }
        }
        Text("No Camera Permission")
    }
}
