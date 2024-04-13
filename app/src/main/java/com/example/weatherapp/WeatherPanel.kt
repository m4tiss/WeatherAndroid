import android.graphics.Bitmap

data class WeatherPanel(
    val temperature: Int,
    val weatherIconBitmap: Bitmap?,
    val date: String,
    val feels: Int,
    val pressure: Int
)
