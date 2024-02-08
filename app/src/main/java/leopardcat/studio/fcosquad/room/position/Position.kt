package leopardcat.studio.fcosquad.room.position

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "position")
data class Position(

    @PrimaryKey
    val spposition: Int,

    @ColumnInfo(name = "desc")
    val desc: String
)