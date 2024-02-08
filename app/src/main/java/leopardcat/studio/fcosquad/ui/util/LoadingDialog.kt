package leopardcat.studio.fcosquad.ui.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import leopardcat.studio.fcosquad.R

@SuppressLint("InflateParams")
class LoadingDialog(context: Context): Dialog(context) {
    init {
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layout = layoutInflater.inflate(R.layout.activity_progress, null)
        setContentView(layout)
    }
}