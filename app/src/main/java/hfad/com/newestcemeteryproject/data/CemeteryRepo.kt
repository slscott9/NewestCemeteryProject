package hfad.com.newestcemeteryproject.data

import androidx.lifecycle.LiveData

class CemeteryRepo(private val cemDao: CemeteryDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCems: LiveData<List<Cemetery>> = cemDao.getAllCemeteries()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.

    fun insertCemetery(word: Cemetery) {
        cemDao.insertCemetery(word)
    }

    fun insertGrave(grave: Grave){
        cemDao.insertGrave(grave)
    }

    fun getGravesWithId(cemeteryId: Int): List<Grave>{
        return cemDao.getAllGravesWithId(cemeteryId)
    }

    fun getCemeteryWithId(id: Int): Cemetery{
        return cemDao.getCemeteryWithRowNum(id)
    }




}
