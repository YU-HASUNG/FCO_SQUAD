package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.dto.FcoId
import leopardcat.studio.fcosquad.data.dto.FcoMaxDivision
import leopardcat.studio.fcosquad.data.source.FcoIdService
import leopardcat.studio.fcosquad.data.source.FcoMaxDivisionService
import javax.inject.Inject

class FcoMaxDivisionRepository @Inject constructor(
    private val fcoMaxDivisionService: FcoMaxDivisionService,
) {
    suspend fun getFcoMaxDivision(key: String, ouid: String): List<FcoMaxDivision> {
        return fcoMaxDivisionService.getMaxDivision(key = key, ouid = ouid)
    }
}