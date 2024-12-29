package com.example.finalproject.weather

import android.net.Uri
import android.os.Bundle
import com.example.finalproject.weather.WeatherFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.finalproject.*
import com.example.finalproject.data.Scene
import com.example.finalproject.databinding.FragmentAboutTheSceneBinding
import com.example.finalproject.databinding.FragmentWeatherBinding


class WeatherFragment : Fragment() {

    lateinit var binding: FragmentWeatherBinding
    private lateinit var scene: Scene
    private lateinit var weatherViewModel: WeatherViewModel

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
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the ViewModel
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        // coding here
        // get the passing data
        val args: WeatherFragmentArgs by navArgs()
        val id = args.sceneId
        val sendLocation = args.sendLocation

        binding.viewModel = weatherViewModel
        binding.lifecycleOwner = this
        Toast.makeText(context, sendLocation, Toast.LENGTH_SHORT).show()
        weatherViewModel.sendRetrofitRequest(sendLocation)

    }
}