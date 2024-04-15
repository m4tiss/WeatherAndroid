import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesCitiesViewModel : ViewModel() {

    private val _favouritesCities = MutableLiveData<List<String>>()
    val favouritesCities: LiveData<List<String>>
        get() = _favouritesCities

    private val sharedPreferencesKey = "favouriteCities"


    fun loadFavouriteCities(context: Context) {
        viewModelScope.launch {
            val sharedPreferences =
                context.getSharedPreferences("favourite_cities", Context.MODE_PRIVATE)
            val citiesSet = sharedPreferences.getStringSet(sharedPreferencesKey, setOf())
            _favouritesCities.value = citiesSet?.toList() ?: emptyList()
        }
    }

    fun addFavouriteCity(context: Context, city: String) {
        viewModelScope.launch {
            val currentCities = _favouritesCities.value?.toMutableList() ?: mutableListOf()
            if (!currentCities.contains(city)) {
                currentCities.add(city)
                _favouritesCities.postValue(currentCities)
                saveFavouriteCities(context, currentCities)
            }
        }
    }

    fun removeFavouriteCity(context: Context, city: String) {
        viewModelScope.launch {
            val currentCities = _favouritesCities.value?.toMutableList() ?: mutableListOf()
            if (currentCities.contains(city)) {
                currentCities.remove(city)
                _favouritesCities.postValue(currentCities)
                saveFavouriteCities(context, currentCities)
            }
        }
    }

    private suspend fun saveFavouriteCities(context: Context, cities: List<String>) {
        withContext(Dispatchers.IO) {
            val sharedPreferences =
                context.getSharedPreferences("favourite_cities", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putStringSet(sharedPreferencesKey, cities.toSet())
            editor.apply()
        }
    }

}