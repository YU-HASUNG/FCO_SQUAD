package leopardcat.studio.fcosquad.data.source

import leopardcat.studio.fcosquad.data.dto.FcoPosition
import retrofit2.http.GET

interface FcoPositionService {
    @GET("static/fconline/meta/spposition.json")
    suspend fun getFcoPosition(): List<FcoPosition>
}