package leopardcat.studio.fcosquad.ui.player

import android.graphics.Color
import android.media.AudioManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.databinding.FragmentChartBinding
import leopardcat.studio.fcosquad.di.UtilModule.convertDateFormat
import leopardcat.studio.fcosquad.ui.marker.CustomMarkerView
import leopardcat.studio.fcosquad.viewmodel.PlayerViewModel
import java.text.NumberFormat

class ChartFragment : Fragment(), OnChartGestureListener, OnChartValueSelectedListener {

    private val viewModel: PlayerViewModel by activityViewModels()
    private var binding: FragmentChartBinding? = null

    private var customMarker: CustomMarkerView? = null
    private var vibrator: Vibrator? = null
    private var audioManager: AudioManager? = null
    private var dataSet: LineDataSet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart, container, false)
        binding?.fragment = this
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vibrator = getSystemService(requireContext(), Vibrator::class.java)
        audioManager = getSystemService(requireContext(), AudioManager::class.java)
        setChart()
    }

    private fun setChart(){
        val entries = ArrayList<Entry>()
        val valueArray = viewModel.getMonthlyValue()

        for (i in 0 until valueArray!!.length()-1) {
            entries.add(Entry(i.toFloat(), (valueArray.getLong(i)).toFloat()))
        }

        dataSet = LineDataSet(entries, "").apply {
            // 심지 부분
            lineWidth = 2F
            setDrawValues(false)
            setDrawCircles(false)
            setDrawHorizontalHighlightIndicator(false)
        }

        // Y 축
        binding?.chart?.axisLeft?.run {
            isEnabled = false
        }

        binding?.chart?.axisRight?.run {
            isEnabled = false
        }

        // X 축
        binding?.chart?.xAxis?.run {
            isEnabled = false
        }

        binding?.chart?.legend?.run {
            isEnabled = false
        }

        binding?.chart?.apply {
            setBackgroundColor(Color.BLACK)
            this.data = LineData(dataSet)
            description.isEnabled = false
            isHighlightPerDragEnabled = true
            requestDisallowInterceptTouchEvent(true)
            setOnChartValueSelectedListener(this@ChartFragment)
            onChartGestureListener = this@ChartFragment
            invalidate()
        }
    }

    override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
        dataSet?.setDrawVerticalHighlightIndicator(true)
    }

    override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
        dataSet?.setDrawVerticalHighlightIndicator(false)
        hideMarker()
    }

    override fun onChartLongPressed(me: MotionEvent?) {
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
    }

    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        //진동 설정
        if(audioManager?.ringerMode != AudioManager.RINGER_MODE_SILENT){
            val customAmplitude = 30
            val vibrationEffect = VibrationEffect.createOneShot(50, customAmplitude)
            vibrator?.vibrate(vibrationEffect)
        }
        //x축 값
        val date = convertDateFormat(viewModel.getMonthlyTime()?.get(e?.x!!.toInt()).toString())
        //y축 값
        val formattedValue = viewModel.getMonthlyValue()?.get(e?.x!!.toInt())
        val value = NumberFormat.getNumberInstance().format(formattedValue.toString().toLong())+"BP"
        //marker width 계산 (16dp)
        val markerWidth = viewModel.calculateDimensions(value, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.toFloat(), resources.displayMetrics))

        customMarker = CustomMarkerView(requireContext(), R.layout.marker_view, date, value, markerWidth)
        customMarker!!.chartView = binding?.chart
        binding?.chart?.marker = customMarker
    }

    override fun onNothingSelected() {
    }

    private fun hideMarker() {
        if(customMarker != null){
            binding?.chart?.marker = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}