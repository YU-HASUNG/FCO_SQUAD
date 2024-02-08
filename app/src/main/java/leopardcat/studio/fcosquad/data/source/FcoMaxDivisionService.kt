package leopardcat.studio.fcosquad.data.source

import leopardcat.studio.fcosquad.data.dto.FcoMaxDivision
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FcoMaxDivisionService {
    @GET("fconline/v1/user/maxdivision")
    suspend fun getMaxDivision(
        @Header("x-nxopen-api-key") key: String,
        @Query("ouid") ouid: String
    ): List<FcoMaxDivision>
}