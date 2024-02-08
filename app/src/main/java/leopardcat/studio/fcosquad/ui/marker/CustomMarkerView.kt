package leopardcat.studio.fcosquad.ui.marker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import leopardcat.studio.fcosquad.R

@SuppressLint("ViewConstructor")
class CustomMarkerView(context: Context, layoutResource: Int, private val date: String?, private val value: String, private val markerWidth: Int) : MarkerView(context, layoutResource) {

    private var tvDate: TextView
    private var tvValue: TextView
    private var layoutWidth: Int? = null

    init {
        val customLayout = View.inflate(context, layoutResource, null)
        tvDate = customLayout.findViewById(R.id.date)
        tvValue = customLayout.findViewById(R.id.value)
        tvDate.text = date
        tvValue.text = value
        layoutWidth = markerWidth
        addView(customLayout)
    }

    override fun draw(canvas: Canvas?, posX: Float, posY: Float) {
        super.draw(canvas, posX - layoutWidth!!.toFloat() / 2, 0f)
    }
}