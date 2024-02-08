package leopardcat.studio.fcosquad.data.source

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FcoMatchHistoryService {
    @GET("fconline/v1/user/match")
    suspend fun getFcoMatchHistory(
        @Header("x-nxopen-api-key") key: String,
        @Query("ouid") ouid: String,
        @Query("matchtype") matchtype: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<String>
}