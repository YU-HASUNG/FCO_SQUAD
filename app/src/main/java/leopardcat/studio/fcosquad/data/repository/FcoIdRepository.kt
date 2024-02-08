package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.dto.FcoId
import leopardcat.studio.fcosquad.data.source.FcoIdService
import javax.inject.Inject

class FcoIdRepository @Inject constructor(
    private val fcoIdService: FcoIdService,
) {
    suspend fun getFcoId(key: String, nickname: String): FcoId {
        return fcoIdService.getFcoId(key = key, nickname = nickname)
    }
}