package hfad.com.newestcemeteryproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cemetery_table")
data class Cemetery(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_number")
    val id: Int = 0,

    val cemeteryName: String,

    val cemeteryLocation: String,

    val cemeteryState: String,

    val cemeteryCounty: String,

    val township: String,

    val range: String,

    val spot: String,

    val firstYear: String,

    val section: String

)

@Entity(tableName = "graves")
data class Grave(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val cemeteryId: Int,

    val firstName: String,

    val lastName: String,

    val birthDate: String,

    val deathDate: String,

    val marriageYear: String,

    val comment: String,

    val graveNumber: String
)