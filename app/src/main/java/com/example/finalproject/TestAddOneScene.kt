package com.example.finalproject

import androidx.navigation.NavController
import com.example.finalproject.SceneViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.navigation.Navigation

class TestAddOneScene(private val mock: SceneViewModel) : AddOneScene() {
    private var mockViewModel: SceneViewModel? = null

        override fun provideSceneViewModel(): SceneViewModel {
        // 直接回傳 mock，就不會用到原本父類的真實 ViewModel
        return mock
    }
    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)

        mockViewModel?.let { mock ->
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return mock as T
                }
            }

            // 將自定義 Factory 傳入 ViewModelProvider
            ViewModelProvider(this, factory).get(SceneViewModel::class.java)
        }
    }
    fun setNavController(mockNavController: NavController) {
        try {
            val view = this.view ?: throw IllegalStateException("Fragment view is not yet created")
            Navigation.setViewNavController(view, mockNavController)
        } catch (e: Exception) {
            throw RuntimeException("Failed to set NavController via Navigation.setViewNavController", e)
        }
    }
}