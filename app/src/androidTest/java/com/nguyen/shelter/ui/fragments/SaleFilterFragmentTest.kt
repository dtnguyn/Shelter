package com.nguyen.shelter.ui.fragments

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.tabs.TabLayout
import com.nguyen.shelter.R
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.MainFragmentDirections
import com.nguyen.shelter.ui.main.adapters.StateBottomSheetAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Description
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Test

@ExperimentalCoroutinesApi
class SaleFilterFragmentTest {

    private fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with drawable same as drawable with id $id")
        }

        override fun matchesSafely(view: View): Boolean {
            val context = view.context
            val expectedBitmap = context.getDrawable(id)?.toBitmap()

            return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
        }
    }


    fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }

    @Test
    fun test_sortFunction() {

        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
        onView(withId(R.id.filter_button)).perform(click())

        onView(withId(R.id.relevance)).perform(click())
            .check(matches(withDrawable(R.drawable.relevance_selected)))
        onView(withId(R.id.date)).perform(click())
            .check(matches(withDrawable(R.drawable.calendar_selected)))
        onView(withId(R.id.price_min)).perform(click())
            .check(matches(withDrawable(R.drawable.price_min_selected)))
        onView(withId(R.id.price_max)).perform(click())
            .check(matches(withDrawable(R.drawable.price_max_selected)))
        onView(withId(R.id.sqft)).perform(click())
            .check(matches(withDrawable(R.drawable.ruler_selected)))
        onView(withId(R.id.photos)).perform(click())
            .check(matches(withDrawable(R.drawable.photo_selected)))
    }

    @Test
    fun test_pickLocationFunction() {

        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
        onView(withId(R.id.filter_button)).perform(click())

        //Check state picker
        onView(withId(R.id.state_button)).perform(click())
        onView(withId(R.id.map_bottom_sheet_title))
            .check(matches(isDisplayed()))
        onView(withId(R.id.states_recyclerview)).perform(
            RecyclerViewActions.actionOnItemAtPosition<StateBottomSheetAdapter.BottomSheetViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.state_button))
            .check(matches(withText("AL")))


        //Check city edit text
        onView(withId(R.id.city_edit_text))
            .perform(clearText(), typeText("Hello"))
            .check(matches(withText("Hello")))

        //Check zip code edit text
        onView(withId(R.id.zip_code_edit_text))
            .perform(clearText(), typeText("1345"))
            .check(matches(withText("1345")))
    }


    @Test
    fun test_propertyTypeFeature() {

        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
        onView(withId(R.id.filter_button)).perform(click())
        onView(withId(R.id.prop_type_include)).perform(scrollTo())
        onView(withId(R.id.apartment_card_view)).perform(click())
        onView(withId(R.id.any_card_view)).perform(click())
        onView(withId(R.id.any_image_view))
            .check(matches(withDrawable(R.drawable.any_selected)))
        onView(withId(R.id.apartment_image_view))
            .check(matches(withDrawable(R.drawable.apartment)))
        onView(withId(R.id.single_image_view))
            .check(matches(withDrawable(R.drawable.single_family)))
        onView(withId(R.id.multi_image_view))
            .check(matches(withDrawable(R.drawable.multi_family)))
        onView(withId(R.id.condo_image_view))
            .check(matches(withDrawable(R.drawable.condo)))
        onView(withId(R.id.mobile_image_view))
            .check(matches(withDrawable(R.drawable.mobile)))
        onView(withId(R.id.farm_image_view))
            .check(matches(withDrawable(R.drawable.farm)))
        onView(withId(R.id.land_image_view))
            .check(matches(withDrawable(R.drawable.land)))
    }

//    @Test
//    fun test_otherFeatureFunction(){
//        ActivityScenario.launch(MainActivity::class.java)
//        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
//        onView(withId(R.id.filter_button)).perform(click())
//        onView(withId(R.id.others_features_include)).perform(scrollTo())
//
////        onView(withId(R.id.foreclosure_checkbox)).perform(click()).check(matches(isChecked()))
////        onView(withId(R.id.open_house_checkbox)).perform(click()).check(matches(isChecked()))
////        onView(withId(R.id.is_pending_checkbox)).perform(click()).check(matches(isChecked()))
////        onView(withId(R.id.not_yet_built_checkbox)).perform(click()).check(matches(isChecked()))
////        onView(withId(R.id.contingent_checkbox)).perform(click()).check(matches(isChecked()))
////        onView(withId(R.id.new_construction_checkbox)).perform(click()).check(matches(isChecked()))
////
////        onView(withId(R.id.foreclosure_checkbox)).perform(click()).check(matches(isNotChecked()))
////        onView(withId(R.id.open_house_checkbox)).perform(click()).check(matches(isNotChecked()))
////        onView(withId(R.id.is_pending_checkbox)).perform(click()).check(matches(isNotChecked()))
////        onView(withId(R.id.not_yet_built_checkbox)).perform(click()).check(matches(isNotChecked()))
////        onView(withId(R.id.contingent_checkbox)).perform(click()).check(matches(isNotChecked()))
////        onView(withId(R.id.new_construction_checkbox)).perform(click()).check(matches(isNotChecked()))
//    }

    @Test
    fun test_petFeature() {

        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
        onView(withId(R.id.filter_button)).perform(click())
        onView(withId(R.id.pet_include)).perform(scrollTo())

        onView(withId(R.id.dog_card_view)).perform(click())
        onView(withId(R.id.dog_image_view))
            .check(matches(withDrawable(R.drawable.dog)))
        onView(withId(R.id.dog_card_view)).perform(click())
        onView(withId(R.id.dog_image_view))
            .check(matches(withDrawable(R.drawable.dog_selected)))

        onView(withId(R.id.cat_card_view)).perform(click())
        onView(withId(R.id.cat_image_view))
            .check(matches(withDrawable(R.drawable.cat_selected)))
        onView(withId(R.id.cat_card_view)).perform(click())
        onView(withId(R.id.cat_image_view))
            .check(matches(withDrawable(R.drawable.cat)))

    }



    @Test
    fun test_othersFeature() {

        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
        onView(withId(R.id.filter_button)).perform(click())
        onView(withId(R.id.sale_filter_scroll_view))
            .perform(swipeUp())

        onView(withId(R.id.recreation_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.pool_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.outdoor_space_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.garage_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.central_air_checkbox)).perform(click())
            .check(
                matches(isChecked())
            )
        onView(withId(R.id.fireplace_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.spa_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.dishwasher_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.doorman_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.elevator_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.laundry_room_checkbox))
            .perform(click()).check(matches(isChecked()))
        onView(withId(R.id.no_fee_checkbox))
            .perform(click()).check(matches(isChecked()))

        onView(withId(R.id.recreation_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.pool_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.outdoor_space_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.garage_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.central_air_checkbox)).perform(click())
            .check(
                matches(isNotChecked())
            )
        onView(withId(R.id.fireplace_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.spa_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.dishwasher_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.doorman_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.elevator_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.laundry_room_checkbox))
            .perform(click()).check(matches(isNotChecked()))
        onView(withId(R.id.no_fee_checkbox))
            .perform(click()).check(matches(isNotChecked()))

    }

}