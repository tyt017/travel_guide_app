package com.example.finalproject

import android.widget.AdapterView
import androidx.lifecycle.*
import com.example.finalproject.data.Scene
import com.example.finalproject.data.SceneDao
import kotlinx.coroutines.launch

@Mockable
class SceneViewModel(private val sceneDao: SceneDao): ViewModel() {

    // declare an LiveData to hold all items in the database
    val allScenes: LiveData<List<Scene>> = sceneDao.getItems().asLiveData()
    val operationStatus = MutableLiveData<OperationStatus>()

    /**
     * Inserts the new Scene into database.
     */
    fun addNewScene(
        thisLocationName: String,
        thisLocationAddress: String,
        thisImageUrl: String,
        thisLocationDescription: String,
        sendLocation: String
    ) {
        val newScene = getNewSceneEntry(thisLocationName, thisLocationAddress, thisImageUrl, thisLocationDescription, sendLocation)
        insertItem(newScene)
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(scene: Scene) {
        viewModelScope.launch {
            // Coroutine?? Scope
            sceneDao.insert(scene)//這個insert是呼叫 Dao
        }
    }

    /**
     * Returns an instance of the [Scene] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Scene database.
     */
    private fun getNewSceneEntry(thisLocationName: String,
                                 thisLocationAddress: String,
                                 thisImageUrl: String,
                                 thisLocationDescription: String,
                                 thisSendLocation: String): Scene {
        return Scene(
            locationName = thisLocationName,
            locationAddress = thisLocationAddress,
            imageUrl = thisImageUrl,
            locationDescription = thisLocationDescription,
            sendLocation = thisSendLocation
            //等號左邊是 data class Scene 裏頭的參數
            //等號右邊是這個 function傳入的變數
        )

    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(thisLocationName: String,
                     thisLocationAddress: String,
                     thisImageId: String,
                     thisLocationDescription: String,
                     thisSendLocation: String): Boolean {
        if (thisLocationName.isBlank()
            || thisLocationAddress.isBlank()
            || thisImageId.isBlank()
            || thisLocationDescription.isBlank()
            || thisSendLocation.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Retrieve one item and return it as LiveData
     */
    fun retrieveScene(id: Int): LiveData<Scene> {
        return sceneDao.getItem(id).asLiveData()
    }

    /**
     * Delete one item
     */
    fun deleteScene(scene: Scene) {
        viewModelScope.launch {
            sceneDao.delete(scene)
        }
    }
}

sealed class OperationStatus {
    object SUCCESS : OperationStatus()
    data class ERROR(val message: String) : OperationStatus()
}



/**
 * Factory class to instantiate the [ViewModel] instance.
 */
// ViewModel 的 Factory
class SceneViewModelFactory(private val sceneDao: SceneDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SceneViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SceneViewModel(sceneDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



