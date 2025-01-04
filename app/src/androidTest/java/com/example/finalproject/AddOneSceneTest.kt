package com.example.finalproject

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import android.text.SpannableStringBuilder
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.*
import okhttp3.internal.tls.OkHostnameVerifier.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class AddOneSceneTest {

    private lateinit var mockSceneViewModel: SceneViewModel
    private lateinit var mockNavController: NavController
    private lateinit var mockOperationStatus: MutableLiveData<OperationStatus>

    @Captor
    private lateinit var captor: ArgumentCaptor<String>

    @Before
    fun setUp() {
//        MockitoAnnotations.openMocks(this)
        mockSceneViewModel = mock()
        mockNavController = mock()
        mockOperationStatus = MutableLiveData()
        whenever(mockSceneViewModel.operationStatus).thenReturn(mockOperationStatus)
        whenever(mockSceneViewModel.isEntryValid(any(), any(), any(), any(), any())).thenReturn(true)
//        whenever(mockSceneViewModel.addNewScene(any(), any(), any(), any(), any())).thenReturn(Unit)
        captor = ArgumentCaptor.forClass(String::class.java)
    }

    @Test
    fun testSaveActionPassesDataToViewModel() {
        val scenario = launchFragmentInContainer<TestAddOneScene>(
            themeResId = R.style.Theme_FinalProject,
            fragmentArgs = bundleOf(), // 如果 constructor 有參數
            factory = object : FragmentFactory() {
                override fun instantiate(
                    classLoader: ClassLoader,
                    className: String
                ): Fragment {
                    return TestAddOneScene(mockSceneViewModel)
                }
            }
        )

        scenario.onFragment { fragment ->
            fragment.requireActivity().runOnUiThread {
                fragment.setNavController(mockNavController)
            }
        }

        // 模擬使用者輸入
        onView(withId(R.id.location_name)).perform(typeText("Valid Location"))
        onView(withId(R.id.location_address)).perform(typeText("Valid Address"))
        onView(withId(R.id.location_description)).perform(typeText("Valid Description"))
        closeSoftKeyboard()
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Taipei"))).perform(click())
        onView(withId(R.id.save_action)).perform(click())


        verify(mockSceneViewModel, times(1)).addNewScene(
            eq("Valid Location"),
            eq("Valid Address"),
            eq("null"),
            eq("Valid Description"),
            eq("Taipei")
        )
//        verifyNoMoreInteractions(mockSceneViewModel)
    }

//    @Test
//    fun testAddOneScene() {
//        // 啟動 AddOneScene Fragment
//        val scenario = launchFragmentInContainer<TestAddOneScene>()
//
//        scenario.onFragment { fragment ->
////            fragment.viewModel = mockSceneViewModel
//            fragment.setViewModel(mockSceneViewModel)
////            val viewModelField = fragment::class.java.getDeclaredField("viewModel")
////            viewModelField.isAccessible = true
////            viewModelField.set(fragment, mockSceneViewModel)
//            // 模擬輸入資料
//            onView(withId(R.id.location_name)).perform(typeText("Valid Location"))
//            onView(withId(R.id.location_address)).perform(typeText("Valid Address"))
//            onView(withId(R.id.location_description)).perform(typeText("Valid Description"))
//            onView(withId(R.id.save_action)).perform(click())
//
//            // 驗證 ViewModel 是否正確調用
//            verify(mockSceneViewModel, times(1)).addNewScene(
//                eq("Valid Location"),
//                eq("Valid Address"),
//                eq("file://image.png"),
//                eq("Valid Description"),
//                eq("City1")
//            )
//        }
//
//        // 驗證畫面資料
//        onView(withId(R.id.location_name)).check(matches(withText("Valid Location")))
//        onView(withId(R.id.location_address)).check(matches(withText("Valid Address")))
//        onView(withId(R.id.location_description)).check(matches(withText("Valid Description")))
//    }

//    @Test
//    fun testAddOneScene() {
//        val scenario = launchFragmentInContainer<TestAddOneScene>(
//            themeResId = R.style.Theme_FinalProject
//        )
//
//        scenario.onFragment { fragment ->
//            // 在 UI Thread 上安全地設置 ViewModel
//            fragment.requireActivity().runOnUiThread {
//                fragment.setViewModel(mockSceneViewModel)
//                fragment.setNavController(mockNavController)
//            }
////            Handler(Looper.getMainLooper()).post {
////                fragment.setViewModel(mockSceneViewModel)
////            }
//        }
//
//        onView(withId(R.id.location_name)).perform(typeText("Valid Location"))
//        onView(withId(R.id.location_address)).perform(typeText("Valid Address"))
//        onView(withId(R.id.location_description)).perform(typeText("Valid Description"))
//        closeSoftKeyboard()
//        onView(withId(R.id.save_action)).perform(click())
//
////        whenever(mockSceneViewModel.addNewScene(
////            eq("Valid Location"),
////            eq("Valid Address"),
////            any(),
////            eq("Valid Description"),
////            any()
////        )).thenReturn(Unit)
//        // 驗證 ViewModel 方法被調用
//        verify(mockSceneViewModel, times(1)).addNewScene("Valid Location", "Valid Address", "file://image.png", "Valid Description", "City1")
////        verify(mockSceneViewModel).addNewScene(
////            captor.capture(), captor.capture(), any(), captor.capture(), any()
////        )
//
////        verify(mockSceneViewModel).addNewScene(
////            any(), any(), any(), any(), any()
////        )
//
////        val capturedArgs = captor.allValues
////        assertEquals("Valid Location", capturedArgs[0])
////        assertEquals("Valid Address", capturedArgs[1])
//////        assertEquals("file://image.png", capturedArgs[2])
////        assertEquals("Valid Description", capturedArgs[3])
//////        assertEquals("City1", capturedArgs[4])
//    }
}

