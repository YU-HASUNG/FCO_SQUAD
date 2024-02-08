package leopardcat.studio.fcosquad.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.databinding.ActivityMainBinding
import leopardcat.studio.fcosquad.room.division.DivisionDatabase
import leopardcat.studio.fcosquad.room.player.PlayerDatabase
import leopardcat.studio.fcosquad.room.position.PositionDatabase
import leopardcat.studio.fcosquad.room.season.SeasonDatabase
import leopardcat.studio.fcosquad.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFragmentView(SquadFragment())

        //유저 변경의 경우 database update 하지 않음
        if(intent.getStringExtra("intent") != "changeUser"){
            setDatabase()
        }
        setUserInfo()

        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main).also {
            it.lifecycleOwner = this
            it.activity = this
            it.viewModel = viewModel
        }
    }

    private fun setDatabase() {
        val playerDatabase: PlayerDatabase? = PlayerDatabase.getDatabase(this)
        viewModel.insertSpid(playerDatabase)

        val seasonDatabase: SeasonDatabase? = SeasonDatabase.getDatabase(this)
        viewModel.insertSeason(seasonDatabase)

        val positionDatabase: PositionDatabase? = PositionDatabase.getDatabase(this)
        viewModel.insertPosition(positionDatabase)

        val divisionDatabase: DivisionDatabase? = DivisionDatabase.getDatabase(this)
        viewModel.insertDivision(divisionDatabase)
    }

    private fun setUserInfo() {
        viewModel.initFlowUser()
    }

    //Bottom Tab clicked
    private fun setFragmentView(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
    fun onSearchClick() {
        setFragmentView(SearchFragment())
        binding.search.setImageResource(R.drawable.search_pre)
        binding.squad.setImageResource(R.drawable.squad_nor)
        binding.user.setImageResource(R.drawable.user_nor)
    }
    fun onSquadClick() {
        setFragmentView(SquadFragment())
        binding.search.setImageResource(R.drawable.search_nor)
        binding.squad.setImageResource(R.drawable.squad_pre)
        binding.user.setImageResource(R.drawable.user_nor)
    }
    fun onUserClick() {
        setFragmentView(UserFragment())
        binding.search.setImageResource(R.drawable.search_nor)
        binding.squad.setImageResource(R.drawable.squad_nor)
        binding.user.setImageResource(R.drawable.user_pre)
    }

}