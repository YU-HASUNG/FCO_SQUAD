package leopardcat.studio.fcosquad.ui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.data.dto.PlayerGridItem
import leopardcat.studio.fcosquad.databinding.FragmentInfoBinding
import leopardcat.studio.fcosquad.ui.adapter.PlayerGridAdapter
import leopardcat.studio.fcosquad.viewmodel.PlayerViewModel

class InfoFragment() : Fragment() {

    private val viewModel: PlayerViewModel by activityViewModels()
    private var binding: FragmentInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding?.fragment = this
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserve() //선수 정보 Observe
    }

    private fun setObserve() {

        //선수 특성
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerSkill.collect { info ->
                    //선수 특성
                    if (info != null) {
                        val recyclerView: RecyclerView? = binding?.character
                        val flexboxLayoutManager = GridLayoutManager(context, 2)
                        recyclerView?.layoutManager = flexboxLayoutManager

                        val items: List<PlayerGridItem>? =
                            info.character?.map { PlayerGridItem(it!!) }

                        val gridAdapter = items?.let { PlayerGridAdapter(it) }
                        recyclerView?.adapter = gridAdapter
                    }
                }
            }
        }

        //선수 개인기
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerBody.collect { info ->
                    //선수 특성
                    if (info.starCnt != 0) {
                        when (info.starCnt) {
                            1 -> {
                                binding?.starOne?.setImageResource(R.drawable.star_pre)
                                binding?.starTwo?.setImageResource(R.drawable.star_nor)
                                binding?.starThree?.setImageResource(R.drawable.star_nor)
                                binding?.starFour?.setImageResource(R.drawable.star_nor)
                                binding?.starFive?.setImageResource(R.drawable.star_nor)
                                binding?.starSix?.setImageResource(R.drawable.star_nor)
                            }

                            2 -> {
                                binding?.starOne?.setImageResource(R.drawable.star_pre)
                                binding?.starTwo?.setImageResource(R.drawable.star_pre)
                                binding?.starThree?.setImageResource(R.drawable.star_nor)
                                binding?.starFour?.setImageResource(R.drawable.star_nor)
                                binding?.starFive?.setImageResource(R.drawable.star_nor)
                                binding?.starSix?.setImageResource(R.drawable.star_nor)
                            }

                            3 -> {
                                binding?.starOne?.setImageResource(R.drawable.star_pre)
                                binding?.starTwo?.setImageResource(R.drawable.star_pre)
                                binding?.starThree?.setImageResource(R.drawable.star_pre)
                                binding?.starFour?.setImageResource(R.drawable.star_nor)
                                binding?.starFive?.setImageResource(R.drawable.star_nor)
                                binding?.starSix?.setImageResource(R.drawable.star_nor)
                            }

                            4 -> {
                                binding?.starOne?.setImageResource(R.drawable.star_pre)
                                binding?.starTwo?.setImageResource(R.drawable.star_pre)
                                binding?.starThree?.setImageResource(R.drawable.star_pre)
                                binding?.starFour?.setImageResource(R.drawable.star_pre)
                                binding?.starFive?.setImageResource(R.drawable.star_nor)
                                binding?.starSix?.setImageResource(R.drawable.star_nor)
                            }

                            5 -> {
                                binding?.starOne?.setImageResource(R.drawable.star_pre)
                                binding?.starTwo?.setImageResource(R.drawable.star_pre)
                                binding?.starThree?.setImageResource(R.drawable.star_pre)
                                binding?.starFour?.setImageResource(R.drawable.star_pre)
                                binding?.starFive?.setImageResource(R.drawable.star_pre)
                                binding?.starSix?.setImageResource(R.drawable.star_nor)
                            }

                            6 -> {
                                binding?.starOne?.setImageResource(R.drawable.star_pre)
                                binding?.starTwo?.setImageResource(R.drawable.star_pre)
                                binding?.starThree?.setImageResource(R.drawable.star_pre)
                                binding?.starFour?.setImageResource(R.drawable.star_pre)
                                binding?.starFive?.setImageResource(R.drawable.star_pre)
                                binding?.starSix?.setImageResource(R.drawable.star_pre)
                            }
                        }
                    }
                }
            }
        }

        //능력치
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerAbility.collect { info ->
                    //속력
                    binding?.palyerInfo?.speedValue?.text = info?.speed.toString()
                    binding?.palyerInfo?.speedValue?.let { info?.speed?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //가속력
                    binding?.palyerInfo?.accelerationValue?.text = info?.acceleration.toString()
                    binding?.palyerInfo?.accelerationValue?.let { info?.acceleration?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //골 결정력
                    binding?.palyerInfo?.goalDeterminateValue?.text = info?.goalDeterminate.toString()
                    binding?.palyerInfo?.goalDeterminateValue?.let { info?.goalDeterminate?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //슛파워
                    binding?.palyerInfo?.shootingPowerValue?.text = info?.shootingPower.toString()
                    binding?.palyerInfo?.shootingPowerValue?.let { info?.shootingPower?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //중거리 슛
                    binding?.palyerInfo?.longShootValue?.text = info?.longShoot.toString()
                    binding?.palyerInfo?.longShootValue?.let { info?.longShoot?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //위치 선정
                    binding?.palyerInfo?.positioningValue?.text = info?.positioning.toString()
                    binding?.palyerInfo?.positioningValue?.let { info?.positioning?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //발리 슛
                    binding?.palyerInfo?.volleyValue?.text = info?.volley.toString()
                    binding?.palyerInfo?.volleyValue?.let { info?.volley?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //패널티 킥
                    binding?.palyerInfo?.penaltyValue?.text = info?.penalty.toString()
                    binding?.palyerInfo?.penaltyValue?.let { info?.penalty?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //짧은 패스
                    binding?.palyerInfo?.shortPassValue?.text = info?.shortPass.toString()
                    binding?.palyerInfo?.shortPassValue?.let { info?.shortPass?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //시야
                    binding?.palyerInfo?.eyesightValue?.text = info?.eyesight.toString()
                    binding?.palyerInfo?.eyesightValue?.let { info?.eyesight?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //크로스
                    binding?.palyerInfo?.crossValue?.text = info?.cross.toString()
                    binding?.palyerInfo?.crossValue?.let { info?.cross?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //긴 패스
                    binding?.palyerInfo?.longPassValue?.text = info?.longPass.toString()
                    binding?.palyerInfo?.longPassValue?.let { info?.longPass?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //프리킥
                    binding?.palyerInfo?.freeKickValue?.text = info?.freeKick.toString()
                    binding?.palyerInfo?.freeKickValue?.let { info?.freeKick?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //커브
                    binding?.palyerInfo?.curveValue?.text = info?.curve.toString()
                    binding?.palyerInfo?.curveValue?.let { info?.curve?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //드리블
                    binding?.palyerInfo?.dribbleValue?.text = info?.dribble.toString()
                    binding?.palyerInfo?.dribbleValue?.let { info?.dribble?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //볼 컨트롤
                    binding?.palyerInfo?.ballControlValue?.text = info?.ballControl.toString()
                    binding?.palyerInfo?.ballControlValue?.let { info?.ballControl?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //민첩성
                    binding?.palyerInfo?.agilityValue?.text = info?.agility.toString()
                    binding?.palyerInfo?.agilityValue?.let { info?.agility?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //밸런스
                    binding?.palyerInfo?.balanceValue?.text = info?.balance.toString()
                    binding?.palyerInfo?.balanceValue?.let { info?.balance?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //반응 속도
                    binding?.palyerInfo?.reactionRateValue?.text = info?.reactionRate.toString()
                    binding?.palyerInfo?.reactionRateValue?.let { info?.reactionRate?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //대인 수비
                    binding?.palyerInfo?.personnelDefenseValue?.text = info?.personnelDefense.toString()
                    binding?.palyerInfo?.personnelDefenseValue?.let { info?.personnelDefense?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //태클
                    binding?.palyerInfo?.tackleValue?.text = info?.tackle.toString()
                    binding?.palyerInfo?.tackleValue?.let { info?.tackle?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //가로채기
                    binding?.palyerInfo?.interceptionValue?.text = info?.interception.toString()
                    binding?.palyerInfo?.interceptionValue?.let { info?.interception?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //헤더
                    binding?.palyerInfo?.headerValue?.text = info?.header.toString()
                    binding?.palyerInfo?.headerValue?.let { info?.header?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //슬라이딩 태클
                    binding?.palyerInfo?.slidingTackleValue?.text = info?.slidingTackle.toString()
                    binding?.palyerInfo?.slidingTackleValue?.let { info?.slidingTackle?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //몸싸움
                    binding?.palyerInfo?.struggleValue?.text = info?.struggle.toString()
                    binding?.palyerInfo?.struggleValue?.let { info?.struggle?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //스태미너
                    binding?.palyerInfo?.staminaValue?.text = info?.stamina.toString()
                    binding?.palyerInfo?.staminaValue?.let { info?.stamina?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //적극성
                    binding?.palyerInfo?.positivenessValue?.text = info?.positiveness.toString()
                    binding?.palyerInfo?.positivenessValue?.let { info?.positiveness?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //점프
                    binding?.palyerInfo?.jumpValue?.text = info?.jump.toString()
                    binding?.palyerInfo?.jumpValue?.let { info?.jump?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //침착성
                    binding?.palyerInfo?.composureValue?.text = info?.composure.toString()
                    binding?.palyerInfo?.composureValue?.let { info?.composure?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //GK 다이빙
                    binding?.palyerInfo?.GKdivingValue?.text = info?.GKdiving.toString()
                    binding?.palyerInfo?.GKdivingValue?.let { info?.GKdiving?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //GK 핸들링
                    binding?.palyerInfo?.GKhandlingValue?.text = info?.GKhandling.toString()
                    binding?.palyerInfo?.GKhandlingValue?.let { info?.GKhandling?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //GK 킥
                    binding?.palyerInfo?.GKkickValue?.text = info?.GKkick.toString()
                    binding?.palyerInfo?.GKkickValue?.let { info?.GKkick?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //GK 반응속도
                    binding?.palyerInfo?.GKreactionRateValue?.text = info?.GKreactionRate.toString()
                    binding?.palyerInfo?.GKreactionRateValue?.let { info?.GKreactionRate?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }

                    //GK 위치선정
                    binding?.palyerInfo?.GKpositioningValue?.text = info?.GKpositioning.toString()
                    binding?.palyerInfo?.GKpositioningValue?.let { info?.GKpositioning?.let { it1 ->
                        setTextColor(it,
                            it1
                        )
                    } }
                }
            }
        }
    }

    private fun setTextColor(textView: TextView, num: Int) {
        when(num/10) {
            0,1,2,3,4,5 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_50))
            }
            6 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_60))
            }
            7 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_70))
            }
            8 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_80))
            }
            9 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_90))
            }
            10 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_100))
            }
            11 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_110))
            }
            12 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_120))
            }
            13 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_130))
            }
            14 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_140))
            }
            15 -> {
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_150))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}