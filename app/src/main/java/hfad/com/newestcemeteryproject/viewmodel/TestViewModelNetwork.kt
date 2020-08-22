package hfad.com.newestcemeteryproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import hfad.com.newestcemeteryproject.data.CemeteryRepo
import hfad.com.newestcemeteryproject.data.CemeteryRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TestViewModelNetwork(application: Application): AndroidViewModel(application) {

    val viewModelJob = Job()

    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val repository: CemeteryRepo


    init {
        val cemDao = CemeteryRoomDatabase.getDatabase(application, uiScope).cemDao()
        repository = CemeteryRepo(cemDao)
        viewModelScope.launch {
            repository.refreshVideos()
        }
    }

    val cemeteryNetworkList = repository.allCems
}