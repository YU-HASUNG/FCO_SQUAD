package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.dto.FcoSeason
import leopardcat.studio.fcosquad.data.source.FcoSeasonService
import javax.inject.Inject

class FcoSeasonRepository @Inject constructor(
    private val fcoSeasonService: FcoSeasonService,
) {
    suspend fun getFcoSeason(): List<FcoSeason> {
        return fcoSeasonService.getFcoSeason()
    }
}