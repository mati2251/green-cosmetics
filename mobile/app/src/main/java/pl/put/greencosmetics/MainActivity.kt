package pl.put.greencosmetics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import pl.put.greencosmetics.theme.AppTheme
import pl.put.greencosmetics.ui.TopBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                TopBar()
            }
        }
    }
}
