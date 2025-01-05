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
        mockSceneViewModel = mock()
        mockNavController = mock()
        mockOperationStatus = MutableLiveData()
        whenever(mockSceneViewModel.operationStatus).thenReturn(mockOperationStatus)
        whenever(mockSceneViewModel.isEntryValid(any(), any(), any(), any(), any())).thenReturn(true) // 強制pass mockSceneViewModel.isEntryValid
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
}

