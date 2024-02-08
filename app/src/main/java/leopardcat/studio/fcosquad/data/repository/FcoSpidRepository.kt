package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.dto.FcoSpid
import leopardcat.studio.fcosquad.data.source.FcoSpidService
import javax.inject.Inject

class FcoSpidRepository @Inject constructor(
    private val fcoSpidService: FcoSpidService,
) {
    suspend fun getFcoSpid(): List<FcoSpid> {
        return fcoSpidService.getFcoSpid()
    }
}