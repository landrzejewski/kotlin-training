package pl.training

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockNetworkInterceptor : Interceptor {

    private val mockResponses = mutableListOf<MockHttpResponse>()
    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val mockResponse = mockResponses.find { request.url.toString().endsWith(it.path) } ?: return chain.proceed(request)
        if (!mockResponse.isPersistent) {
            mockResponses.remove(mockResponse)
        }
        Thread.sleep(mockResponse.delayInMilliseconds)
        return createResponse(request, mockResponse)
    }

    private fun createResponse(request: Request, mockResponse: MockHttpResponse) = Response.Builder()
        .code(mockResponse.status)
        .message(mockResponse.description)
        .protocol(Protocol.HTTP_1_1)
        .request(request)
        .body(toJson(mockResponse.body).toResponseBody(APPLICATION_JSON.toMediaType()))
        .build()

    private fun toJson(body: Any) = gson.toJson(body)

    fun addResponse(mockHttpResponse: MockHttpResponse) = mockResponses.add(mockHttpResponse)

    companion object {

        const val APPLICATION_JSON = "application/json"

    }

}
