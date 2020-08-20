package hfad.com.newestcemeteryproject.data

import androidx.lifecycle.LiveData
import hfad.com.newestcemeteryproject.network.RestApi
import hfad.com.newestcemeteryproject.network.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response


/*
    Repository is single source of truth for the application. Contains local database query method from the dao and
    Network request methods (put and get)
 */
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


    //This method is asynchronous
    fun addUser(cemetery: Cemetery, onResult: (Cemetery?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addCemetery(cemId = cemetery.id.toString(),
                                cemName = cemetery.cemeteryName,
                                location = cemetery.cemeteryLocation,
                                county = cemetery.cemeteryCounty,
                                township = cemetery.township,
                                range = cemetery.range,
                                spot = cemetery.spot,
                                yearFounded = cemetery.firstYear,
                                section = cemetery.section,
                                state = cemetery.cemeteryState).enqueue(
            object : retrofit2.Callback<Cemetery> {
                override fun onFailure(call: Call<Cemetery>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<Cemetery>, response: Response<Cemetery>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }
}
