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


/*
    Note: Room uses its own dispatcher to run queries on a background thread.
    Your code should not use withContext(Dispatchers.IO) to call suspending room queries.
    It will complicate the code and make your queries run slower.

    Using suspend only says the function can be used for coroutines and will be non blocking when it is called.
    A suspend function does its work then resumes where it left off when it has a result.
    To be asynchronous we must call suspend function inside of coroutines in the view model
 */
class CemeteryViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job() //1.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)     //3.

    private val repository: CemeteryRepo

    private val _gravesWithId = MutableLiveData<List<Grave>>() //can change we need it mutable because it will be set over and over by database query results view model can change this property
    val gravesWithId: LiveData<List<Grave>>                     //cannot change, we expose this to other classes (incapsulation)
        get() =  _gravesWithId

    private val _cemetery = MutableLiveData<Cemetery>()
    val cemetery: LiveData<Cemetery>
        get() = _cemetery

     var newCemeteryCounter: Int


    init {
        val wordsDao = CemeteryRoomDatabase.getDatabase(application, viewModelScope).cemDao()
        repository = CemeteryRepo(wordsDao)
        viewModelScope.launch {
            repository.refreshVideos()
        }
        newCemeteryCounter = 0
    }

    val allCemeteries = repository.allCemeteries


    //different way to do coroutines
    fun insertCemetery(word: Cemetery) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCemetery(word)
        newCemeteryCounter += 1
    }

    //Call this when we need grave list and then observe the list from CemeteryDetailActivity
    fun getGraveList(id: Int){
        uiScope.launch {
            _gravesWithId.value = repository.getGravesWithId(id)
            Log.i("ViewModel", "cemetery id is $id")
        }
    }

    fun getCemetery(id: Int){
        uiScope.launch {
            _cemetery.value = repository.getCemeteryWithId(id)
        }
    }


    fun insertGrave(grave: Grave){
        uiScope.launch {
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
