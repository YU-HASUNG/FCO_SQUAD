package leopardcat.studio.fcosquad.di

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leopardcat.studio.fcosquad.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    @Singleton
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setGrade(context: Context, grade: String): Drawable {
        return when(grade){
            "1" -> context.resources.getDrawable(R.drawable.grade_1, null)
            "2" -> context.resources.getDrawable(R.drawable.grade_2, null)
            "3" -> context.resources.getDrawable(R.drawable.grade_3, null)
            "4" -> context.resources.getDrawable(R.drawable.grade_4, null)
            "5" -> context.resources.getDrawable(R.drawable.grade_5, null)
            "6" -> context.resources.getDrawable(R.drawable.grade_6, null)
            "7" -> context.resources.getDrawable(R.drawable.grade_7, null)
            "8" -> context.resources.getDrawable(R.drawable.grade_8, null)
            "9" -> context.resources.getDrawable(R.drawable.grade_9, null)
            "10" -> context.resources.getDrawable(R.drawable.grade_10, null)
            else -> context.resources.getDrawable(R.drawable.grade_1, null)
        }
    }

    @Provides
    @Singleton
    fun setPositionColor(context: Context, position: Int): Int {
        return if(position == 0) {
            context.resources.getColor(R.color.gk, null)
        } else if(position <= 8) {
            context.resources.getColor(R.color.df, null)
        } else if(position <= 19) {
            context.resources.getColor(R.color.mf, null)
        } else if(position <= 27) {
            context.resources.getColor(R.color.fw, null)
        } else {
            context.resources.getColor(R.color.main_gray, null)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateFormat(inputDate: String): String? {
        val inputFormat = SimpleDateFormat("M.d", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM.dd", Locale.getDefault())

        val now = System.currentTimeMillis()

        return try {
            val date: Date? = inputFormat.parse(inputDate)
            val calendar = Calendar.getInstance()

            if (date != null) {
                val currentDate = Date(now)

                val currentCalendar = Calendar.getInstance()
                currentCalendar.time = currentDate

                val inputCalendar = Calendar.getInstance()
                inputCalendar.time = date

                if (inputCalendar[Calendar.MONTH] > currentCalendar[Calendar.MONTH] || (inputCalendar[Calendar.MONTH] == currentCalendar[Calendar.MONTH] && inputCalendar[Calendar.DAY_OF_MONTH] >= currentCalendar[Calendar.DAY_OF_MONTH])) {
                    calendar.add(Calendar.YEAR, -1)
                }
            }

            val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)
            date?.let { year + "." + outputFormat.format(it) }

        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun playerActionImageUrl(spid: String): String{
        return "https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/playersAction/p{spid}.png".replace("{spid}", spid)
    }

    fun playerImageUrl(spid: String): String{
        return "https://fco.dn.nexoncdn.co.kr/live/externalAssets/common/players/p{spid}.png".replace("{spid}", spid)
    }

    fun playerDetailUrl(spid: String, grade: String): String{
        return "https://fconline.nexon.com/DataCenter/PlayerInfo?spid={spid}&n1Strong={grade}".replace("{spid}", spid).replace("{grade}", grade)
    }

    val SPID = "spid"
    val GRADE = "grade"
}