package leopardcat.studio.fcosquad.room.season

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SeasonDao {
    @Query("SELECT * FROM season")
    fun getAllSeason(): List<Season>

    @Insert
    fun insertSeason(season: Season)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIfNotExists(season: Season)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(season: List<Season>)

    @Update
    fun updateSeason(season: Season)

    @Delete
    fun deleteSeason(season: Season)

    @Query("SELECT seasonImg FROM season WHERE seasonId = :seasonId")
    fun getSeasonImageUrl(seasonId: String): String?

    @Query("SELECT COUNT(*) FROM season") //전체 개수 가져오기
    fun getSeasonCount(): Int
}