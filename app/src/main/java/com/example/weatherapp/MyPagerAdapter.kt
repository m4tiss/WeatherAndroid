import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.weatherapp.FragmentAdvancedInfo
import com.example.weatherapp.FragmentSettings
import com.example.weatherapp.FragmentTodayWeather

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentSettings()
            1 -> FragmentTodayWeather()
            2 -> FragmentAdvancedInfo()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return 3
    }
}
