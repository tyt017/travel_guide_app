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

//    lateinit var binding: FragmentAddOneSceneBinding
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
//    private val viewModel: SceneViewModel by activityViewModels {//在 Fragment 使用 ViewModel 的code
//        SceneViewModelFactory(
//            (activity?.application as SceneListApplication).database.sceneDao()
//        )
//    }

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
//        binding = FragmentAddOneSceneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideSceneViewModel()
        // Get the ViewModel
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        setupImageSelection() // 將原本直接寫在onViewCreated中的image處理程式碼獨立出來，變成一個function
        setupSpinner() // 將原本直接寫在onViewCreated中的spinner處理程式碼獨立出來，變成一個function
        setupSaveAction()
        observeViewModel()
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

    private fun setupSaveAction() {
        binding.saveAction.setOnClickListener {
            addNewScene()
            Toast.makeText(context,"SUCCESSFUL ADDED", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupImageSelection() {
        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imagePath1 = uri
            binding.imagePreview1.apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                load(uri) { size(40, 40) }
            }
        }

        binding.button1Add.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.button1Delete.setOnClickListener {
            imagePath1 = null
            binding.imagePreview1.setImageResource(android.R.drawable.ic_menu_camera)
        }

        binding.imagePreview1.setOnClickListener {
            binding.imageView.load(imagePath1) { size(1000) }
            Toast.makeText(context, "Path=${imagePath1}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            weatherViewModel.cities
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                locationindex = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 可以選擇加入默認邏輯或保持空
            }
        }
    }

    private fun observeViewModel() {
        viewModel.operationStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is OperationStatus.SUCCESS -> {
                    Toast.makeText(context, "Success Added", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }
                is OperationStatus.ERROR -> {
                    Toast.makeText(context, status.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}