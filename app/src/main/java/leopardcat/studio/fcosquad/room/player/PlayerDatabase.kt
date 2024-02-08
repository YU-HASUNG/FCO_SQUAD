package leopardcat.studio.fcosquad.room.player

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Player::class], version = 1)
abstract class PlayerDatabase: RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object{

        private var INSTANCE: PlayerDatabase? = null

        fun getDatabase(context: Context): PlayerDatabase?{

            if(INSTANCE == null){

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    PlayerDatabase::class.java,
                    "player_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}