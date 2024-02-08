package leopardcat.studio.fcosquad.room.position

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PositionDao {

    @Insert
    fun insertPosition(position: Position)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIfNotExists(position: Position)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(position: List<Position>)

    @Update
    fun updatePosition(position: Position)

    @Delete
    fun deletePosition(position: Position)

    @Query("SELECT `desc` FROM position WHERE spposition = :spposition")
    fun getPosition(spposition: String): String?

    @Query("SELECT COUNT(*) FROM position") //전체 개수 가져오기
    fun getPositionCount(): Int
}