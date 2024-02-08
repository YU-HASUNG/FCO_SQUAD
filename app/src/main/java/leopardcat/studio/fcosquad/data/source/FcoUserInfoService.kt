package leopardcat.studio.fcosquad.data.source

import leopardcat.studio.fcosquad.data.dto.FcoUserInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FcoUserInfoService {
    @GET("fconline/v1/user/basic")
    suspend fun getFcoUserInfo(
        @Header("x-nxopen-api-key") key: String,
        @Query("ouid") ouid: String
    ): FcoUserInfo
}