package leopardcat.studio.fcosquad.data.dto

import leopardcat.studio.fcosquad.data.entity.MatchInfo

data class FcoMatch(
    val matchId: String?,
    val matchDate: String?,
    val matchType: Int?,
    val matchInfo: List<MatchInfo>?
)