package leopardcat.studio.fcosquad.ui.util

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.databinding.FragmentDataBottomSheetBinding
import leopardcat.studio.fcosquad.viewmodel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var perf: SharedPreferences

    private var binding: FragmentDataBottomSheetBinding? = null
    private var matchType: Int? = null
    private val type1 = 50
    private val type2 = 60
    private val type3 = 30
    private val type4 = 40

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_bottom_sheet, container, false)
        binding?.fragment = this
        binding?.lifecycleOwner = viewLifecycleOwner
        return binding?.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchType = perf.getInt("matchType", type1)
        when(matchType) {
            type1 -> {
                option1Clicked()
            }
            type2 -> {
                option2Clicked()
            }
            type3 -> {
                option3Clicked()
            }
            type4 -> {
                option4Clicked()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun option1Clicked() {
        matchType = type1
        binding?.option1?.background = requireContext().getDrawable(R.drawable.bg_change_coach)
        binding?.option1?.setTextColor(requireContext().getColor(R.color.main_blue))
        binding?.option2?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option2?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option3?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option3?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option4?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option4?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun option2Clicked() {
        matchType = type2
        binding?.option1?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option1?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option2?.background = requireContext().getDrawable(R.drawable.bg_change_coach)
        binding?.option2?.setTextColor(requireContext().getColor(R.color.main_blue))
        binding?.option3?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option3?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option4?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option4?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun option3Clicked() {
        matchType = type3
        binding?.option1?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option1?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option2?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option2?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option3?.background = requireContext().getDrawable(R.drawable.bg_change_coach)
        binding?.option3?.setTextColor(requireContext().getColor(R.color.main_blue))
        binding?.option4?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option4?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun option4Clicked() {
        matchType = type4
        binding?.option1?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option1?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option2?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option2?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option3?.background = requireContext().getDrawable(R.drawable.bg_data_update)
        binding?.option3?.setTextColor(requireContext().getColor(R.color.color_c2c2c2))
        binding?.option4?.background = requireContext().getDrawable(R.drawable.bg_change_coach)
        binding?.option4?.setTextColor(requireContext().getColor(R.color.main_blue))
    }

    @SuppressLint("CommitPrefEdits")
    fun setMatchType() {
        matchType?.let {
            perf.edit().putInt("matchType", it).apply()//matchType 저장
            when(it) {
                type1 -> {
                    Toast.makeText(requireContext(), "공식경기를 기준으로 데이터를 설정합니다", Toast.LENGTH_SHORT).show()
                }
                type2 -> {
                    Toast.makeText(requireContext(), "공식 친선을 기준으로 데이터를 설정합니다", Toast.LENGTH_SHORT).show()
                }
                type3 -> {
                    Toast.makeText(requireContext(), "리그 친선을 기준으로 데이터를 설정합니다", Toast.LENGTH_SHORT).show()
                }
                type4 -> {
                    Toast.makeText(requireContext(), "클래식 1on1을 기준으로 데이터를 설정합니다", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    ""
                }
            }
        }
        viewModel.initFlowUser() //데이터 업데이트
        dismiss()
    }

    fun closeBtn() {
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}