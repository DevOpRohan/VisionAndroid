//import android.util.Base64
//import com.android.volley.*
//import com.android.volley.toolbox.HttpHeaderParser
//import java.io.ByteArrayOutputStream
//import java.io.IOException
//import java.io.UnsupportedEncodingException
//import java.net.URLConnection
//import java.nio.charset.Charset
//import java.util.HashMap
//import org.apache.commons.io.FileUtils
//import java.io.File
//
//class MultipartRequest(
//    method: Int,
//    url: String,
//    private val mListener: Response.Listener<String>,
//    errorListener: Response.ErrorListener,
//    private val mFile: File
//) : Request<String>(method, url, errorListener) {
//
//    private val mHeaders: MutableMap<String, String> = HashMap()
//
//    @Throws(AuthFailureError::class)
//    override fun getHeaders(): Map<String, String> {
//        return mHeaders
//    }
//
//    fun setHeader(name: String, value: String) {
//        mHeaders[name] = value
//    }
//
//    override fun getBodyContentType(): String {
//        return "multipart/form-data; boundary=$BOUNDARY"
//    }
//
//    @Throws(AuthFailureError::class)
//    override fun getBody(): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        try {
//            outputStream.write(("--$BOUNDARY\r\n").toByteArray())
//            outputStream.write(("Content-Disposition: form-data; name=\"image\"; filename=\"${mFile.name}\"\r\n").toByteArray())
//            outputStream.write(("Content-Type: ${URLConnection.guessContentTypeFromName(mFile.name)}\r\n\r\n").toByteArray())
//            outputStream.write(FileUtils.readFileToByteArray(mFile))
//            outputStream.write(("\r\n--$BOUNDARY--\r\n").toByteArray())
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return outputStream.toByteArray()
//    }
//
//    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
//        return try {
//            val jsonString = String(response.data, Charset.forName(HttpHeaderParser.parseCharset(response.headers)))
//            Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response))
//        } catch (e: UnsupportedEncodingException) {
//            Response.error(ParseError(e))
//        }
//    }
//
//    override fun deliverResponse(response: String) {
//
//        mListener.onResponse(response)
//    }
//
//    companion object {
//        private const val BOUNDARY = "*****"
//    }
//}
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class MultipartRequest(
    url: String,
    private val mListener: Response.Listener<String>,
    private val mErrorListener: Response.ErrorListener,
    private val mHeaders: Map<String, String>?,
    private val mParams: Map<String, String>?,
    private val mFilePartName: String,
    private val mFile: ByteArray,
    private val mMimeType: String
) : StringRequest(Method.POST, url, mListener, mErrorListener) {

    private val twoHyphens = "--"
    private val lineEnd = "\r\n"
    private val boundary = "apiclient-" + System.currentTimeMillis()

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return mHeaders ?: super.getHeaders()
    }

@Throws(AuthFailureError::class)
override fun getParams(): Map<String, String> {
    return mParams ?: emptyMap()
}

    override fun getBodyContentType(): String {
        return "multipart/form-data;boundary=$boundary"
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        val bos = ByteArrayOutputStream()
        val dos = DataOutputStream(bos)

        try {
            // populate text payload
            mParams?.let {
                for ((key, value) in it.entries) {
                    // the first parameter needs one less new line
                    buildTextPart(dos, key, value)
                }
            }

            // populate data byte payload
            buildDataPart(dos, mFilePartName, mFile, mMimeType)

            // send multipart form data necessary after file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)

            return bos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return super.getBody()
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
        return try {
            val result = String(response.data)
            Response.success(result, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(com.android.volley.ParseError(e))
        }
    }

    @Throws(IOException::class)
    private fun buildTextPart(dataOutputStream: DataOutputStream, parameterName: String, parameterValue: String) {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd)
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"$parameterName\"$lineEnd")
        dataOutputStream.writeBytes(lineEnd)
        dataOutputStream.writeBytes(parameterValue + lineEnd)
    }

    @Throws(IOException::class)
    private fun buildDataPart(dataOutputStream: DataOutputStream, parameterName: String, dataFile: ByteArray, mimeType: String) {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd)
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                parameterName + "\"; filename=\"" + parameterName + "\"" + lineEnd)
        if (mimeType.isNotEmpty()) {
            dataOutputStream.writeBytes("Content-Type: $mimeType$lineEnd")
        }
        dataOutputStream.writeBytes(lineEnd)

        val fileInputStream = ByteArrayInputStream(dataFile)
        var bytesAvailable = fileInputStream.available()

        val maxBufferSize = 1024 * 1024
        var bufferSize = Math.min(bytesAvailable, maxBufferSize)
        val buffer = ByteArray(bufferSize)

        // read file and write it into form...
        var bytesRead = fileInputStream.read(buffer, 0, bufferSize)

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize)
            bytesAvailable = fileInputStream.available()
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
        }

        dataOutputStream.writeBytes(lineEnd)
    }
}