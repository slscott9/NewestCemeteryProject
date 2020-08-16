package hfad.com.newestcemeteryproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Cemetery::class, Grave::class], version = 1, exportSchema = false)
abstract class CemeteryRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): CemeteryDao

    companion object {
        @Volatile
        private var INSTANCE: CemeteryRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CemeteryRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CemeteryRoomDatabase::class.java,
                        "word_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
        }
    }
}