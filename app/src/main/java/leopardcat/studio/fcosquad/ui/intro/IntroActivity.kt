package leopardcat.studio.fcosquad.ui.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.databinding.ActivityIntroBinding
import leopardcat.studio.fcosquad.ui.main.MainActivity
import leopardcat.studio.fcosquad.viewmodel.IntroViewModel
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private val viewModel: IntroViewModel by viewModels()

    @Inject
    lateinit var perf: SharedPreferences

    @SuppressLint("ClickableViewAccessibility", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isChangeUser = "" //구단주 변경여부
        if(intent.getStringExtra("intent") != null) {
            isChangeUser = "changeUser"
        } else if(perf.getString("ouid", "") != "") {
            //Preference에 ouid 저장되어있으면 Intro 건너뛰기
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }

        binding = DataBindingUtil.setContentView<ActivityIntroBinding?>(this, R.layout.activity_intro).also {
            it.lifecycleOwner = this
            it.activity = this
            it.viewModel = viewModel
        }

        binding.layout.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        //화면 클릭 시 Keyboard Hide
        binding.textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.introText.visibility = GONE
            }
        }

        //입력 완료 클릭 시
        binding.textInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                confirm()
            }
            return@setOnEditorActionListener true
        }

//        binding.inputButton.setOnClickListener {
//            RealtimeStockApp.prefs.setId("id", viewBinding.textInputEditText.text.toString())
//            introVm.getUserInfo()
//        }

        lifecycleScope.launch {
            // FcoId를 관찰하여 변경이 있을 때마다 실행되는 코드
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.focId.collect { focId ->
                    // 변경된 FcoId를 사용하여 원하는 작업을 수행
                    Log.d("", "fcoId : ${focId.ouid}")
                    if(focId.ouid == "error") {
                        Toast.makeText(this@IntroActivity, R.string.intro_fail_alert, Toast.LENGTH_SHORT).show()
                    } else if(focId.ouid != "") {
                        perf.edit().putString("ouid", focId.ouid).apply()
                        val intent = Intent(this@IntroActivity, MainActivity::class.java)
                        setResult(RESULT_OK, intent)
                        intent.putExtra("intent", isChangeUser)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(binding.textInputEditText.windowToken, 0)
    }

    fun confirm() {
        if(binding.textInputEditText.text.trim().toString() == "") {
            Toast.makeText(this, R.string.intro_nickname_alert, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.getFcoId(binding.textInputEditText.text.toString())
        }
    }
}