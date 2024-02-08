package leopardcat.studio.fcosquad.room.division

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "division")
data class Division(
    @PrimaryKey
    val divisionId: Int,

    @ColumnInfo(name = "divisionName")
    val divisionName: String
)