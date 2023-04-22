package org.tensorflow.lite.examples.objectdetection

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.prianshuprasad.assistant.messageData
import org.json.JSONObject
import java.util.*

class chatActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    lateinit var speechIntent: Intent
    private lateinit var speechRecognizer: SpeechRecognizer
    private var tts: TextToSpeech? = null
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var mAdapter: adapter
    lateinit var ll: LinearLayoutManager
    var usernamei: String? = ""
    private lateinit var rcview: RecyclerView
    private lateinit var storage: SharedPreferences
    private lateinit var Editor: SharedPreferences.Editor

    var MessageArray: ArrayList<messageData> = arrayListOf()

    // Boolean varaibles to control the flow of app
    private var isWaitingForResponse = false
    private var isTTSSpeaking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        tts = TextToSpeech(this, this)

        val settings = applicationContext.getSharedPreferences("Userdata", 0)
        storage = applicationContext.getSharedPreferences("Userdata", 0)
        Editor = storage.edit()
        MessageArray.add(messageData("Welcome", 0))

        Handler().postDelayed({
            speakOut("Welcome")
        }, 1000)

        rcview = findViewById(R.id.recyclerView)

        ll = LinearLayoutManager(this)
        ll.stackFromEnd = true
        ll.reverseLayout = false

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val edittext1 = findViewById<EditText>(R.id.edittext1)
        val mic = findViewById<ImageView>(R.id.mic)
        val touchme = findViewById<ImageView>(R.id.touchme)

        recyclerView.layoutManager = ll
        mAdapter = adapter(this)
        recyclerView.adapter = mAdapter
        mAdapter.updatenews(MessageArray)
        edittext1.hint = "Hello $usernamei"

        mic.setOnClickListener {
            if (!isWaitingForResponse && !isTTSSpeaking) {
                Talk()
            }
        }
        touchme.setOnClickListener {
            if (!isWaitingForResponse && !isTTSSpeaking) {
                Talk()
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                1
            )
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
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
    }

    fun API(str: String): String {
        // Set isWaitingForResponse to true when sending a request
        isWaitingForResponse = true

        MessageArray.add(messageData("%%%%", 1))
        mAdapter.updatenews(MessageArray)

        runOnUiThread {
            while (true) {
                if (!tts!!.isSpeaking)
                    break
                Thread.sleep(1000)
            }

            var url = "https://9085-54-243-246-120.ngrok.io/vision?q=\""
            url += str
            url += "\""

            Log.d("URL_VISION", url)

            val mRequestQueue = Volley.newRequestQueue(this)

            val mStringRequest = StringRequest(Request.Method.GET, url, object :
                Response.Listener<String?> {

                override fun onResponse(response: String?) {
                    if (response != null) {
                        val jsonResponse = JSONObject(response)
                        val pResp = jsonResponse.getString("message")

                        if (pResp.isEmpty())
                            return

                        if (pResp.startsWith("@")) {
                            val pRespTrimmed = pResp.substring(1)
                            if (pResp.contains("ocr") || pRespTrimmed.contains("vq")) {
                                val intent = Intent(this@chatActivity, cameraScene::class.java)
                                startActivity(intent)
                            } else if (pRespTrimmed.contains("exit")) {
                                finish()
                            } else {
                                val item = pRespTrimmed
                                val intent = Intent(this@chatActivity, MainActivity::class.java)
                                intent.putExtra("object", item)
                                startActivity(intent)
                            }
                        } else {
                            speakOut(pResp)
                            MessageArray.removeLast()
                            MessageArray.add(messageData(pResp, 1))
                            mAdapter.updatenews(MessageArray)
                        }
                    }
                    // Set isWaitingForResponse to false when the response is received
                    isWaitingForResponse = false
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    MessageArray.removeLast()
                    speakOut("Something wrong happend! \nCheck Your connectivity")
                    MessageArray.add(
                        messageData(
                            "Something wrong happend! \nCheck Your connectivity",
                            1
                        )
                    )
                    mAdapter.updatenews(MessageArray)
                    Log.i(ContentValues.TAG, "Error :" + error.toString())

                    // Set isWaitingForResponse to false when an error occurs
                    isWaitingForResponse = false
                }
            })

            mStringRequest.retryPolicy = DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            mRequestQueue.add(mStringRequest)
        }
        return "Reply from API"
    }

    fun Talk() {
        runOnUiThread {
            while (true) {
                if (!tts!!.isSpeaking)
                    break
                Thread.sleep(1000)
            }

            speakOut(" say Something")
            Thread.sleep(1000)
            while (true) {
                if (!tts!!.isSpeaking)
                    break
                Thread.sleep(1000)
            }

            try {
                startActivityForResult(speechIntent, 1)
            } catch (e: Exception) {
                Toast.makeText(
                    this, " " + e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                MessageArray.add(messageData(res[0], 0))
                mAdapter.updatenews(MessageArray)
                API(res[0])
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
                Toast.makeText(this, "Something BAd Occured", Toast.LENGTH_LONG).show()
            }
        }
    }

private fun speakOut(str: String) {
    val text = str
    isTTSSpeaking = true
    tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
        }

        override fun onDone(utteranceId: String?) {
            isTTSSpeaking = false
        }

        override fun onError(utteranceId: String?) {
            isTTSSpeaking = false
        }
    })
}
    public override fun onDestroy() {
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

        if (ObjectViewmodel.objectResult != "") {
            val it = ObjectViewmodel.objectResult
            if (it != "") {
                speakOut(it)
                MessageArray.removeLast()
                MessageArray.add(messageData(it, 1))
                mAdapter.updatenews(MessageArray)
                ObjectViewmodel.objectResult = ""
            }
        }
    }

    fun scrolltoPos(x: Int) {
        rcview.scrollToPosition(x)
    }
}