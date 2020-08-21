package hfad.com.newestcemeteryproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hfad.com.newestcemeteryproject.data.Cemetery
import hfad.com.newestcemeteryproject.data.CemeteryRepo
import hfad.com.newestcemeteryproject.data.CemeteryRoomDatabase
import hfad.com.newestcemeteryproject.data.Grave
import kotlinx.coroutines.*

class CemeteryViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job() //1.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)     //3.

    private val repository: CemeteryRepo
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCems: LiveData<List<Cemetery>>

    private val _gravesWithId = MutableLiveData<List<Grave>>() //can change we need it mutable because it will be set over and over by database query results view model can change this property
    val gravesWithId: LiveData<List<Grave>>                     //cannot change, we expose this to other classes (incapsulation)
        get() =  _gravesWithId

    private val _cemetery = MutableLiveData<Cemetery>()
    val cemetery: LiveData<Cemetery>
        get() = _cemetery

    init {
        val wordsDao = CemeteryRoomDatabase.getDatabase(application, viewModelScope).cemDao()
        repository = CemeteryRepo(wordsDao)
        allCems = repository.allCems
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertCemetery(word: Cemetery) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCemetery(word)
    }

    //Call this when we need grave list and then observe the list from CemeteryDetailActivity
    fun getGraveList(id: Int){
        uiScope.launch {
            _gravesWithId.value = getGraves(id)
            Log.i("ViewModel", "cemetery id is $id")
        }
    }

    private suspend fun getGraves(id: Int): List<Grave>{
        return withContext(Dispatchers.IO){
            val graveList = repository.getGravesWithId(id)
            graveList
        }
    }
    fun getCemetery(id: Int){
        uiScope.launch {
            _cemetery.value = getCemeteryWithId(id)
        }
    }

    private suspend fun getCemeteryWithId(id: Int): Cemetery? {
        return withContext(Dispatchers.IO) {
            val cemetery = repository.getCemeteryWithId(id)

            cemetery
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

    fun sendCemeteryToNetwork(cemetery: Cemetery){
            repository.addCemetery(cemetery){
//                if (it?.userId != null) {
//                    // it = newly added user parsed as response
//                    // it?.id = newly added user ID
//                } else {
//                    Timber.d("Error registering new user")
//                }
            }
    }

    fun sendGraveToNetwork(grave: Grave){
        repository.addGrave(grave){
            //if (it?.userId != null) {
//                    // it = newly added user parsed as response
//                    // it?.id = newly added user ID
//                } else {
//                    Timber.d("Error registering new user")
//                }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
