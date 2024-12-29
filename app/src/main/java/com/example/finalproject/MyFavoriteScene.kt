package com.example.finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.adapter.SceneListAdapter
import com.example.finalproject.databinding.FragmentMyFavoriteSceneBinding
import com.example.finalproject.touchHelper.ItemTouchHelperCallback

class MyFavoriteScene : Fragment() {

    private lateinit var binding: FragmentMyFavoriteSceneBinding
    private val viewModel: SceneViewModel by activityViewModels {
        SceneViewModelFactory(
            (activity?.application as SceneListApplication).database.sceneDao()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_my_favorite_scene, container, false)

        // by binding
        binding = FragmentMyFavoriteSceneBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //負責設定recyclerView的排列方式，在這邊設定的是垂直往下排，還有許多樣式可以查詢試看看。
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        //設定分隔線的地方，也可以透過配合其他元件來改變分隔線的樣式，這裡設了一個最基本的橫線將資料區隔開來。
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        val listAdapter = SceneListAdapter {
            // handle item selection
            Toast.makeText(context, it.locationName, Toast.LENGTH_SHORT).show()
            val action =
                MyFavoriteSceneDirections.actionMyFavoriteSceneToAboutTheScene( it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.adapter = listAdapter

        val callback = ItemTouchHelperCallback(listAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        // observe the livedata
        viewModel.allScenes.observe(viewLifecycleOwner) {
                items -> items.let {
                listAdapter.submitList(it)
            }
        }


        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_myFavoriteScene_to_addOneScene)
        }

    }

}