package org.tensorflow.lite.examples.objectdetection

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
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class cameraScene : AppCompatActivity() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    var preview:PreviewView?=null
    private lateinit var imgCaptureExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_scene)

        preview= findViewById<PreviewView>(R.id.preview)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        startCamera()
        imgCaptureExecutor = Executors.newSingleThreadExecutor()
        storage = FirebaseStorage.getInstance();
        storageReference = storage?.reference;

        Handler().postDelayed({
            takePhoto()
        },1000)




    }
    private fun startCamera(){
        // listening for data from the camera
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder().build()
            // connecting a preview use case to the preview in the xml file.
            val preview = Preview.Builder().build().also{
                it.setSurfaceProvider(preview?.surfaceProvider)
            }
            try{
                // clear all the previous use cases first.
                cameraProvider.unbindAll()
                // binding the lifecycle of the camera to the lifecycle of the application.
                cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            } catch (e: Exception) {
                Log.d("Camera", "Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(this))
    }
    var filePath: Uri? =null
    private fun takePhoto(){
        imageCapture?.let{
            //Create a storage location whose fileName is timestamped in milliseconds.
            val fileName = "JPEG_${System.currentTimeMillis()}"
            val file = File(externalMediaDirs[0],fileName)

            // Save the image in the above file
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            /* pass in the details of where and how the image is taken.(arguments 1 and 2 of takePicture)
            pass in the details of what to do after an image is taken.(argument 3 of takePicture) */

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults){
                        filePath= file.toUri()
                        Log.i("camaera","The image has been saved in ${file.toUri()}")
                        runOnUiThread {  uploadImage() }

                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            this@cameraScene,
                            "Error taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("camear", "Error taking photo:$exception")
                    }

                })
        }
    }


    private fun uploadImage() {

        if (filePath != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            // Defining the child of storageReference
            val ref = storageReference
                ?.child("images/"
                        + UUID.randomUUID().toString())

            Log.d("Camera "," image uploaded to $ref")
            // adding listeners on upload
            // or failure of image
            ref?.putFile(filePath!!)
                ?.addOnSuccessListener {
                    // Dismiss dialog

                    progressDialog.dismiss()
                    Toast
                        .makeText(this@cameraScene,
                            "Image Uploaded!!",
                            Toast.LENGTH_SHORT)
                        .show()
                }
                ?.addOnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast
                        .makeText(this@cameraScene,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT)
                        .show()
                }
                ?.addOnProgressListener { taskSnapshot ->

                    // Progress Listener for loading
                    // percentage on the dialog box
                    val progress = (100.0
                            * taskSnapshot.bytesTransferred
                            / taskSnapshot.totalByteCount)
                    progressDialog.setMessage(
                        "Uploaded "
                                + progress.toInt() + "%")
                }?.addOnCompleteListener {

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Camera "," image uploaded to $it")
                        sendLink(it.toString())

                        // Got the download URL for 'users/me/profile.png'
                    }.addOnFailureListener {
                        ObjectViewmodel.objectResult= "Image upload failed to firebase"
                        finish()
                        // Handle any errors
                    }



                }
        }









    }



    fun sendLink(link:String){

        var url ="https://b27c-35-240-182-128.ngrok.io/uploadImageLink?q=\""
        url+=link;

        url += "\""

        Log.d("URL_VISION",url)

        val mRequestQueue = Volley.newRequestQueue(this)

        // String Request initialized
        val mStringRequest = StringRequest(Request.Method.GET, url, object :
            Response.Listener<String?> {
            // display the response on screen


            override fun onResponse(response: String?) {
                if (response != null) {

                    var pResp=response;
                    ObjectViewmodel.objectResult = response
                    finish()

                };
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                ObjectViewmodel.objectResult="An error occured "

                finish()
            }
        })

        mStringRequest.retryPolicy = DefaultRetryPolicy(
            60000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        mRequestQueue.add(mStringRequest)

        mRequestQueue.add(mStringRequest)

    }
















}