package leopardcat.studio.fcosquad.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.di.UtilModule.GRADE
import leopardcat.studio.fcosquad.di.UtilModule.SPID
import leopardcat.studio.fcosquad.di.UtilModule.playerActionImageUrl
import leopardcat.studio.fcosquad.di.UtilModule.playerImageUrl
import leopardcat.studio.fcosquad.room.player.Player
import leopardcat.studio.fcosquad.room.player.PlayerDatabase
import leopardcat.studio.fcosquad.room.season.SeasonDatabase
import leopardcat.studio.fcosquad.ui.player.PlayerActivity


class SearchAdapter(
    private val playerDatabase: PlayerDatabase?,
    private val seasonDatabase: SeasonDatabase?,
    private val searchList: List<Player>?
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = searchList?.get(position)

        //선수 이미지
        Glide.with(holder.itemView.context)
            .load(playerActionImageUrl(currentItem?.spid.toString()))
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
                            .load(playerImageUrl(currentItem?.spid.toString().substring(3, 9)))
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

//        holder.playerImage.setImageDrawable(context.getDrawable(R.drawable.not_found)) //스크린샷

        //시즌 이미지
        Glide.with(holder.itemView.context).load(seasonDatabase?.seasonDao()?.getSeasonImageUrl(currentItem?.spid.toString().substring(0, 3))).into(holder.seasonImage)

        //선수명
        holder.playerName.text = playerDatabase?.playerDao()?.getPlayerName(currentItem?.spid.toString())

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(SPID, currentItem?.spid.toString())
            intent.putExtra(GRADE, "1")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return searchList?.size!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val playerName : MaterialTextView = itemView.findViewById(R.id.playerName)
        val playerImage : ShapeableImageView = itemView.findViewById(R.id.playerImage)
        val seasonImage : ShapeableImageView = itemView.findViewById(R.id.seasonImage)
    }
}