package com.example.weather_app

import com.example.weather_app.models.weather_data
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Apiinterface {
    @GET("weather?appid=4ac37440d1e71d12e64b842d233af3cc")
    fun get_data(
        @Query("q") city: String
    ): Call<weather_data>

    companion object {
        var base = "https://api.openweathermap.org/data/2.5/"

        fun create(): Apiinterface {
            val retrofit1 = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(base).build()
            return retrofit1.create(Apiinterface::class.java)
        }
    }
}