package leopardcat.studio.fcosquad.data.entity

data class MatchInfo(
    val ouid: String,
    val nickname: String,
    val matchDetail: MatchDetail,
    val shoot: Shoot,
    val shootDetail: List<ShootDetail>,
    val pass: Pass,
    val defence: Defence,
    val player: List<Player>
)