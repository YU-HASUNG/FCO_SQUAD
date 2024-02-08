package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.source.FcoMatchHistoryService
import javax.inject.Inject

class FcoMatchHistoryRepository @Inject constructor(
    private val fcoMatchHistoryService: FcoMatchHistoryService,
) {
    suspend fun getFcoMatchHistory(key: String, ouid: String, matchtype: Int, offset: Int, limit: Int): List<String> {
        return fcoMatchHistoryService.getFcoMatchHistory(key = key, ouid = ouid, matchtype = matchtype, offset = offset, limit = limit)
    }
}