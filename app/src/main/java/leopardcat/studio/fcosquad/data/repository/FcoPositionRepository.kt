package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.dto.FcoPosition
import leopardcat.studio.fcosquad.data.source.FcoPositionService
import javax.inject.Inject

class FcoPositionRepository @Inject constructor(
    private val fcoPositionService: FcoPositionService,
) {
    suspend fun getFcoPosition(): List<FcoPosition> {
        return fcoPositionService.getFcoPosition()
    }
}