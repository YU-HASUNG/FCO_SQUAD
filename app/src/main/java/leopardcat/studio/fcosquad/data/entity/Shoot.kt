package leopardcat.studio.fcosquad.data.entity

data class Shoot(
    val shootTotal: Int,
    val effectiveShootTotal: Int,
    val shootOutScore: Int,
    val goalTotal: Int,
    val goalTotalDisplay: Int,
    val ownGoal: Int,
    val shootHeading: Int,
    val goalHeading: Int,
    val shootFreekick: Int,
    val goalFreekick: Int,
    val shootInPenalty: Int,
    val goalInPenalty: Int,
    val shootOutPenalty: Int,
    val goalOutPenalty: Int,
    val shootPenaltyKick: Int,
    val goalPenaltyKick: Int
)