package id.co.integrapratama.sdk.core.data.remote

import id.co.integrapratama.logsdk.LogSdk
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

class LogSdkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val startTime = System.nanoTime()
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            LogSdk.json("REQUEST", """"{"error": "${e.message}"}""")
            throw e
        }
        val endTime = System.nanoTime()

        // Extract request body
        val requestBody = request.body
        val requestBuffer = Buffer()
        requestBody?.writeTo(requestBuffer)
        val requestJson = requestBuffer.readUtf8()

        // Extract response body
        val responseBody = response.peekBody(Long.MAX_VALUE)
        val responseJson = responseBody.string()

        // Log using your SDK
        LogSdk.json(
            "REQUEST",
            """
            {
              "url": "${request.url}",
              "method": "${request.method}",
              "duration_ms": ${(endTime - startTime) / 1_000_000},
              "request": $requestJson,
              "response": $responseJson
            }
            """.trimIndent()
        )

        return response
    }
}
