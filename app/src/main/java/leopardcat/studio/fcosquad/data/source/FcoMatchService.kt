package leopardcat.studio.fcosquad.data.source

import leopardcat.studio.fcosquad.data.dto.FcoMatch
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FcoMatchService {
    @GET("fconline/v1/match-detail")
    suspend fun getFcoMatch(
        @Header("x-nxopen-api-key") key: String,
        @Query("matchid") matchid: String
    ): FcoMatch
}