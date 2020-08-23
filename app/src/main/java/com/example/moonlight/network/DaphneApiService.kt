package com.example.moonlight.network


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://mars.udacity.com/"

private val retrofit = Retrofit.Builder()
  .addConverterFactory(ScalarsConverterFactory.create())
  .baseUrl(BASE_URL)
  .build()

interface DaphneApiService {
  @GET("realestate")
  fun getProperties(): Call<String>
}

object DaphneApi {
  val retrofitService: DaphneApiService by lazy {
    retrofit.create(DaphneApiService::class.java)
  }
}
