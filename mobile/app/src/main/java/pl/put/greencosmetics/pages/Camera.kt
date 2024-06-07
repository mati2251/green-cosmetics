package pl.put.greencosmetics.pages

import android.annotation.SuppressLint
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import androidx.core.content.ContextCompat
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import pl.put.greencosmetics.shared.IP

@Composable
fun CameraScreen(navController: NavController, barCode: Boolean) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }
    var cameraProvider: ProcessCameraProvider? = null
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                preview.setSurfaceProvider(previewView.surfaceProvider)
                val imageAnalysis = ImageAnalysis.Builder().build()
                if (barCode) {
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        BarcodeAnalyzer(context, navController)
                    )
                } else {
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        OCRAnalyzer()
                    )
                }
                runCatching {
                    cameraProvider = cameraProviderFuture.get()
                    cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                }.onFailure {
                    Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
                }
                previewView
            }
        )
        if (!barCode) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Button(
                    onClick = {
                        cameraProvider?.unbindAll()
                    }) {
                    Text("Scan")
                }
            }
        }
    }
}

class BarcodeAnalyzer(private val context: Context, private val navController: NavController) :
    ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    private val scanner = BarcodeScanning.getClient(options)
    var redirect = false;

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image
            ?.let { image ->
                scanner.process(
                    InputImage.fromMediaImage(
                        image, imageProxy.imageInfo.rotationDegrees
                    )
                ).addOnSuccessListener { barcode ->
                    barcode?.takeIf { it.isNotEmpty() }
                        ?.mapNotNull { it.rawValue }
                        ?.joinToString(",")
                        ?.let { barcodeValue ->
                            val volleQueue = Volley.newRequestQueue(context)
                            val url = "http://${IP}/product/${barcodeValue}/exists"
                            val stringRequest = StringRequest(
                                Request.Method.GET, url,
                                { response ->
                                    if (!redirect) {
                                        navController.navigate("item/${barcodeValue}")
                                        redirect = true
                                    }
                                },
                                { error ->
                                    if (error.networkResponse.statusCode == 404) {
                                        if (!redirect) {
                                            navController.navigate("new/${barcodeValue}" )
                                            redirect = true
                                        }
                                    }
                                }
                            )
                            volleQueue.add(stringRequest)
                        }
                }.addOnCompleteListener {
                    imageProxy.close()
                }
            }
    }
}

class OCRAnalyzer() : ImageAnalysis.Analyzer {

    private var options = TextRecognizerOptions.DEFAULT_OPTIONS
    private val scanner = TextRecognition.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image
            ?.let { image ->
                scanner.process(
                    InputImage.fromMediaImage(
                        image, imageProxy.imageInfo.rotationDegrees
                    )
                ).addOnSuccessListener { results ->
                    results.textBlocks
                        .takeIf { it.isNotEmpty() }
                }.addOnCompleteListener {
                    imageProxy.close()
                }
            }
    }
}

