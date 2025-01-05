package com.example.finalproject

import android.R
import android.net.Uri
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.finalproject.databinding.FragmentAddOneSceneBinding

open class AddOneScene : Fragment() {

    private var _binding: FragmentAddOneSceneBinding? = null
    private val binding get() = _binding!!
    private var imagePath1: Uri? = null

    protected open fun provideSceneViewModel(): SceneViewModel {
        // 在正式執行時，這裡使用原本的工廠或 activityViewModels 的邏輯
        return ViewModelProvider(
            requireActivity(),
            SceneViewModelFactory(
                (requireActivity().application as SceneListApplication).database.sceneDao()
            )
        ).get(SceneViewModel::class.java)
    }

    lateinit var viewModel: SceneViewModel
    private lateinit var weatherViewModel: WeatherViewModel

    private var locationindex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_add_one_scene, container, false)
        // By binding
        val contextThemeWrapper = ContextThemeWrapper(activity, com.example.finalproject.R.style.Theme_FinalProject)
        val themedInflater = inflater.cloneInContext(contextThemeWrapper)
        _binding = FragmentAddOneSceneBinding.inflate(themedInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideSceneViewModel()
        // Coding here
        binding.saveAction.setOnClickListener {
            addNewScene()
            Toast.makeText(context,"SUCCESSFUL ADDED", Toast.LENGTH_SHORT).show()
        }

        val getImage1 =
            registerForActivityResult(ActivityResultContracts.GetContent()) {// handle selection of one image

                uri -> imagePath1 = uri// save the image's path
                binding.imagePreview1.apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    load(uri) {
                        size(40, 40) // in pixels
                    }
                }
            }
        binding.button1Add.setOnClickListener {
            getImage1.launch("image/*")
        }
        binding.button1Delete.setOnClickListener {
            imagePath1 = null
            binding.imagePreview1.setImageResource(android.R.drawable.ic_menu_camera)
            // not R.drawable
        }
        // 按圖片時，把圖片放大 //即便是image 也支持 setOnclick的方法
        binding.imagePreview1.setOnClickListener {
            binding.imageView.load(imagePath1) {
                size(1000)
            }
            Toast.makeText(context, "Path=${imagePath1}", Toast.LENGTH_SHORT).show()

        }

        // Get the ViewModel
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        // configure the spinner
        // set the data source

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, weatherViewModel.cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
                // p0 means parent here
                val selectedPosition = parent?.selectedItemPosition
                locationindex = if (selectedPosition != null) {
                    selectedPosition
                } else {
                    0
                }
                /* selectedPosition.let {
                            WeatherViewModel.sendRetrofitRequest(WeatherViewModel.cities[it!!])
                        }*/
            }
            override fun onNothingSelected(p0: AdapterView<*>) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun addNewScene() {
        // check input
        if (isEntryValid()) {
            viewModel.addNewScene(
                binding.locationName.text.toString(),//retrival
                binding.locationAddress.text.toString(),
                imagePath1.toString(),
                binding.locationDescription.text.toString(), //Integer to String
                weatherViewModel.cities[locationindex]
            )
            // back to listFragment
            findNavController().navigateUp()
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.locationName.text.toString(),
            binding.locationAddress.text.toString(),
            imagePath1.toString(),
            binding.locationDescription.text.toString(),
            weatherViewModel.cities[locationindex]
        )
    }
}