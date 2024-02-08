package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.dto.FcoMatch
import leopardcat.studio.fcosquad.data.source.FcoMatchService
import javax.inject.Inject

class FcoMatchRepository @Inject constructor(
    private val fcoMatchService: FcoMatchService,
) {
    suspend fun getFcoMatch(key: String, matchid: String): FcoMatch {
        return fcoMatchService.getFcoMatch(key = key, matchid = matchid)
    }
}