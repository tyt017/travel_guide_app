package com.example.finalproject

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.finalproject.data.Scene
import com.example.finalproject.databinding.FragmentAboutTheSceneBinding
import com.example.finalproject.databinding.FragmentAddOneSceneBinding
import com.example.finalproject.databinding.FragmentMyFavoriteSceneBinding

class AboutTheScene : Fragment() {


    lateinit var binding: FragmentAboutTheSceneBinding
    private lateinit var scene: Scene

    private val viewModel: SceneViewModel by activityViewModels {//在 Fragment 使用 ViewModel 的code
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
        binding = FragmentAboutTheSceneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // handle item selection
        //Toast.makeText(context, , Toast.LENGTH_SHORT).show()



        // get the passing data
        val args: AboutTheSceneArgs by navArgs()
        val id = args.sceneId
        // retrieve the item with the id and observe it
        viewModel.retrieveScene(id).observe(viewLifecycleOwner) {
                selectedItem ->
            scene = selectedItem
            bind(scene)

            binding.showWeather.setOnClickListener {
                val action = AboutTheSceneDirections.actionAboutTheSceneToWeatherFragment(scene.id, scene.sendLocation)
                this.findNavController().navigate(action)
            }
            binding.showMap.setOnClickListener {
                val action = AboutTheSceneDirections.actionAboutTheSceneToMapsFragment()
                this.findNavController().navigate(action)
            }
            binding.delete.setOnClickListener {
                this.viewModel.deleteScene(scene)
                this.findNavController().navigateUp()
            }

        }

    }

    private fun bind(scene: Scene)
    {
        binding.apply {
            titleInDetailed.text = scene.locationName
            brief.text = scene.locationDescription
            imageView.load(Uri.parse(scene.imageUrl))
        }

    }

}