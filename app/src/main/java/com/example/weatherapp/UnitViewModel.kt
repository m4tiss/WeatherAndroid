import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UnitViewModel : ViewModel() {
    private val _unit = MutableLiveData<String>()
    val unit: LiveData<String>
        get() = _unit

    init {
        _unit.value = "metric"
    }

    fun setUnit(newUnit: String) {
        _unit.value = newUnit
        println("Unit set to: $newUnit")
    }
}
