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