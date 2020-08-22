package hfad.com.newestcemeteryproject.data

import androidx.lifecycle.LiveData
import hfad.com.newestcemeteryproject.network.RestApi
import hfad.com.newestcemeteryproject.network.ServiceBuilder
import hfad.com.newestcemeteryproject.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response


/*
    Repository is single source of truth for the application. Contains local database query method from the dao and
    Network request methods (put and get)
 */
class CemeteryRepo(private val cemDao: CemeteryDao) {

    val allCemeteries = cemDao.getAllCemeteries()


    suspend fun insertCemetery(word: Cemetery) {
        cemDao.insertCemetery(word)
    }

    suspend fun insertGrave(grave: Grave){
        cemDao.insertGrave(grave)
    }

    suspend fun getGravesWithId(cemeteryId: Int): List<Grave>{
        return cemDao.getAllGravesWithId(cemeteryId)
    }

    suspend fun getCemeteryWithId(id: Int): Cemetery{
        return cemDao.getCemeteryWithRowNum(id)
    }

    suspend fun deleteGraveWithId(cemeteryId: Int){
        cemDao.deleteGraveWithId(cemeteryId)
    }

    suspend fun getGraveWithRowId(rowId: Int): Grave{
        return cemDao.getGraveWithRowId(rowId)
    }


    //This method is asynchronous
    fun addCemetery(cemetery: Cemetery, onResult: (Cemetery?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addCemetery(
            cemId = cemetery.id.toString(),
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
                    val addedCemetery = response.body()
                    onResult(addedCemetery)
                }
            }
        )
    }

    fun addGrave(grave: Grave, onResult: (Grave?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.addGrave(
            id = grave.id,
            cemeteryId = grave.cemeteryId,
            firstName = grave.firstName,
            lastName = grave.lastName,
            bornDate = grave.birthDate,
            deathDate = grave.deathDate,
            marriageYear = grave.marriageYear,
            comment = grave.comment,
            graveNum = grave.graveNumber
            ).enqueue(

            object : retrofit2.Callback<Grave> {
                override fun onFailure(call: Call<Grave>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<Grave>, response: Response<Grave>) {
                    val addedGrave = response.body()
                    onResult(addedGrave)
                }
            }
        )
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val retrofit = ServiceBuilder.buildService(RestApi::class.java)
            val cemeteryNetworkList = retrofit.getCemeteriesFromNetwork().await()
            cemDao.insertCemeteryNetworkList(*cemeteryNetworkList.asDatabaseModel())
        }
    }

}


