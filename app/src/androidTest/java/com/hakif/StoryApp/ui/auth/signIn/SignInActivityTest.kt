package com.hakif.StoryApp.ui.auth.signIn

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.hakif.StoryApp.R
import com.hakif.StoryApp.ui.MainActivity
import com.hakif.StoryApp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SignInActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(SignInActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginLogoutFlow() {
        Intents.init()

        onView(withId(R.id.edt_email)).perform(typeText("haikaliman@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.edt_password)).perform(typeText("Haikal123"), closeSoftKeyboard())
        onView(withId(R.id.btn_signIn)).perform(click())

        Thread.sleep(8000)

        intended(hasComponent(MainActivity::class.java.name))

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_host_fragment_content_main)).check(matches(isDisplayed()))

        val ctx: Context = ApplicationProvider.getApplicationContext()
        openActionBarOverflowOrOptionsMenu(ctx)
        onView(withText(R.string.action_to_logOut)).perform(click())
        onView(withId(R.id.btn_signIn)).check(matches(isDisplayed()))

        Intents.release()
    }

}

