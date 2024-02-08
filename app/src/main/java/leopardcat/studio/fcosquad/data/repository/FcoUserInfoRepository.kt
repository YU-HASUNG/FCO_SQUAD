package leopardcat.studio.fcosquad.data.repository

import leopardcat.studio.fcosquad.data.dto.FcoUserInfo
import leopardcat.studio.fcosquad.data.source.FcoUserInfoService
import javax.inject.Inject

class FcoUserInfoRepository @Inject constructor(
    private val fcoUserInfoService: FcoUserInfoService,
) {
    suspend fun getFcoUserInfo(key: String, ouid: String): FcoUserInfo {
        return fcoUserInfoService.getFcoUserInfo(key = key, ouid = ouid)
    }
}