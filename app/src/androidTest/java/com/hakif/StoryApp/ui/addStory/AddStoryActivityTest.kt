package com.hakif.StoryApp.ui.addStory

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.hakif.StoryApp.R
import com.hakif.StoryApp.ui.MainActivity
import com.hakif.StoryApp.ui.auth.signIn.SignInActivity
import com.hakif.StoryApp.utils.EspressoIdlingResource
import org.hamcrest.Matchers.anyOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AddStoryActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(SignInActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        Intents.init()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        Intents.release()
    }

    private fun loginToAppAndOpenAddStory() {
        onView(withId(R.id.edt_email)).perform(typeText("haikaliman@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.edt_password)).perform(typeText("Haikal123"), closeSoftKeyboard())
        onView(withId(R.id.btn_signIn)).perform(click())

        Thread.sleep(8000)

        intended(hasComponent(MainActivity::class.java.name))

        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.edt_description)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_gallery)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddStoryFlow() {
        loginToAppAndOpenAddStory()

        val pickers = anyOf(
            hasAction("android.provider.action.PICK_IMAGES"),
            hasAction(Intent.ACTION_GET_CONTENT),
            hasAction(Intent.ACTION_OPEN_DOCUMENT)
        )
        val imageUri: Uri = Uri.parse("android.resource://com.hakif.StoryApp/drawable/app_story_logo")
        val resultData = Intent().apply { data = imageUri }
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(pickers).respondWith(result)

        onView(withId(R.id.edt_description))
            .perform(typeText("Test story description"), closeSoftKeyboard())
        onView(withId(R.id.btn_gallery)).perform(click())
        onView(withId(R.id.iv_preview)).check(matches(isDisplayed()))

        onView(withId(R.id.btn_post)).perform(click())

        Thread.sleep(3000)

        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
    }

}
