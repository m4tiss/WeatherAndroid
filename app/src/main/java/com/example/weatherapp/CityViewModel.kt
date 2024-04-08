import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CityViewModel : ViewModel() {
    private val _city = MutableLiveData<String>()
    val city: LiveData<String>
        get() = _city

    init {
        _city.value = "Warsaw"
    }

    fun setCity(newCity: String) {
        _city.value = newCity
    }

}
