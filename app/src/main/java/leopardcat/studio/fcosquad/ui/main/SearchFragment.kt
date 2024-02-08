package leopardcat.studio.fcosquad.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import leopardcat.studio.fcosquad.R
import leopardcat.studio.fcosquad.databinding.FragmentSearchBinding
import leopardcat.studio.fcosquad.room.player.Player
import leopardcat.studio.fcosquad.room.player.PlayerDatabase
import leopardcat.studio.fcosquad.room.season.SeasonDatabase
import leopardcat.studio.fcosquad.ui.adapter.SearchAdapter
import leopardcat.studio.fcosquad.viewmodel.MainViewModel

class SearchFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FragmentSearchBinding? = null

    private var playerDatabase: PlayerDatabase? = null
    private var seasonDatabase: SeasonDatabase? = null

    private lateinit var searchAdapter: SearchAdapter
    private var playerList: List<Player>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 데이터 바인딩 초기화
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding?.fragment = this
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        //DB 초기화
        playerDatabase = PlayerDatabase.getDatabase(requireContext())
        seasonDatabase = SeasonDatabase.getDatabase(requireContext())

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.searchView?.isSubmitButtonEnabled = false
        playerList = playerDatabase?.playerDao()?.getAllPlayer()

        searchAdapter = SearchAdapter(playerDatabase, seasonDatabase, playerList)
        binding?.searchList?.adapter = searchAdapter
        binding?.searchList?.layoutManager = LinearLayoutManager(context)

        setSearchQuery()
    }

    private fun setSearchQuery(){
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            @SuppressLint("SetTextI18n")
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    searchDatabase(query)
                    binding?.searchView?.clearFocus()
                    binding?.searchTitle?.visibility = View.VISIBLE
                    binding?.searchTitle?.text = requireActivity().getString(R.string.search_title) + "(" + searchAdapter.itemCount + ")"
                    if(searchAdapter.itemCount > 0){
                        binding?.searchTitleNone?.visibility = View.GONE
                    } else {
                        binding?.searchTitleNone?.visibility = View.VISIBLE
                    }
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query != null){
                    searchDatabase(query)
                    binding?.searchTitle?.visibility = View.GONE
                }
                return true
            }
        })
    }

    private fun searchDatabase(query: String){
        val searchQuery = "%$query%"
        searchAdapter = SearchAdapter(playerDatabase, seasonDatabase, playerDatabase?.playerDao()?.searchDatabase(searchQuery))
        binding?.searchList?.adapter = searchAdapter
        binding?.searchList?.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}