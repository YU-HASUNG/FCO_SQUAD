package leopardcat.studio.fcosquad.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.databinding.FragmentUserBinding
import leopardcat.studio.fcosquad.ui.util.BottomSheetFragment
import leopardcat.studio.fcosquad.ui.adapter.PlayerAdapter
import leopardcat.studio.fcosquad.ui.intro.IntroActivity
import leopardcat.studio.fcosquad.viewmodel.MainViewModel

class UserFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentUserBinding? = null
    private lateinit var playerAdapter: PlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 데이터 바인딩 초기화
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        binding?.fragment = this
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserInfo()
    }

    private fun initUserInfo() {
        lifecycleScope.launch {
            //계정 정보 조회
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fcoUserInfo.collect {
                    when(it.ouid) {
                        "error" -> {
                            Toast.makeText(requireContext(), R.string.setting_api_fail, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            //역대 최고 등급 조회
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fcoMaxDivision.collect {
                    Log.d("", "matchType : $it.matchType")
                    when(it.matchType) {
                        9999 -> {
                            Toast.makeText(requireContext(), R.string.setting_api_fail, Toast.LENGTH_SHORT).show()
                        }
                        0 -> {
                            binding?.profileImage?.background = ContextCompat.getDrawable(requireContext(), R.drawable.user_nor)
                        }
                        else -> {
                            binding?.profileImage?.let { image ->
                                Glide.with(requireContext())
                                    .load(viewModel.getDivisionUrl(it.division.toString()))
                                    .placeholder(R.drawable.user_nor)
                                    .into(image)
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            //매치 상세 기록 조회
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fcoMatch.collect {
                    Log.d("", "matchId : $it.matchId")
                    when(it.matchId) {
                        "9999" -> {
                            Toast.makeText(requireContext(), R.string.setting_match_api_none, Toast.LENGTH_SHORT).show()
                        }
                        "error" -> {
                            Toast.makeText(requireContext(), R.string.setting_api_fail, Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            if(it.matchInfo?.get(0)?.player != null) {
                                playerAdapter = PlayerAdapter(it.matchInfo[0].player)
                                binding?.PlayerListRV?.adapter = playerAdapter
                                binding?.PlayerListRV?.layoutManager = LinearLayoutManager(context)
                            }
                        }
                    }
                }
            }
        }
    }

    //유저 변경
    fun changeUser() {
        val intent = Intent(requireContext(), IntroActivity::class.java)
        intent.putExtra("intent", "changeUser")
        startActivity(intent)
    }

    //데이터 업데이트 기준
    fun dataUpdateBottomSheet() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(parentFragmentManager, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}