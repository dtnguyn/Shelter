package com.nguyen.shelter.ui.main

import android.annotation.SuppressLint
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import com.nguyen.shelter.R
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var drawerLayout: DrawerLayout

    companion object {
        var CURRENT_TAB = "rent"
    }

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("debug: Running Main activity")


        val mainToolBar = main_toolbar
        drawerLayout = drawer_layout
        navController = findNavController(R.id.main_fragment)

        setSupportActionBar(mainToolBar)

        filter_button.setOnClickListener {
            when(CURRENT_TAB){
                "rent" -> findNavController(R.id.main_fragment).navigate(R.id.rent_filter_fragment)
                "sale" -> findNavController(R.id.main_fragment).navigate(R.id.sale_filter_fragment)
            }

        }

        navigation_view.setupWithNavController(navController)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun changeBackgroundToSale(){
        drawerLayout.background = getDrawable(R.drawable.sale_transition)
        val transition = drawerLayout.background as TransitionDrawable
        transition.startTransition(500)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun changeBackgroundToRent(){
        drawerLayout.background = getDrawable(R.drawable.rent_transition)
        val transition = drawerLayout.background as TransitionDrawable
        transition.startTransition(500)
    }

    fun showActionBar(){
        main_toolbar_group.visibility = View.VISIBLE
        supportActionBar?.show()
    }

    fun hideActionBar(){
        main_toolbar_group.visibility = View.GONE
        supportActionBar?.hide()
    }

    fun hideKeyboard(){
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideUserMenuItem(){
        navigation_view.menu.findItem(R.id.user_fragment).isVisible = false
    }

    fun showUserMenuItem(){
        navigation_view.menu.findItem(R.id.user_fragment).isVisible = true
    }

}