import java.util.Date

data class WeatherDataForecast(
    val dateTime: Date,
    val temperature: Int,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val description: String,
    val iconUrl: String
)
