package pl.put.greencosmetics.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import pl.put.greencosmetics.shared.IP
import pl.put.greencosmetics.shared.ProductRequest


@Composable
fun NewItem(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    val id = navController.currentBackStackEntry?.arguments?.getString("id") ?: ""
    val context = LocalContext.current
    val queue: RequestQueue = Volley.newRequestQueue(context)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = id,
            readOnly = true,
            onValueChange = { },
            label = { Text( "id") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ingredients,
            onValueChange = { ingredients = it },
            label = { Text("Ingredients") },
            modifier = Modifier
                .defaultMinSize(minHeight = 40.dp)
                .fillMaxWidth(),
            maxLines = 20,
            minLines = 10,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Right
        ) {
            IconButton(onClick = { navController.navigate("ocr") }) {
                Icon(Icons.Filled.Camera, contentDescription = "OCR")
            }
            Button(onClick = {
                val ingredientsList =
                    ingredients.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val product = ProductRequest(id, ingredientsList, name)
                val url = "http://${IP}/product"

                val jsonBody = JSONObject()
                jsonBody.put("id", product.id)
                jsonBody.put("name", product.name)
                val jsonArray = JSONArray()
                ingredientsList.forEach {
                    jsonArray.put(it)
                }
                jsonBody.putOpt("ingredients", jsonArray)
                val jsonObjectRequest = JsonObjectRequest(1, url, jsonBody,
                    { response ->
                        navController.navigate("item/${id}")
                    }, { error ->
                        Log.e("NEW ITEM", error.message.toString())
                    })
                queue.add(jsonObjectRequest)
            }) {
                Text("Create")
            }
        }
    }
}