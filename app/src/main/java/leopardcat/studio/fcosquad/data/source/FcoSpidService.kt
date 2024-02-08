package leopardcat.studio.fcosquad.data.source

import leopardcat.studio.fcosquad.data.dto.FcoSpid
import retrofit2.http.GET

interface FcoSpidService {
    @GET("static/fconline/meta/spid.json")
    suspend fun getFcoSpid(): List<FcoSpid>
}