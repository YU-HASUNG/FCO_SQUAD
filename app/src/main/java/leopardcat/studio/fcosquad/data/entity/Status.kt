package leopardcat.studio.fcosquad.data.entity

data class Status(
    val shoot: Int,
    val effectiveShoot: Int,
    val assist: Int,
    val goal: Int,
    val dribble: Int,
    val intercept: Int,
    val defending: Int,
    val passTry: Int,
    val passSuccess: Int,
    val dribbleTry: Int,
    val dribbleSuccess: Int,
    val ballPossesionTry: Int,
    val ballPossesionSuccess: Int,
    val aerialTry: Int,
    val aerialSuccess: Int,
    val blockTry: Int,
    val block: Int,
    val tackleTry: Int,
    val tackle: Int,
    val yellowCards: Int,
    val redCards: Int,
    val spRating: Double
)