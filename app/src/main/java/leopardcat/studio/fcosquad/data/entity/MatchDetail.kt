package leopardcat.studio.fcosquad.data.entity

data class MatchDetail(
    val seasonId: Any?,
    val matchResult: String,
    val matchEndType: Int,
    val systemPause: Int,
    val foul: Int,
    val injury: Int,
    val redCards: Int,
    val yellowCards: Int,
    val dribble: Int,
    val cornerKick: Int,
    val possession: Int,
    val offsideCount: Int,
    val averageRating: Double,
    val controller: String
)