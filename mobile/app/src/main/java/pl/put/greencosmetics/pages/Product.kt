package pl.put.greencosmetics.pages

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import pl.put.greencosmetics.shared.IP

@Composable
fun Product(navController: NavController) {
    val context = LocalContext.current
    val queue: RequestQueue = Volley.newRequestQueue(context)
    val id = navController.currentBackStackEntry?.arguments?.getString("id") ?: ""
    val url = "http://${IP}/product/${id}"

    val name = remember { mutableStateOf("") }
    val ingredients = remember { mutableStateOf(listOf<String>()) }
    val isNatural = remember { mutableStateOf(listOf<Boolean>()) }

    val jsonObjectRequest = JsonObjectRequest(0, url, null,
        { response ->
            name.value = response.getString("name")
            ingredients.value = List(
                response.getJSONArray("ingredients").length()
            ) { i -> response.getJSONArray("ingredients").getJSONObject(i).getString("name") }
            isNatural.value = List(
                response.getJSONArray("ingredients").length()
            ) { i -> response.getJSONArray("ingredients").getJSONObject(i).getBoolean("isNatural") }
        }, { error ->
            Log.e("PRODUCT", error.message.toString())
        })
    queue.add(jsonObjectRequest)
    if (ingredients.value.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = name.value, style = MaterialTheme.typography.headlineMedium)
            for (i in 0..<ingredients.value.size) {
                Row(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).fillMaxWidth().padding(8.dp),
                ){
                Text(
                    text = ingredients.value[i],
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .background(if (isNatural.value[i]) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error)
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
            }
            }

        }
    }
}