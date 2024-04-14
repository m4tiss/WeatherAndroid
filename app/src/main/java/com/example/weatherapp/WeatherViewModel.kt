import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData>
        get() = _weatherData

    init {
        val defaultCity = _weatherData.value?.city ?: "Warsaw"
        val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
        val apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=${defaultCity}&appid=$apiKey&units=metric"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = URL(apiUrl).readText()
                val weatherData = parseWeatherData(response)
                _weatherData.postValue(weatherData)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun fetchWeather(context: Context, unit: String) {
        val defaultCity = _weatherData.value?.city ?: "Warsaw"
        val apiKey = "faefbb6cd2775b6c28ba6c3a080ead31"
        val apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=${defaultCity}&appid=$apiKey&units=$unit"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = URL(apiUrl).readText()
                val weatherData = parseWeatherData(response)
                _weatherData.postValue(weatherData)

                saveWeatherDataToFile(context,defaultCity, weatherData)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun updateCity(newCity: String) {
        val currentWeatherData = _weatherData.value
        if (currentWeatherData != null) {
            val updatedWeatherData = WeatherData(
                newCity,
                currentWeatherData.latitude,
                currentWeatherData.longitude,
                currentWeatherData.time,
                currentWeatherData.temperature,
                currentWeatherData.pressure,
                currentWeatherData.description,
                currentWeatherData.humidity,
                currentWeatherData.windSpeed,
                currentWeatherData.windDeg,
                currentWeatherData.clouds,
                currentWeatherData.visibility
            )
            _weatherData.value = updatedWeatherData
        }
    }


    private fun parseWeatherData(jsonString: String): WeatherData {
        val jsonObject = JSONObject(jsonString)

        val city = jsonObject.getString("name")

        val coord = jsonObject.getJSONObject("coord")
        val latitude = coord.getDouble("lat")
        val longitude = coord.getDouble("lon")

        val weatherArray = jsonObject.getJSONArray("weather")
        val weatherObject = weatherArray.getJSONObject(0)
        val description = weatherObject.getString("description")

        val main = jsonObject.getJSONObject("main")
        val temperature = main.getDouble("temp")
        val pressure = main.getInt("pressure")
        val humidity = main.getInt("humidity")

        val wind = jsonObject.getJSONObject("wind")
        val windSpeed = wind.getDouble("speed")
        val windDeg = wind.getInt("deg")

        val clouds = jsonObject.getJSONObject("clouds")
        val cloudiness = clouds.getInt("all")

        val visibility = jsonObject.getInt("visibility")


        val timezoneOffset = jsonObject.getLong("timezone")
        val currentTimeUTC = System.currentTimeMillis() / 1000
        val localTime = currentTimeUTC + timezoneOffset

        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val sdfTime = sdf.format(localTime * 1000)

        return WeatherData(city, latitude, longitude, sdfTime, temperature, pressure, description, humidity, windSpeed, windDeg, cloudiness,visibility)
    }

    private fun saveWeatherDataToFile(context: Context,city: String, weatherData: WeatherData) {
        val fileName = "${city}_today.json"

        val json = JSONObject().apply {
            put("city", weatherData.city)
            put("latitude", weatherData.latitude)
            put("longitude", weatherData.longitude)
            put("time", weatherData.time)
            put("temperature", weatherData.temperature)
            put("pressure", weatherData.pressure)
            put("description", weatherData.description)
            put("humidity", weatherData.humidity)
            put("windSpeed", weatherData.windSpeed)
            put("windDeg", weatherData.windDeg)
            put("clouds", weatherData.clouds)
            put("visibility", weatherData.visibility)
        }

        val fileDir = context.filesDir
        val file = File(fileDir, fileName)

// Create FileWriter instance for file
        val fileWriter = FileWriter(file)

// Write JSON data to file
        fileWriter.write(json.toString())

// Close FileWriter to save changes
        fileWriter.close()

        println("Dane zostały zapisane do pliku: ${file.absolutePath}")


        }

//            val file = File(fileDir, fileName)
//            val file = File(context.filesDir, fileName)
//            file.writeText(json.toString())
//            println("Zapisałem")
//        } catch (e: IOException) {
//            e.printStackTrace()
//            println("NieZapisałem")
//        }
//    }

}
