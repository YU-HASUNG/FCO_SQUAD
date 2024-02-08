package leopardcat.studio.fcosquad.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.databinding.FragmentSquadBinding
import leopardcat.studio.fcosquad.di.UtilModule
import leopardcat.studio.fcosquad.di.UtilModule.playerActionImageUrl
import leopardcat.studio.fcosquad.di.UtilModule.playerImageUrl
import leopardcat.studio.fcosquad.room.player.PlayerDatabase
import leopardcat.studio.fcosquad.room.position.PositionDatabase
import leopardcat.studio.fcosquad.room.season.SeasonDatabase
import leopardcat.studio.fcosquad.viewmodel.MainViewModel

class SquadFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentSquadBinding? = null

    private var playerDatabase: PlayerDatabase? = null
    private var seasonDatabase: SeasonDatabase? = null
    private var positionDatabase: PositionDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 데이터 바인딩 초기화
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_squad, container, false)
        binding?.fragment = this
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        //DB
        playerDatabase = PlayerDatabase.getDatabase(requireContext())
        seasonDatabase = SeasonDatabase.getDatabase(requireContext())
        positionDatabase = PositionDatabase.getDatabase(requireContext())

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSquadInfo()
    }

    private fun initSquadInfo() {
        lifecycleScope.launch {
            //매치 상세 기록 조회
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fcoMatch.collect { info ->
                    Log.d("", "matchId : $info.matchId")
                    when (info.matchId) {
                        "9999" -> {
                            Toast.makeText(
                                requireContext(),
                                R.string.setting_match_api_none,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        "error" -> {
                            Toast.makeText(
                                requireContext(),
                                R.string.setting_api_fail,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            if (info.matchInfo?.get(0)?.player != null) {
                                val sortedPlayerList = info.matchInfo[0].player.sortedBy { it.spPosition }
                                for((cnt, i) in sortedPlayerList.withIndex()) {
                                    when(i.spPosition) {
                                        0 -> binding?.GK?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage0, binding?.playerPosition0, binding?.playerName0) }
                                        1 -> binding?.SW?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage1, binding?.playerPosition1, binding?.playerName1) }
                                        2 -> binding?.RWB?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage2, binding?.playerPosition2, binding?.playerName2) }
                                        3 -> binding?.RB?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage3, binding?.playerPosition3, binding?.playerName3) }
                                        4 -> binding?.RCB?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage4, binding?.playerPosition4, binding?.playerName4) }
                                        5 -> binding?.CB?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage5, binding?.playerPosition5, binding?.playerName5) }
                                        6 -> binding?.LCB?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage6, binding?.playerPosition6, binding?.playerName6) }
                                        7 -> binding?.LB?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage7, binding?.playerPosition7, binding?.playerName7) }
                                        8 -> binding?.LWB?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage8, binding?.playerPosition8, binding?.playerName8) }
                                        9 -> binding?.RDM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage9, binding?.playerPosition9, binding?.playerName9) }
                                        10 -> binding?.CDM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage10, binding?.playerPosition10, binding?.playerName10) }
                                        11 -> binding?.LDM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage11, binding?.playerPosition11, binding?.playerName11) }
                                        12 -> binding?.RM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage12, binding?.playerPosition12, binding?.playerName12) }
                                        13 -> binding?.RCM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage13, binding?.playerPosition13, binding?.playerName13) }
                                        14 -> binding?.CM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage14, binding?.playerPosition14, binding?.playerName14) }
                                        15 -> binding?.LCM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage15, binding?.playerPosition15, binding?.playerName15) }
                                        16 -> binding?.LM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage16, binding?.playerPosition16, binding?.playerName16) }
                                        17 -> binding?.RAM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage17, binding?.playerPosition17, binding?.playerName17) }
                                        18 -> binding?.CAM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage18, binding?.playerPosition18, binding?.playerName18) }
                                        19 -> binding?.LAM?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage19, binding?.playerPosition19, binding?.playerName19) }
                                        20 -> binding?.RF?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage20, binding?.playerPosition20, binding?.playerName20) }
                                        21 -> binding?.CF?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage21, binding?.playerPosition21, binding?.playerName21) }
                                        22 -> binding?.LF?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage22, binding?.playerPosition22, binding?.playerName22) }
                                        23 -> binding?.RW?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage23, binding?.playerPosition23, binding?.playerName23) }
                                        24 -> binding?.RS?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage24, binding?.playerPosition24, binding?.playerName24) }
                                        25 -> binding?.ST?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage25, binding?.playerPosition25, binding?.playerName25) }
                                        26 -> binding?.LS?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage26, binding?.playerPosition26, binding?.playerName26) }
                                        27 -> binding?.LW?.let { setSquad(sortedPlayerList[cnt].spId.toString(), sortedPlayerList[cnt].spPosition, it, binding?.seasonImage27, binding?.playerPosition27, binding?.playerName27) }
                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun setSquad(spId: String, position: Int, playerImage: ImageView, seasonImage: ImageView?, positionView: TextView?, playerName: TextView?){
        //선수 이미지 설정
        withContext(Dispatchers.Main) {
            Glide.with(requireContext())
                .load(playerActionImageUrl(spId))
                .placeholder(R.drawable.not_found)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        getPlayerImage(spId)
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).into(playerImage)
        }
//        playerImage.setImageResource(R.drawable.not_found) //스크린샷

        //시즌 이미지 설정
        val seasonUrl = seasonDatabase?.seasonDao()?.getSeasonImageUrl(spId.substring(0, 3))
        seasonImage.let { if (it != null) { Glide.with(requireContext()).load(seasonUrl).into(it) } }

        //포지션명 설정
        positionView?.text = positionDatabase?.positionDao()?.getPosition(position.toString())
        positionView?.setTextColor(UtilModule.setPositionColor(requireContext(), position))

        //선수명 설정
        playerName?.text = playerDatabase?.playerDao()?.getPlayerName(spId)
    }

    //일반 이미지 설정
    private fun getPlayerImage(spId: String) {
        val handler = Handler(Looper.getMainLooper())
        lifecycleScope.launch {
            handler.post {
                binding?.GK?.let {
                    Glide.with(requireContext())
                        .load(playerImageUrl(spId.substring(3, 9)))
                        .placeholder(R.drawable.not_found)
                        .error(R.drawable.not_found)
                        .into(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}



