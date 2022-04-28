package com.example.api

import com.example.api.data.GetWeatherByCityNameResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface OpenWeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = "33bb1ed92538bdebe147ed23fdff735d",
        @Query("units") units: String = "metric"
    ): GetWeatherByCityNameResponse
}

object OpenWeatherApi {
    val retrofitService : OpenWeatherApiService by lazy { retrofit.create(OpenWeatherApiService::class.java) }
}
