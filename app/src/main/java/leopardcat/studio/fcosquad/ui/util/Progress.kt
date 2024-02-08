package leopardcat.studio.fcosquad.ui.util

import android.app.Dialog
import android.content.Context

class Progress(context: Context): Dialog(context) {
    companion object {
        fun dialog(context: Context): LoadingDialog {
            return LoadingDialog(context)
        }
    }
}