package leopardcat.studio.fcosquad.room.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player(
    @PrimaryKey
    val spid: Long,

    @ColumnInfo(name = "name")
    val playerName: String
)