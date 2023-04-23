package org.tensorflow.lite.examples.objectdetection
import MultipartRequest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.Task
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.prianshuprasad.assistant.messageData
import java.io.Console
import java.io.File
import java.lang.invoke.ConstantCallSite
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

//Experimental Feature to use MLKIT for OCR on  Android
/*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
 */
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import kotlin.math.max
import kotlin.math.round
import kotlin.math.roundToInt


class cameraScene : AppCompatActivity() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    var preview: PreviewView? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_scene)

        preview = findViewById<PreviewView>(R.id.preview)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        startCamera()
        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        storage = FirebaseStorage.getInstance();
        storageReference = storage?.reference;

        Handler().postDelayed({
            takePhoto()
        }, 1000)
    }


    private fun startCamera() {
        // listening for data from the camera
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder().build()
            // connecting a preview use case to the preview in the xml file.
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(preview?.surfaceProvider)
            }
            try {
                // clear all the previous use cases first.
                cameraProvider.unbindAll()
                // binding the lifecycle of the camera to the lifecycle of the application.
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d("Camera", "Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    var filePath: Uri? = null
    private fun takePhoto() {
        imageCapture?.let {
            //Create a storage location whose fileName is timestamped in milliseconds.
            val fileName = "JPEG_${System.currentTimeMillis()}"
//            val fileName = "PNG_${System.currentTimeMillis()}.png"
            val file = File(externalMediaDirs[0], fileName)

            // Save the image in the above file
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            /* pass in the details of where and how the image is taken.(arguments 1 and 2 of takePicture)
            pass in the details of what to do after an image is taken.(argument 3 of takePicture) */

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        filePath = file.toUri()
                        Log.i("camera", "The image has been saved in ${file.toUri()}")
                        runOnUiThread { sendImage(file) }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            this@cameraScene,
                            "Error taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("camera", "Error taking photo:$exception")
                    }

                })
        }
    }

fun processImage(file: File): Bitmap {
    // Decode the image file into a Bitmap
    val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)

    // Calculate the scale factor to resize the image
    val scaleFactor = 512f / max(originalBitmap.width, originalBitmap.height)

    // Calculate the new width and height, rounding to the nearest multiple of 64
    val newWidth = (originalBitmap.width * scaleFactor / 64.0).roundToInt() * 64
    val newHeight = (originalBitmap.height * scaleFactor / 64.0).roundToInt() * 64

    // Create a matrix for the manipulation
    val matrix = Matrix()

    // Resize the bitmap
    matrix.postScale(newWidth.toFloat() / originalBitmap.width, newHeight.toFloat() / originalBitmap.height)

    // Create a new bitmap with the specified size and RGB_565 format
    val resizedBitmap = Bitmap.createBitmap(
        originalBitmap,
        0,
        0,
        originalBitmap.width,
        originalBitmap.height,
        matrix,
        true
    ).copy(Bitmap.Config.RGB_565, false)

    return resizedBitmap
}

    /*
        Experimental Features: Performing OCR on Android
     */
    /*
    private fun extractTextFromImage(bitmap: Bitmap, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        // Create an InputImage from the Bitmap
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        // Create an instance of TextRecognizer
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        // Process the image
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                // Extract the text from the result
                val extractedText = visionText.text
                onSuccess(extractedText)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

*/
private fun sendImage(file: File) {

    val url = "https://6679-44-208-85-154.ngrok-free.app/uploadImage"

    // Decode the original image file into a Bitmap
    val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)

    // Extract text from the original image and display it in the debug console
    /*
    extractTextFromImage(
        originalBitmap,
        onSuccess = { extractedText ->
            Log.i("sendImage", "Extracted text: $extractedText")
        },
        onFailure = { exception ->
            Log.e("sendImage", "Error extracting text: $exception")
        }
    )
    */

    // Process the image
    val resizedBitmap = processImage(file)

    // Convert the resized bitmap to a byte array
    val byteArrayOutputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val resizedImageBytes = byteArrayOutputStream.toByteArray()

    val filePartName = "image"
//    val mimeType = "image/png"
    val mimeType = "image/jpeg"

    val multipartRequest = MultipartRequest(
        url,
        Response.Listener { response ->
            Log.d("sendImage", "Server response: $response")
            println("Server response: $response")
            // Parse the JSON response
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
        60000, // Increase the timeout duration to 60 seconds
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )
    val requestQueue = Volley.newRequestQueue(this)
    requestQueue.add(multipartRequest)
}
}