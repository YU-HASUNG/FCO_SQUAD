package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.dto.FcoDivision
import leopardcat.studio.fcosquad.data.source.FcoDivisionService
import javax.inject.Inject

class FcoDivisionRepository @Inject constructor(
    private val fcoDivisionService: FcoDivisionService,
) {
    suspend fun getFcoDivision(): List<FcoDivision> {
        return fcoDivisionService.getDivision()
    }
}