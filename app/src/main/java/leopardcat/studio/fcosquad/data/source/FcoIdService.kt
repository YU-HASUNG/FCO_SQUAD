package leopardcat.studio.fcosquad.data.source

import leopardcat.studio.fcosquad.data.dto.FcoId
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FcoIdService {
    @GET("fconline/v1/id")
    suspend fun getFcoId(
        @Header("x-nxopen-api-key") key: String,
        @Query("nickname") nickname: String
    ): FcoId
}