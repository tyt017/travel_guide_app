package com.example.finalproject

import org.junit.Test

import com.example.finalproject.data.Scene
import com.example.finalproject.data.SceneDao
import com.example.finalproject.SceneViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.mockito.Mockito.*

import org.junit.Assert.*
import org.junit.Before
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
//    @Test
//    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//    }

//    private val mockSceneDao: SceneDao = mock(SceneDao::class.java)

    @Test
    fun testIsEntryValid() {
        // 模擬 SceneDao
        val mockSceneList = listOf(
            Scene(1, "Scene1", "Addr1", "Image1", "Desc1", "sendLoc1"),
            Scene(2, "Scene2", "Addr2", "Image2", "Desc2", "sendLoc2")
        )

        `when`(mockSceneDao.getItems()).thenReturn(flowOf(mockSceneList))

        // 傳入模擬的 SceneDao 初始化 ViewModel
        val viewModel = SceneViewModel(mockSceneDao)

        // 測試 isEntryValid 方法
        val result = viewModel.isEntryValid(
            "Name",
            "Addr",
            "ImageUri",
            "Description",
            "City"
        )

        assertTrue(result)
    }

    private lateinit var mockSceneDao: SceneDao
    private lateinit var viewModel: SceneViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher()) // 控制 Dispatcher
        mockSceneDao = mock(SceneDao::class.java)
        whenever(mockSceneDao.getItems()).thenReturn(flowOf(emptyList()))
        viewModel = SceneViewModel(mockSceneDao)
    }

    @Test
    fun testAddNewSceneInViewModel() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        viewModel = SceneViewModel(mockSceneDao)

        viewModel.addNewScene(
            "Valid Location",
            "Valid Address",
            "file://image.png",
            "Valid Description",
            "City"
        )

        testDispatcher.scheduler.advanceUntilIdle() // 等待所有協程完成

        val captor = argumentCaptor<Scene>()
        verify(mockSceneDao).insert(captor.capture())

        val capturedScene = captor.firstValue
        assertEquals("Valid Location", capturedScene.locationName)
        assertEquals("Valid Address", capturedScene.locationAddress)
        assertEquals("file://image.png", capturedScene.imageUrl)
        assertEquals("Valid Description", capturedScene.locationDescription)
        assertEquals("City", capturedScene.sendLocation)
    }

}