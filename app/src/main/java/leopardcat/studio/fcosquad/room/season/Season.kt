package leopardcat.studio.fcosquad.room.season

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "season")
data class Season(

    @PrimaryKey
    val seasonId: Int,

    @ColumnInfo(name = "className")
    val className: String,

    @ColumnInfo(name = "seasonImg")
    val seasonImg: String

)