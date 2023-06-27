package pl.training

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun <T> createMockApi(
    type: Class<T>,
    vararg mockHttpResponses: MockHttpResponse,
    baseUrl: String = "http://localhost/"
    ): T {
    val mockInterceptor = MockNetworkInterceptor()
    mockHttpResponses.forEach { mockInterceptor.addResponse(it) }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(BASIC))
        .addInterceptor(mockInterceptor)
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(type)
}
