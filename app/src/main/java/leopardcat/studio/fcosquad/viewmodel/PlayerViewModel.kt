package leopardcat.studio.fcosquad.viewmodel

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import leopardcat.studio.fcosquad.data.dto.PlayerBody
import leopardcat.studio.fcosquad.data.dto.PlayerCharacterValue
import leopardcat.studio.fcosquad.data.dto.PlayerCountry
import leopardcat.studio.fcosquad.data.dto.PlayerInfo
import leopardcat.studio.fcosquad.data.dto.PlayerPrice
import leopardcat.studio.fcosquad.di.UtilModule.playerDetailUrl
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class PlayerViewModel: ViewModel() {

    private var spid: String = ""
    private var grade: String = ""

    private var timeMonthlyArray: JSONArray? = null
    private var valueMonthlyArray: JSONArray? = null

    private var plus = 3 //기본 능력치

    //SPID, GRADE 설정
    fun setSpid(getSpid: String) {
        spid = getSpid
    }
    fun setGrade(getGrade: String) {
        grade = getGrade
        when(grade) {
            "2" -> {
                plus += 1
            }
            "3" -> {
                plus += 2
            }
            "4" -> {
                plus += 4
            }
            "5" -> {
                plus += 6
            }
            "6" -> {
                plus += 8
            }
            "7" -> {
                plus += 11
            }
            "8" -> {
                plus += 15
            }
            "9" -> {
                plus += 19
            }
            "10" -> {
                plus += 24
            }
            else -> {}
        }
    }
    fun getSpid(): String {
        return spid
    }
    fun getGrade(): String {
        return grade
    }

    //가격 정보
    fun getMonthlyTime(): JSONArray? {
        return timeMonthlyArray
    }

    fun getMonthlyValue(): JSONArray? {
        return valueMonthlyArray
    }
    //Marker width 계산하여 return
    fun calculateDimensions(text: String, textSize: Float): Int {
        val paint = Paint()
        paint.textSize = textSize
        return paint.measureText(text).toInt()
    }


    //클럽 경력
    private val _playerClub = MutableStateFlow("")
    val playerClub: StateFlow<String> = _playerClub
    //선수 특성
    private val _playerSkill = MutableStateFlow<PlayerCharacterValue?>(null)
    val playerSkill: StateFlow<PlayerCharacterValue?> = _playerSkill
    //선수 능력치 정보
    private val _playerAbility = MutableStateFlow<PlayerInfo?>(null)
    val playerAbility: StateFlow<PlayerInfo?> = _playerAbility
    //선수 국가 정보
    private val _playerCountry = MutableStateFlow(PlayerCountry("", "", ""))
    val playerCountry: StateFlow<PlayerCountry> = _playerCountry
    //선수 신체 정보
    private val _playerBody = MutableStateFlow(PlayerBody("", "", "", 0, ""))
    val playerBody: StateFlow<PlayerBody> = _playerBody
    //선수 급여 정보
    private val _playerPay = MutableStateFlow("")
    val playerPay: StateFlow<String> = _playerPay
    //선수 가격 정보
    private val _playerValue = MutableStateFlow("")
    val playerValue: StateFlow<String> = _playerValue
    //선수 날짜별 가격 정보
    private val _playerPrice = MutableStateFlow<PlayerPrice?>(null)
    val playerPrice: StateFlow<PlayerPrice?> = _playerPrice

    //선수 정보
    fun getPlayerInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            playerClub() //클럽 경력
            playerSkill() //선수 특성
            playerAbility() //선수 능력치
            playerCountry() //선수 정보
            playerBody() //신체 정보
            playerPay() //급여 정보
        }
    }

    //클럽 경력
    private fun playerClub() {
        try {
            val document: Document = Jsoup.connect(playerDetailUrl(spid, grade)).get()
            val year: Elements = document.select("div[class='td year']")
            val club: Elements = document.select("div[class='td club']")
            if (year.size > 0 && club.size > 0) {
                val json = JSONObject()
                for (i in 0 until year.size) {
                    val yearText = year[i].text()
                    val clubText = club[i].text()
                    json.put(yearText, clubText)
                }
                val flow = json.toString().replace(",","\n").replace("\"", "").replace(":","  ").replace("ClubValue(club={", "").replace("{", "").replace("}", "").replace(")","")
                _playerClub.value = flow
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    //선수 특성
    private fun playerSkill() {
        try {
            val document: Document = Jsoup.connect(playerDetailUrl(spid, grade)).get()
            val skill: Elements = document.select("div.skill_wrap span.desc")

            if (skill.size > 0) {
                val array = arrayOfNulls<String>(skill.size)
                for (i in 0 until skill.size) {
                    val skillText = skill[i].text()
                    array[i] = skillText
                }
                _playerSkill.value = PlayerCharacterValue(array)
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    //선수 능력치
    private fun playerAbility() {
        try {
            val document: Document = Jsoup.connect(playerDetailUrl(spid, grade)).get()
            val ability: Elements = document.select("div[class^='value over']")

            if(ability.size > 0){
                val playerAbility = PlayerInfo(
                    ability[6].text().toInt() + plus,
                    ability[7].text().toInt() + plus,
                    ability[8].text().toInt() + plus,
                    ability[9].text().toInt() + plus,
                    ability[10].text().toInt() + plus,
                    ability[11].text().toInt() + plus,
                    ability[12].text().toInt() + plus,

                    ability[13].text().toInt() + plus,
                    ability[14].text().toInt() + plus,
                    ability[15].text().toInt() + plus,
                    ability[16].text().toInt() + plus,
                    ability[17].text().toInt() + plus,
                    ability[18].text().toInt() + plus,
                    ability[19].text().toInt() + plus,

                    ability[20].text().toInt() + plus,
                    ability[21].text().toInt() + plus,
                    ability[22].text().toInt() + plus,
                    ability[23].text().toInt() + plus,
                    ability[24].text().toInt() + plus,
                    ability[25].text().toInt() + plus,
                    ability[26].text().toInt() + plus,

                    ability[27].text().toInt() + plus,
                    ability[28].text().toInt() + plus,
                    ability[29].text().toInt() + plus,
                    ability[30].text().toInt() + plus,
                    ability[31].text().toInt() + plus,
                    ability[32].text().toInt() + plus,
                    ability[33].text().toInt() + plus,

                    ability[34].text().toInt() + plus,
                    ability[35].text().toInt() + plus,
                    ability[36].text().toInt() + plus,
                    ability[37].text().toInt() + plus,
                    ability[38].text().toInt() + plus,
                    ability[39].text().toInt() + plus
                )
                _playerAbility.value = playerAbility
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    //선수 정보
    private fun playerCountry() {
        try {
            val document: Document = Jsoup.connect(playerDetailUrl(spid, grade)).get()
            val valueInfo: Elements = document.select("div[class='info_line info_ab'] span")
            val countryInfo: Elements = document.select("div[class='etc nation'] span")

            var position = ""
            var stats = ""
            var nation = ""

            if (valueInfo.size >= 3 && countryInfo.size >= 2) {
                position = valueInfo[1].text().toString()
                stats = (Integer.parseInt(valueInfo[2].text().toString()) + plus).toString()
                nation = countryInfo[0].select("img").attr("src").toString()
            }
            _playerCountry.value = PlayerCountry(position, stats, nation)

        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    //신체 정보
    private fun playerBody() {
        try {
            val document: Document = Jsoup.connect(playerDetailUrl(spid, grade)).get()
            val info: Elements = document.select("div[class='info_line info_etc'] span")

            var height = ""
            var weight = ""
            var physical = ""
            var starCnt = 0
            var foot = ""

            if (info.size >= 7) {
                height = info[1].text()
                weight = info[2].text()
                physical = info[3].text()
                starCnt = info[5].text().length
                foot = info[6].text()
            }
            _playerBody.value = PlayerBody(height, weight, physical, starCnt, foot)

        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    //급여 정보
    private fun playerPay() {
        try {
            val document: Document = Jsoup.connect(playerDetailUrl(spid, grade)).get()
            val info: Elements = document.select("div[class='pay_side']")

            var flow = ""
            if (info != null) {
                flow = info.text()
            }
            _playerPay.value = flow
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    //가격 정보
    fun playerPrice(html: String) {
        try {
            val document: Document = Jsoup.parse(html)
            val scriptElements: Elements = document.select("div[class='toggle_content active _area_PlayerPriceGraph'] script")
            val divElements: Elements = document.select("div[class='toggle_content active _area_PlayerPriceGraph'] div")

            var monthlyMaxValue: String? = null
            var monthlyMinValue: String? = null

            //날짜 및 가격
            var jsonObject: JSONObject? = null
            if (scriptElements.size > 1) {
                val jsonScript = scriptElements[1].html()
                val jsonStartIndex = jsonScript.indexOf("{")
                val jsonEndIndex = jsonScript.lastIndexOf("}")
                val jsonString = jsonScript.substring(jsonStartIndex!!, jsonEndIndex!! + 1)
                jsonObject = JSONObject(jsonString)
            }

            timeMonthlyArray = jsonObject?.getJSONArray("time") //날짜
            valueMonthlyArray = jsonObject?.getJSONArray("value") //가격

            //현재가 및 최고,최저가
            if(divElements.size >= 2) {
                val value = divElements.select("li")
                monthlyMaxValue = "당월"+value[0].text().replace("BP", "") //최고가
                monthlyMinValue = "당월"+value[1].text().replace("BP", "") //최저가

                //현재가
                divElements[0].getElementsByTag("strong")
            }

            //날짜별 가격
            _playerPrice.value = PlayerPrice(timeMonthlyArray, valueMonthlyArray, monthlyMaxValue, monthlyMinValue)

            //현재가
            _playerValue.value = divElements[0].getElementsByTag("strong").text().replace(" ", "").replace("BP", "")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //전면 광고
    //광고 상태
    private var mInterstitialAd: InterstitialAd? = null
    private val COVER_AD_ID = ""
    private val COVER_AD_TEST_ID = ""

    fun setCoverAds(context: Context) {
        MobileAds.initialize(context) {
            val adRequest = AdRequest.Builder().build()
            //TODO 광고 ID 설정
            InterstitialAd.load(context,
                COVER_AD_TEST_ID,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        mInterstitialAd = interstitialAd
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        mInterstitialAd = null
                    }
                }
            )
        }
    }
    fun loadCoverAds(activity: Activity) {
        mInterstitialAd?.show(activity)
    }
}