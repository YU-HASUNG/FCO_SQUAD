package leopardcat.studio.fcosquad.data.entity

data class ShootDetail(
    val goalTime: Int,
    val x: Double,
    val y: Double,
    val type: Int,
    val result: Int,
    val spId: Int,
    val spGrade: Int,
    val spLevel: Int,
    val spIdType: Boolean,
    val assist: Boolean,
    val assistSpId: Int,
    val assistX: Double,
    val assistY: Double,
    val hitPost: Boolean,
    val inPenalty: Boolean
)