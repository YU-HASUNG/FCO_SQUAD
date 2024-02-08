package leopardcat.studio.fcosquad.room.position

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Position::class], version = 1)
abstract class PositionDatabase: RoomDatabase() {

    abstract fun positionDao(): PositionDao

    companion object{

        private var INSTANCE: PositionDatabase? = null

        fun getDatabase(context: Context): PositionDatabase?{

            if(INSTANCE == null){

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    PositionDatabase::class.java,
                    "position_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}