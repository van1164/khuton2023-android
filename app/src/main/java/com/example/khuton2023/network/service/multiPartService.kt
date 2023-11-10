package com.example.khuton2023.network.service

import com.example.khuton2023.data.dao.ProblemResponse
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface multiPartService {
    @Multipart
    @POST("/api/v1/generation/pdf")
    fun uploadFile(
        @Part upload_file : MultipartBody.Part
    ): Call<ProblemResponse>

    @POST("/api/v1/chat/user")
    fun createName(
        @Query("client_id") client_id : String,
        @Body reqeust :Request
    ): Call<ProblemResponse>

    data class Request(
        @SerializedName("persona")
        val persona : Map<String,Any> = emptyMap()
    )

    companion object{
        private const val BASE_URL = "http://facerain-dev.iptime.org:5000/"

        private val gson =
            GsonBuilder()
                .setLenient()
                .create()
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .readTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(300, TimeUnit.SECONDS)
            .build();
        fun create() : multiPartService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(multiPartService::class.java)
        }
    }
}