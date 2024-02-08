package leopardcat.studio.fcosquad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import leopardcat.studio.fcosquad.data.dto.FcoId
import leopardcat.studio.fcosquad.data.repository.FcoIdRepository
import leopardcat.studio.fcosquad.data.repository.FcoUserInfoRepository
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject internal constructor(
    private val fcoIdRepository: FcoIdRepository,
    private val fcoUserInfoRepository: FcoUserInfoRepository,
    private val apiKey: String
): ViewModel() {

    //닉네임
    private val _fcoId = MutableStateFlow(FcoId(""))
    val focId: StateFlow<FcoId> = _fcoId
//    var id =  ""
//    private val _fcoUserInfo = MutableStateFlow(FcoUserInfo("", "", 0))
//    val fcoUserInfo: StateFlow<FcoUserInfo> = _fcoUserInfo
//    var id =  ""

    fun getFcoId(nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                id = nickname
                _fcoId.value = fcoIdRepository.getFcoId(apiKey, nickname)
//                val ouid = fcoIdRepository.getFcoId(apiKey, nickname)
//                _fcoUserInfo.value = fcoUserInfoRepository.getFcoUserInfo(apiKey, ouid.ouid)

            } catch (e: Exception) {
                _fcoId.value = FcoId("error")
//                _fcoUserInfo.value = FcoUserInfo("error", "", 0)
            }
        }
    }
}