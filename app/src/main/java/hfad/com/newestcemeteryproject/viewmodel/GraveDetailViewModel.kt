package hfad.com.newestcemeteryproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hfad.com.newestcemeteryproject.data.CemeteryRepo
import hfad.com.newestcemeteryproject.data.CemeteryRoomDatabase
import hfad.com.newestcemeteryproject.data.Grave
import kotlinx.coroutines.*

class GraveDetailViewModel(application: Application): AndroidViewModel(application) {

    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val repository: CemeteryRepo

    private val _grave = MutableLiveData<Grave>()
    val grave: LiveData<Grave>
    get() =  _grave

    init {
        val cemDao = CemeteryRoomDatabase.getDatabase(application, viewModelScope).cemDao()
        repository = CemeteryRepo(cemDao)
    }

    fun deleteGraveWithId(id: Int){
        uiScope.launch {
            repository.deleteGraveWithId(id)
        }
    }


    fun getGraveWithRowId(rowId: Int){
        uiScope.launch {
            _grave.value = repository.getGraveWithRowId(rowId)
        }
    }

}