package leopardcat.studio.fcosquad.room.player

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player")
    fun getAllPlayer(): List<Player>

    @Insert
    fun insertPlayer(player: Player)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIfNotExists(player: Player)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(players: List<Player>)
    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)

    @Query("SELECT name FROM player WHERE spid = :spid")
    fun getPlayerName(spid: String): String?

    @Query("SELECT * FROM player WHERE name LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): List<Player>

    @Query("SELECT COUNT(*) FROM player") //전체 개수 가져오기
    fun getPlayerCount(): Int
}