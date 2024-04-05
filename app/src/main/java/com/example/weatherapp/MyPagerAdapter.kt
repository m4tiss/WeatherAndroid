import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.FragmentAdvancedInfo
import com.example.weatherapp.FragmentSettings
import com.example.weatherapp.FragmentTodayWeather

class MyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentSettings()
            1 -> FragmentTodayWeather()
            2 -> FragmentAdvancedInfo()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
