package leopardcat.studio.fcosquad.data.source

import leopardcat.studio.fcosquad.data.dto.FcoDivision
import retrofit2.http.GET

interface FcoDivisionService {
    @GET("static/fconline/meta/division.json")
    suspend fun getDivision(): List<FcoDivision>
}