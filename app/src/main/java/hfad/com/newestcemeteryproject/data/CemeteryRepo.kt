package hfad.com.newestcemeteryproject.data

import androidx.lifecycle.LiveData

class CemeteryRepo(private val cemDao: CemeteryDao) {


    val allCems: LiveData<List<Cemetery>> = cemDao.getAllCemeteries()



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

    fun deleteGraveWithId(cemeteryId: Int){
        cemDao.deleteGraveWithId(cemeteryId)
    }

    fun getGraveWithRowId(rowId: Int): Grave{
        return cemDao.getGraveWithRowId(rowId)
    }




}
