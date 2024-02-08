package leopardcat.studio.fcosquad.room.division

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DivisionDao {
    @Query("SELECT * FROM division")
    fun getAllDivision(): List<Division>

    @Insert
    fun insertDivision(division: Division)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIfNotExists(division: Division)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(divisions: List<Division>)
    @Update
    fun updateDivision(division: Division)

    @Delete
    fun deleteDivision(division: Division)

    @Query("SELECT * FROM division WHERE divisionId LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): List<Division>

    @Query("SELECT COUNT(*) FROM division") //전체 개수 가져오기
    fun getDivisionCount(): Int
}