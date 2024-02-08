package leopardcat.studio.fcosquad.room.division

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Division::class], version = 1)
abstract class DivisionDatabase: RoomDatabase() {

    abstract fun divisionDao(): DivisionDao

    companion object{

        private var INSTANCE: DivisionDatabase? = null

        fun getDatabase(context: Context): DivisionDatabase?{

            if(INSTANCE == null){

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    DivisionDatabase::class.java,
                    "division_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}