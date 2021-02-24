package com.nguyen.shelter.ui.fragments

import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nguyen.shelter.R
import com.nguyen.shelter.ui.community.adapters.BlogAdapter
import com.nguyen.shelter.ui.community.fragments.BlogFragment
import com.nguyen.shelter.util.FakeData.fakeBlogs
import com.nguyen.shelter.util.FakeData.fakeString
import com.nguyen.shelter.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class BlogFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Test
    fun test_isRecyclerViewShowingItems() {
        launchFragmentInHiltContainer<BlogFragment> {
            viewModel.setBlog(fakeBlogs)
        }
        onView(withId(R.id.blog_recyclerview)).perform(scrollToPosition<BlogAdapter.BlogViewHolder>(fakeBlogs.size - 1))
    }

    @Test
    fun test_isAddingBlogFragmentShowing() {
        launchFragmentInHiltContainer<BlogFragment>()
        onView(withId(R.id.add_post_button)).perform(click())

        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()))
        onView(withId(R.id.add_image_button)).check(matches(isDisplayed()))
        onView(withId(R.id.add_edit_post_text)).check(matches(isDisplayed()))
        onView(withId(R.id.content_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.post_button)).check(matches(isDisplayed()))
    }

    @Test
    fun test_typingOnAddBlogEditText(){
        launchFragmentInHiltContainer<BlogFragment>()
        onView(withId(R.id.add_post_button)).perform(click())
        onView(withId(R.id.content_edit_text)).perform(clearText(), typeText(fakeString)).check(matches(withText(
            fakeString)))
    }


}