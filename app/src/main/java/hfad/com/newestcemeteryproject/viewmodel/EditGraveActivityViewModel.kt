package hfad.com.newestcemeteryproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hfad.com.newestcemeteryproject.data.CemeteryRepo
import hfad.com.newestcemeteryproject.data.CemeteryRoomDatabase
import hfad.com.newestcemeteryproject.data.Grave
import kotlinx.coroutines.*

class EditGraveActivityViewModel(application: Application): AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository: CemeteryRepo

    init {
        val cemDao = CemeteryRoomDatabase.getDatabase(application, uiScope).cemDao()
        repository = CemeteryRepo(cemDao)
    }

    /*
        1. function to get a grave with matching id
     */

    private val _grave = MutableLiveData<Grave>()
    val grave : LiveData<Grave>
    get() = _grave

    fun getGraveWithId(id: Int){
        uiScope.launch {
            _grave.value = susGetGraveWithId(id)
        }
    }

    private suspend fun susGetGraveWithId(id: Int): Grave{
        return withContext(Dispatchers.IO){
            val grave = repository.getGraveWithRowId(id)
            grave
        }
    }

    fun insertGrave(grave: Grave){
         uiScope.launch {
             susInsertGrave(grave)
         }
    }

    private suspend fun susInsertGrave(grave: Grave){
        withContext(Dispatchers.IO){
            repository.insertGrave(grave)
        }
    }
}