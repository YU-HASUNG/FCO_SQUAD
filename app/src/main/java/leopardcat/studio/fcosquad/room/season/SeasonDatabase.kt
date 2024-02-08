package leopardcat.studio.fcosquad.room.season

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Season::class], version = 1)
abstract class SeasonDatabase: RoomDatabase() {

    abstract fun seasonDao(): SeasonDao

    companion object{

        private var INSTANCE: SeasonDatabase? = null

        fun getDatabase(context: Context): SeasonDatabase?{

            if(INSTANCE == null){

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    SeasonDatabase::class.java,
                    "season_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}