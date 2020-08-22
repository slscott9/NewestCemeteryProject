package hfad.com.newestcemeteryproject.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CemeteryDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from cemetery_table")
    fun getAllCemeteries(): LiveData<List<Cemetery>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCemetery(cem: Cemetery)

    @Insert(onConflict = OnConflictStrategy.REPLACE) //if a grave has the same primary key it will replace it with the new data
    suspend fun insertGrave(grave: Grave)

    @Query("DELETE FROM cemetery_table")
    suspend fun deleteAll()

    @Query("select * from graves where cemeteryId= :cemId")
    suspend fun getAllGravesWithId(cemId: Int) : List<Grave>

    @Query("select * from  cemetery_table where row_number= :rowNum")
    suspend fun getCemeteryWithRowNum(rowNum: Int) : Cemetery

    @Query("delete from graves where id= :cemId")
    suspend fun deleteGraveWithId(cemId: Int)

    @Query("select * from graves where id= :rowId")
    suspend fun getGraveWithRowId(rowId: Int) : Grave



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCemeteryNetworkList(vararg cemeteryNetworkList: Cemetery)
}