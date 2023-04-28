package org.tensorflow.lite.examples.objectdetection

import MultipartRequest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.common.util.concurrent.ListenableFuture
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Handler
import androidx.camera.core.*
import androidx.camera.core.ImageProxy.PlaneProxy
import java.nio.ByteBuffer
import org.json.JSONObject
import kotlin.math.max
import kotlin.math.roundToInt

class cameraScene : AppCompatActivity() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    var preview: PreviewView? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_scene)

        preview = findViewById<PreviewView>(R.id.preview)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        startCamera()
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        Handler().postDelayed({
            takePhoto()
        }, 1000)
    }

    private fun startCamera() {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder().build()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(preview?.surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d("Camera", "Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        imageCapture?.let {
            it.takePicture(
                imgCaptureExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val bitmap = imageProxyToBitmap(image)
                        image.close()

                        if (bitmap != null) {
                            val processedBitmap = processImage(bitmap)
                            sendImage(processedBitmap)
                        } else {
                            Log.e("Camera", "Error converting ImageProxy to Bitmap")
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            this@cameraScene,
                            "Error taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("Camera", "Error taking photo:$exception")
                    }
                })
        }
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
        val planeProxy: PlaneProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun processImage(bitmap: Bitmap): Bitmap {
        val scaleFactor = 512f / max(bitmap.width, bitmap.height)
        val newWidth = (bitmap.width * scaleFactor).roundToInt()
        val newHeight = (bitmap.height * scaleFactor).roundToInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun sendImage(bitmap: Bitmap) {
        val url = "https://0645-54-80-185-234.ngrok-free.app/uploadImage"

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
        val resizedImageBytes = byteArrayOutputStream.toByteArray()

        val filePartName = "image"
        val mimeType = "image/jpeg"

        val multipartRequest = MultipartRequest(
            url,
            Response.Listener { response ->
                Log.d("sendImage", "Server response: $response")
                val jsonResponse = JSONObject(response)
                val result = jsonResponse.getString("message")
                if (result.isNullOrEmpty() || result.contains("error", ignoreCase = true)) {
                    Log.e("sendImage", "Error in response: $response")
                    ObjectViewmodel.objectResult = "An error occurred"
                } else {
                    ObjectViewmodel.objectResult = result
                }
                finish()
            },
            Response.ErrorListener { error ->
                Log.e("sendImage", "Error: $error")
                ObjectViewmodel.objectResult = "An error occurred"
                finish()
            },
            null,
            null,
            filePartName,
            resizedImageBytes,
            mimeType
        )
        multipartRequest.retryPolicy = DefaultRetryPolicy(
            100000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(multipartRequest)
    }

//Experimental Feature:
//    private fun extractTextFromImage(bitmap: Bitmap, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
//        // Create an InputImage from the Bitmap
//        val inputImage = InputImage.fromBitmap(bitmap, 0)
//
//        // Create an instance of TextRecognizer
//        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//
//        // Process the image
//        recognizer.process(inputImage)
//            .addOnSuccessListener { visionText ->
//                // Extract the text from the result
//                val extractedText = visionText.text
//                onSuccess(extractedText)
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//            }
//    }
}