package leopardcat.studio.fcosquad.ui.player

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.databinding.ActivityPlayerBinding
import leopardcat.studio.fcosquad.di.UtilModule.GRADE
import leopardcat.studio.fcosquad.di.UtilModule.SPID
import leopardcat.studio.fcosquad.di.UtilModule.playerActionImageUrl
import leopardcat.studio.fcosquad.di.UtilModule.playerDetailUrl
import leopardcat.studio.fcosquad.di.UtilModule.playerImageUrl
import leopardcat.studio.fcosquad.di.UtilModule.setGrade
import leopardcat.studio.fcosquad.room.player.PlayerDatabase
import leopardcat.studio.fcosquad.room.season.SeasonDatabase
import leopardcat.studio.fcosquad.ui.util.LoadingDialog
import leopardcat.studio.fcosquad.ui.util.Progress
import leopardcat.studio.fcosquad.viewmodel.PlayerViewModel

@AndroidEntryPoint
class PlayerActivity: AppCompatActivity() {

    private val viewModel: PlayerViewModel by viewModels()
    private var binding: ActivityPlayerBinding? = null

    private var playerDatabase: PlayerDatabase? = null
    private var seasonDatabase: SeasonDatabase? = null

    private val loadingDialog: LoadingDialog by lazy { Progress.dialog(this) }
    private var plus: Int = 3 //스텟 더하기 값

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        binding = DataBindingUtil.setContentView<ActivityPlayerBinding?>(this, R.layout.activity_player).also {
            it.lifecycleOwner = this
            it.activity = this
            it.viewModel = viewModel
        }

        //DB 초기화
        playerDatabase = PlayerDatabase.getDatabase(this)
        seasonDatabase = SeasonDatabase.getDatabase(this)

        init()
    }

    private fun init(){
        loadingDialog.show()

        viewModel.setCoverAds(this) //광고 설정
        setFragment() //탭 설정
        setData() //spid, grade 설정
        setWebView() //웹뷰 설정
        setPlayerStats() //선수 정보 설정
        initObserve() //선수 정보 Observe
    }

    //탭 설정
    private fun setFragment(){
        //기본탭 설정
        //radio button 하단선 색상 설정
        binding?.radioViewOne?.setImageResource(R.color.transparent)
        binding?.radioViewThree?.setImageResource(R.drawable.bg_radio_button)
        //radio button text 굵기 설정
        binding?.chart?.typeface = Typeface.DEFAULT
        binding?.info?.typeface = Typeface.DEFAULT_BOLD

        //탭 클릭 설정
        binding?.radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.chart -> {
                    setFragmentView(ChartFragment())
                    //radio button 하단선 색상 설정
                    binding?.radioViewOne?.setImageResource(R.drawable.bg_radio_button)
                    binding?.radioViewThree?.setImageResource(R.color.transparent)
                    //radio button text 굵기 설정
                    binding?.chart?.typeface = Typeface.DEFAULT_BOLD
                    binding?.info?.typeface = Typeface.DEFAULT
                }
                R.id.info -> {
                    setFragmentView(InfoFragment())
                    //radio button 하단선 색상 설정
                    binding?.radioViewOne?.setImageResource(R.color.transparent)
                    binding?.radioViewThree?.setImageResource(R.drawable.bg_radio_button)
                    //radio button text 굵기 설정
                    binding?.chart?.typeface = Typeface.DEFAULT
                    binding?.info?.typeface = Typeface.DEFAULT_BOLD
                }
            }
        }
    }

    private fun setFragmentView(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }


    //spid, grade 설정
    private fun setData(){
        if(intent.hasExtra(SPID)){
            viewModel.setSpid(intent.getStringExtra(SPID).toString())
        } else {
            Toast.makeText(this, "정보가 정상적으로 전달되지 않았습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            finish()
        }
        if(intent.hasExtra(GRADE)){
            viewModel.setGrade(intent.getStringExtra(GRADE).toString())
        } else {
            Toast.makeText(this, "정보가 정상적으로 전달되지 않았습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    //웹뷰 설정
    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        val webView = binding?.webView
        webView?.settings?.javaScriptEnabled = true

        // 자바스크립트 인터페이스로 연결
        webView?.addJavascriptInterface(object : Any() {
            @JavascriptInterface
            fun getHtml(html: String) {
                viewModel.playerPrice(html)
                setFragmentView(InfoFragment())
                loadingDialog.dismiss()
            }
        }, "Android")

        // 페이지가 모두 로드되었을 때, 작업 정의
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                // 자바스크립트 인터페이스로 연결되어 있는 getHTML를 실행
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);")
            }
        }

        // 지정한 URL을 웹 뷰로 접근하기
        webView?.loadUrl(playerDetailUrl(viewModel.getSpid(), viewModel.getGrade()))
    }

    private fun setPlayerStats(){
        //데이터 로딩
        viewModel.getPlayerInfo()

        //선수 이미지
        binding?.playerImage?.let {
            Glide.with(this@PlayerActivity)
                .load(playerActionImageUrl(viewModel.getSpid()))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // 이미지 로딩 실패 시 처리할 내용을 여기에 작성
                        it.post {
                            Glide.with(this@PlayerActivity)
                                .load(playerImageUrl(viewModel.getSpid().substring(3, 9)))
                                .placeholder(R.drawable.not_found)
                                .into(it)
                        }
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // 이미지 로딩이 완료된 후 처리할 내용을 여기에 작성
                        binding?.playerImage?.setImageDrawable(resource)
                        return false
                    }
                })
                .placeholder(R.drawable.not_found)
                .into(it)
        }

//        binding?.playerImage?.setImageResource(R.drawable.not_found) //스크린샷

        //시즌 이미지
        val seasonUrl = seasonDatabase?.seasonDao()?.getSeasonImageUrl(viewModel.getSpid().substring(0, 3))
        binding?.playerSeasonImage?.let { Glide.with(this@PlayerActivity).load(seasonUrl).into(it) }

        //강화단계 이미지
        binding?.playerGrade?.background = setGrade(this@PlayerActivity, viewModel.getGrade())

        //선수명
        binding?.playerName?.text = playerDatabase?.playerDao()?.getPlayerName(viewModel.getSpid())
    }

    private fun initObserve() {
        //국기 이미지
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerCountry.collect {info ->
                    //국기 이미지
                    if(info.nation != "") {
                        binding?.playerNationImage?.let {
                            Glide.with(this@PlayerActivity).load(info.nation).into(it)
                        }
                    }
                }
            }
        }
    }

    //뒤로가기
    fun finishActivity() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.loadCoverAds(this)
        binding = null
    }
}