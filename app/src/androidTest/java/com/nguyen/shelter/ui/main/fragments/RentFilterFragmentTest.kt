package com.nguyen.shelter.ui.main.fragments

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nguyen.shelter.R
import com.nguyen.shelter.ui.main.MainActivity

import kotlinx.android.synthetic.main.filter_sort.view.*
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RentFilterFragmentTest {




    fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with drawable same as drawable with id $id")
        }

        override fun matchesSafely(view: View): Boolean {
            val context = view.context
            val expectedBitmap = context.getDrawable(id)?.toBitmap()

            return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
        }
    }

    @Test
    fun testSortFunction() {


        val activityScenario = ActivityScenario.launch(MainActivity::class.java)



        onView(withId(R.id.relevance)).perform(click()).check(matches(withDrawable(R.drawable.relevance_selected)))
        onView(withId(R.id.date)).perform(click()).check(matches(withDrawable(R.drawable.calendar_selected)))
        onView(withId(R.id.price_min)).perform(click()).check(matches(withDrawable(R.drawable.price_min_selected)))
        onView(withId(R.id.price_max)).perform(click()).check(matches(withDrawable(R.drawable.price_max_selected)))
        onView(withId(R.id.sqft)).perform(click()).check(matches(withDrawable(R.drawable.ruler_selected)))
        onView(withId(R.id.photos)).perform(click()).check(matches(withDrawable(R.drawable.photo_selected)))
    }

}