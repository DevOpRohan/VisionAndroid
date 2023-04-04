package org.tensorflow.lite.examples.objectdetection

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.prianshuprasad.assistant.messageData
import java.util.*


class chatActivity : AppCompatActivity(), TextToSpeech.OnInitListener  {
    lateinit var speechIntent: Intent
    private lateinit var speechRecognizer: SpeechRecognizer
    private var tts: TextToSpeech? = null
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

//            val objectViewmodel by viewModels<ObjectViewmodel>()

    private lateinit var mAdapter: adapter
    lateinit var ll: LinearLayoutManager
    var usernamei: String? = ""
    private lateinit var rcview: RecyclerView
    private lateinit var storage: SharedPreferences
    private lateinit var Editor: SharedPreferences.Editor

    var MessageArray: ArrayList<messageData> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        tts = TextToSpeech(this, this)

        val settings = applicationContext.getSharedPreferences("Userdata", 0)

//        usernamei = settings.getString("username", "Unknown")
        storage = applicationContext.getSharedPreferences("Userdata", 0)
        Editor = storage.edit()
        MessageArray.add(messageData("Welcome", 0))


        Handler().postDelayed({

            speakOut("Welcome")

        }, 1000)


        rcview = findViewById(R.id.recyclerView)





        ll = LinearLayoutManager(this)
        ll.stackFromEnd = true     // items gravity sticks to bottom
        ll.reverseLayout = false

        val recyclerView= findViewById<RecyclerView>(R.id.recyclerView)
        val edittext1 = findViewById<EditText>(R.id.edittext1)
        val mic = findViewById<ImageView>(R.id.mic)
        val touchme = findViewById<ImageView>(R.id.touchme)


        recyclerView.layoutManager = ll

        mAdapter = adapter(this)

        recyclerView.adapter = mAdapter

        mAdapter.updatenews(MessageArray)

        edittext1.hint = "Hello $usernamei"

        mic.setOnClickListener {
            Talk()
        }
        touchme.setOnClickListener {
            Talk()
        }


        //
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                1)

        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)


        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )


        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )


        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")


//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, 1)

//        val intent= Intent(this,cameraScene::class.java)
//        startActivity(intent)




//        supportFragmentManager

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.container, MainFragment())
//            .commitNow()


//        ObjectViewmodel.objectListener.observeForever {
//            if(it!=""){
//                speakOut(it)
//                MessageArray.removeLast()
//                MessageArray.add(messageData(it, 1))
//                mAdapter.updatenews(MessageArray)
//
//            }
//        }


    }


    fun API(str: String): String {

        MessageArray.add(messageData("%%%%", 1))
        mAdapter.updatenews(MessageArray)




        runOnUiThread {

            while (true) {
                if (!tts!!.isSpeaking)
                    break;
                Thread.sleep(1000)

            }

            var url ="https://walrus-app-hodhq.ondigitalocean.app/vision?q="
            url+=str;

            if(str.contains("open"))
            {
                val intent = Intent(this@chatActivity,MainActivity::class.java)
                startActivity(intent)
            }



            val mRequestQueue = Volley.newRequestQueue(this)

            // String Request initialized
            val mStringRequest = StringRequest(Request.Method.GET, url, object :
                Response.Listener<String?> {
                // display the response on screen


                override fun onResponse(response: String?) {
                    if (response != null) {

                        var pResp=response;

                        if(pResp[0]=='@'){
                            if(pResp.contains("ocr")|| pResp.contains("scene")){

                                val intent= Intent(this@chatActivity,cameraScene::class.java)
                                startActivity(intent)

                            }else if(pResp.contains("exit")){
                                finish()
                            }
                            else{

                                val item = pResp.substring(1)
                                val intent = Intent(this@chatActivity, MainActivity::class.java)
                                intent.putExtra("object",item)
                                startActivity(intent)


                            }

                        }else {
                            speakOut(pResp)
                            MessageArray.removeLast()
                            MessageArray.add(messageData(pResp, 1))
                            mAdapter.updatenews(MessageArray)
                        }
                    };
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    MessageArray.removeLast()
                    speakOut("Something wrong happend! \nCheck Your connectivity")
                    MessageArray.add(messageData("Something wrong happend! \nCheck Your connectivity", 1))
                    mAdapter.updatenews(MessageArray)
                    Log.i(ContentValues.TAG, "Error :" + error.toString())
                }
            })
            mRequestQueue.add(mStringRequest)




        }
        return "Reply from API"
    }


    fun Talk() {
        runOnUiThread {
            while (true) {
                if (!tts!!.isSpeaking)
                    break;
                Thread.sleep(1000)
            }


            speakOut(" say Something");
            Thread.sleep(1000)
            while (true) {
                if (!tts!!.isSpeaking)
                    break;
                Thread.sleep(1000)
            }

            try {
                startActivityForResult(speechIntent, 1)
            } catch (e: Exception) {
                // on below line we are displaying error message in toast
                Toast.makeText(
                    this, " " + e.message, Toast.LENGTH_SHORT).show()
            }


        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request
        // code with our result code.
        if (requestCode == 1) {
            // on below line we are checking if result code is ok
            if (resultCode == RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                MessageArray.add(messageData(res[0], 0))
                mAdapter.updatenews(MessageArray)
                API(res[0])


            }
        }

    }


    // Text to Specch
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
                Toast.makeText(this, "Something BAd Occured", Toast.LENGTH_LONG).show()
            } else {
//                btnSpeak!!.isEnabled = true
            }
        }
    }

    private fun speakOut(str: String) {
        val text = str;
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    fun onitemclicked(messageData: messageData) {

    }

    override fun onResume() {
        super.onResume()

        if(ObjectViewmodel.objectResult!=""){
            val it= ObjectViewmodel.objectResult
            if(it!=""){
                speakOut(it)
                MessageArray.removeLast()
                MessageArray.add(messageData(it, 1))
                mAdapter.updatenews(MessageArray)
                ObjectViewmodel.objectResult=""

            }

        }

    }

    fun scrolltoPos(x: Int) {
        rcview.scrollToPosition(x)
    }





}
