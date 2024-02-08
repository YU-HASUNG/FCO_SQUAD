package leopardcat.studio.fcosquad.data.source

import leopardcat.studio.fcosquad.data.dto.FcoSeason
import retrofit2.http.GET

interface FcoSeasonService {
    @GET("static/fconline/meta/seasonid.json")
    suspend fun getFcoSeason(): List<FcoSeason>
}