package leopardcat.studio.fcosquad.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import leopardcat.studio.fcosquad.data.dto.FcoDivision
import leopardcat.studio.fcosquad.data.dto.FcoMatch
import leopardcat.studio.fcosquad.data.dto.FcoMaxDivision
import leopardcat.studio.fcosquad.data.dto.FcoPosition
import leopardcat.studio.fcosquad.data.dto.FcoSeason
import leopardcat.studio.fcosquad.data.dto.FcoSpid
import leopardcat.studio.fcosquad.data.dto.FcoUserInfo
import leopardcat.studio.fcosquad.data.repository.FcoDivisionRepository
import leopardcat.studio.fcosquad.data.repository.FcoMatchHistoryRepository
import leopardcat.studio.fcosquad.data.repository.FcoMatchRepository
import leopardcat.studio.fcosquad.data.repository.FcoMaxDivisionRepository
import leopardcat.studio.fcosquad.data.repository.FcoPositionRepository
import leopardcat.studio.fcosquad.data.repository.FcoSeasonRepository
import leopardcat.studio.fcosquad.data.repository.FcoSpidRepository
import leopardcat.studio.fcosquad.data.repository.FcoUserInfoRepository
import leopardcat.studio.fcosquad.room.division.Division
import leopardcat.studio.fcosquad.room.division.DivisionDatabase
import leopardcat.studio.fcosquad.room.player.Player
import leopardcat.studio.fcosquad.room.player.PlayerDatabase
import leopardcat.studio.fcosquad.room.position.Position
import leopardcat.studio.fcosquad.room.position.PositionDatabase
import leopardcat.studio.fcosquad.room.season.Season
import leopardcat.studio.fcosquad.room.season.SeasonDatabase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject internal constructor(
    private val fcoSpidRepository: FcoSpidRepository, //spid
    private val fcoSeasonRepository: FcoSeasonRepository, //season
    private val fcoPositionRepository: FcoPositionRepository, //position
    private val fcoDivisionRepository: FcoDivisionRepository, //division
    private val fcoUserInfoRepository: FcoUserInfoRepository, //계정 정보 조회
    private val fcoMaxDivisionRepository: FcoMaxDivisionRepository, //역대 최고 등급 조회
    private val fcoMatchHistoryRepository: FcoMatchHistoryRepository, //유저 매치 기록 조회
    private val fcoMatchRepository: FcoMatchRepository, //매치 상세 기록 조회
    private val apiKey: String
): ViewModel() {

    @Inject
    lateinit var perf: SharedPreferences

    //계정 정보 조회
    private val _fcoUserInfo = MutableStateFlow(FcoUserInfo("", "", 0))
    val fcoUserInfo: StateFlow<FcoUserInfo> = _fcoUserInfo
    //역대 최고 등급 조회
    private val _fcoMaxDivision = MutableStateFlow(FcoMaxDivision(0, 0, ""))
    val fcoMaxDivision: StateFlow<FcoMaxDivision> = _fcoMaxDivision
    //매치 상세 기록 조회
    private val _fcoMatch = MutableStateFlow(FcoMatch(null, null, null,null))
    val fcoMatch: StateFlow<FcoMatch> = _fcoMatch

    fun initFlowUser() {
        //계정 정보 조회
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _fcoUserInfo.value = fcoUserInfoRepository.getFcoUserInfo(apiKey, perf.getString("ouid", "ouid")!!)
            } catch (e: Exception) {
                _fcoUserInfo.value = (FcoUserInfo("error", "", 0))
            }
        }
        //역대 최고 등급 조회
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDivisionList= fcoMaxDivisionRepository.getFcoMaxDivision(apiKey, perf.getString("ouid", "ouid")!!)
                for(i in userDivisionList){
                    if(i.matchType == 50){
                        _fcoMaxDivision.value = (FcoMaxDivision(i.matchType, i.division, i.achievementDate))
                        return@launch
                    }
                }
                _fcoMaxDivision.value = (FcoMaxDivision(0, 0, "0"))
            } catch (e: Exception) {
                _fcoMaxDivision.value = (FcoMaxDivision(9999, 0, "0"))
            }
        }
        //유저 매치 기록 조회
        //매치 상세 기록 조회
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fcoMatchHistory = fcoMatchHistoryRepository.getFcoMatchHistory(apiKey, perf.getString("ouid", "ouid")!!, perf.getInt("matchType", 50), 0, 1)
                Log.d("", "fcoMatchHistory : ${fcoMatchHistory[0]}")
                if(fcoMatchHistory[0].isNotEmpty()) {
                    _fcoMatch.value = fcoMatchRepository.getFcoMatch(apiKey, fcoMatchHistory[0])
                } else {
                    _fcoMatch.value = FcoMatch("9999", "", 0, null)
                }
            } catch (e: Exception) {
                Log.d("", "fcoMatchHistory Exception : $e")
                _fcoMatch.value = FcoMatch("error", "", 0, null)
            }
        }
    }

    //선수목록 다운로드
    fun insertSpid(playerDatabase: PlayerDatabase?) {
        viewModelScope.launch(Dispatchers.IO) {
            val fcoSpid = fcoSpidRepository.getFcoSpid()

            val fcoSpidSize = fcoSpid.size//api spid Size
            val existingSize = playerDatabase?.playerDao()?.getPlayerCount() ?: 0 //DB spid Size

            if (fcoSpidSize > existingSize) {
                Log.d("", "insert spid")
                val playerList = fcoSpid.toPlayerList()
                playerDatabase?.playerDao()?.insertAll(playerList)
                Log.d("", "insert spid complete")
            } else {
                Log.d("", "none spid insert")
            }
        }
    }

    //List<FcoSpid> -> List<Player>
    private fun List<FcoSpid>.toPlayerList(): List<Player> {
        return map { it.toPlayer() }
    }

    //FcoSpid -> Player
    private fun FcoSpid.toPlayer(): Player {
        return Player(id, name)
    }

    //시즌목록 다운로드
    fun insertSeason(seasonDatabase: SeasonDatabase?) {
        viewModelScope.launch(Dispatchers.IO) {
            val fcoSeason = fcoSeasonRepository.getFcoSeason()

            val fcoSeasonSize = fcoSeason.size//api season Size
            val existingSize = seasonDatabase?.seasonDao()?.getSeasonCount() ?: 0 //DB season Size

            if (fcoSeasonSize > existingSize) {
                Log.d("", "insert season")
                val seasonList = fcoSeason.toSeasonList()
                seasonDatabase?.seasonDao()?.insertAll(seasonList)
                Log.d("", "insert season complete")
            } else {
                Log.d("", "none insert season")
            }
        }
    }

    //List<FcoSeason> -> List<Season>
    private fun List<FcoSeason>.toSeasonList(): List<Season> {
        return map { it.toSeason() }
    }

    //FcoSeason -> Player
    private fun FcoSeason.toSeason(): Season {
        return Season(seasonId, className, seasonImg)
    }

    //포지션목록 다운로드
    fun insertPosition(positionDatabase: PositionDatabase?) {
        viewModelScope.launch(Dispatchers.IO) {
            val fcoPosition = fcoPositionRepository.getFcoPosition()

            val fcoPositionSize = fcoPosition.size//api position Size
            val existingSize = positionDatabase?.positionDao()?.getPositionCount() ?: 0 //DB position Size

            if (fcoPositionSize > existingSize) {
                Log.d("", "insert position")
                val positionList = fcoPosition.toPositionList()
                positionDatabase?.positionDao()?.insertAll(positionList)
                Log.d("", "insert position complete")
            } else {
                Log.d("", "none insert position")
            }
        }
    }

    //List<FcoPosition> -> List<Position>
    private fun List<FcoPosition>.toPositionList(): List<Position> {
        return map { it.toPosition() }
    }

    //FcoPosition -> Position
    private fun FcoPosition.toPosition(): Position {
        return Position(spposition, desc)
    }

    //등급식별자 목록 다운로드
    fun insertDivision(divisionDatabase: DivisionDatabase?) {
        viewModelScope.launch(Dispatchers.IO) {
            val fcoDivision = fcoDivisionRepository.getFcoDivision()

            val fcoDivisionSize = fcoDivision.size//api division Size
            val existingSize = divisionDatabase?.divisionDao()?.getDivisionCount() ?: 0 //DB division Size

            if (fcoDivisionSize > existingSize) {
                Log.d("", "insert division")
                val playerList = fcoDivision.toDivisionList()
                divisionDatabase?.divisionDao()?.insertAll(playerList)
                Log.d("", "insert division complete")
            } else {
                Log.d("", "none insert division")
            }
        }
    }

    //List<FcoDivision> -> List<Division>
    private fun List<FcoDivision>.toDivisionList(): List<Division> {
        return map { it.toDivision() }
    }

    //FcoDivision -> Division
    private fun FcoDivision.toDivision(): Division {
        return Division(divisionId, divisionName)
    }

    //등급 URL
    fun getDivisionUrl(division: String): String {
        return when(division){
            "800" -> divisionImageUrl("0")
            "900" -> divisionImageUrl("1")
            "1000" -> divisionImageUrl("2")
            "1100" -> divisionImageUrl("3")
            "1200" -> divisionImageUrl("4")
            "1300" -> divisionImageUrl("5")
            "2000" -> divisionImageUrl("6")
            "2100" -> divisionImageUrl("7")
            "2200" -> divisionImageUrl("8")
            "2300" -> divisionImageUrl("9")
            "2400" -> divisionImageUrl("10")
            "2500" -> divisionImageUrl("11")
            "2600" -> divisionImageUrl("12")
            "2700" -> divisionImageUrl("13")
            "2800" -> divisionImageUrl("14")
            "2900" -> divisionImageUrl("15")
            "3000" -> divisionImageUrl("16")
            "3100" -> divisionImageUrl("17")
            else -> "error"
        }
    }
    private fun divisionImageUrl(num: String): String{
        return "https://ssl.nexon.com/s2/game/fo4/obt/rank/large/update_2009/ico_rank{num}.png".replace("{num}", num)
    }
}