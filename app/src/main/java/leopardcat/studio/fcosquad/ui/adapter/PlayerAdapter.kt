package leopardcat.studio.fcosquad.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.data.entity.Player
import leopardcat.studio.fcosquad.di.UtilModule
import leopardcat.studio.fcosquad.di.UtilModule.playerActionImageUrl
import leopardcat.studio.fcosquad.di.UtilModule.playerImageUrl
import leopardcat.studio.fcosquad.di.UtilModule.setGrade
import leopardcat.studio.fcosquad.di.UtilModule.setPositionColor
import leopardcat.studio.fcosquad.room.player.PlayerDatabase
import leopardcat.studio.fcosquad.room.position.PositionDatabase
import leopardcat.studio.fcosquad.room.season.SeasonDatabase
import leopardcat.studio.fcosquad.ui.player.PlayerActivity

class PlayerAdapter(private val playerList: List<Player>):
    RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

    private var playerDatabase: PlayerDatabase? = null
    private var seasonDatabase: SeasonDatabase? = null
    private var positionDatabase: PositionDatabase? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        //DB
        playerDatabase = PlayerDatabase.getDatabase(parent.context)
        seasonDatabase = SeasonDatabase.getDatabase(parent.context)
        positionDatabase = PositionDatabase.getDatabase(parent.context)

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //포지션 기준으로 정렬
        val sortedPlayerList = playerList.sortedBy { it.spPosition }
        val currentPlayer = sortedPlayerList[position]
        Log.d("", "position : ${currentPlayer.spPosition}")

        //선수 이미지
        Glide.with(holder.itemView.context)
            .load(playerActionImageUrl(currentPlayer.spId.toString()))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 이미지 로딩 실패 시 처리할 내용을 여기에 작성
                    holder.playerImage.post {
                        Glide.with(holder.itemView.context)
                            .load(playerImageUrl(currentPlayer.spId.toString().substring(3, 9)))
                            .placeholder(R.drawable.not_found)
                            .into(holder.playerImage)
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
                    holder.playerImage.setImageDrawable(resource)
                    return false
                }
            })
            .placeholder(R.drawable.not_found)
            .into(holder.playerImage)

//        holder.playerImage.setImageResource(R.drawable.not_found) //스크린샷

        //시즌 이미지
        val seasonUrl = seasonDatabase?.seasonDao()?.getSeasonImageUrl(currentPlayer.spId.toString().substring(0, 3))
        val seasonBigUrl = seasonUrl?.replace(".png", "_big.png")
        Glide.with(context)
            .load(seasonBigUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    // 이미지 로딩 실패 시 처리할 내용을 여기에 작성
                    holder.seasonImage.post { Glide.with(context).load(seasonUrl).into(holder.seasonImage) }
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    // 이미지 로딩이 완료된 후 처리할 내용을 여기에 작성
                    holder.seasonImage.setImageDrawable(resource)
                    return false
                }
            }).into(holder.seasonImage)

        //강화단계 이미지
        holder.gradeImage.background = setGrade(context, currentPlayer.spGrade.toString())

        //선수 이름
        holder.playerName.text = playerDatabase?.playerDao()?.getPlayerName(currentPlayer.spId.toString())

        //선수 포지션
        holder.playerPosition.text = positionDatabase?.positionDao()?.getPosition(currentPlayer.spPosition.toString())
        holder.playerPosition.setTextColor(setPositionColor(context, currentPlayer.spPosition))

        //선발 여부
        holder.playerStarting.text = if(currentPlayer.spPosition != 28) context.resources.getString(R.string.setting_starting) else context.resources.getString(R.string.setting_bench)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(UtilModule.SPID, currentPlayer.spId.toString())
            intent.putExtra(UtilModule.GRADE, currentPlayer.spGrade.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val playerName : TextView = itemView.findViewById(R.id.playerName)
        val playerImage : ImageView = itemView.findViewById(R.id.playerImage)
        val seasonImage : ImageView = itemView.findViewById(R.id.seasonImage)
        val gradeImage : ImageView = itemView.findViewById(R.id.gradeImage)
        val playerPosition : TextView = itemView.findViewById(R.id.playerPosition)
        val playerStarting : TextView = itemView.findViewById(R.id.playerStarting)
    }
}