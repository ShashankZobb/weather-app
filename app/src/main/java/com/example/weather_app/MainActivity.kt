package com.example.weather_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.models.weather_data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class MainActivity : AppCompatActivity(){
    lateinit private var data: weather_data
    private var city: String = "mumbai"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val btn = findViewById<Button>(R.id.btn)
        val input = findViewById<EditText>(R.id.city_input)
        val api = Apiinterface.create()
        btn.setOnClickListener() {
            city = input.text.toString().lowercase()
            if(city.length == 0){
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
                city = "mumbai"
            }
            try {
                api.get_data(city).enqueue(object : Callback<weather_data> {
                    override fun onResponse(
                        call: Call<weather_data>,
                        response: Response<weather_data>
                    ) {
                        if (response.isSuccessful) {
                            data = response.body()!!
                            update_data()
                        }
                        else{
                            Toast.makeText(this@MainActivity, "Please enter a valid city", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<weather_data>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Sorry we were unable to process your request. Please try again", Toast.LENGTH_SHORT).show()
                    }

                })
            } catch (e1: Exception) {
                Log.d("MainActivity", e1.message, e1.cause)
            }
        }
    }
    fun update_data() {
        val wind_speed = data.wind.speed.toString()+"m/s"
        binding.windData.text = wind_speed
        binding.tempData.text = (data.main.temp-273).toString().subSequence(0, 4)
        val data_id = data.weather[0].id
        if(data_id in 200..232){
            binding.weatherImg.setImageResource(R.drawable.storm)
            binding.weatherData.text = "Strom"
        }
        else if(data_id in 300..531){
            binding.weatherImg.setImageResource(R.drawable.rain)
            binding.weatherData.text = "Rain"
        }
        else if(data_id in 600..622){
            binding.weatherImg.setImageResource(R.drawable.snowflake)
            binding.weatherData.text = "Snow"
        }
        else if(data_id in 700..721){
            binding.weatherImg.setImageResource(R.drawable.storm)
            binding.weatherData.text = "Dust"
        }
        else if(data_id == 800){
            binding.weatherImg.setImageResource(R.drawable.sunny)
            binding.weatherData.text = "Clear"
        }
        else{
            binding.weatherImg.setImageResource(R.drawable.cloud)
            binding.weatherData.text = "Cloudy"
        }
        binding.humidityData.text = data.main.humidity.toString()
    }
    private val request_location = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        isGranted: Boolean ->
            if(isGranted){
                Log.i("Permission: ", "Granted")
            }
            else{
                Log.i("Permission: ", "Denied")
            }
    }
}