package com.nguyen.shelter.ui.community.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.databinding.FragmentBlogBinding
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.model.User
import com.nguyen.shelter.ui.community.adapters.AddImageAdapter
import com.nguyen.shelter.ui.community.adapters.BlogAdapter
import com.nguyen.shelter.ui.community.viewmodels.BlogViewModel
import com.nguyen.shelter.ui.community.viewmodels.BlogViewModel.Companion.NEW_YORK_CITY
import com.nguyen.shelter.ui.community.viewmodels.MainStateEvent
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class BlogFragment : Fragment() {


    companion object{
        const val PICK_IMAGE_REQUEST = 1
        const val LOCATION_REQUEST_CODE = 1
    }

    private val viewModel: BlogViewModel by viewModels()

    private lateinit var blogAdapter: BlogAdapter
    private lateinit var addImageAdapter: AddImageAdapter

    private lateinit var binding: FragmentBlogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =  FragmentBlogBinding.inflate(inflater, container, false)

        subscribeObservers()


        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, fun(result: Boolean){
            if(result) getCurrentLocation()
        })



        blogAdapter = BlogAdapter(arrayListOf(), requireContext())

//        viewModel.setStateEvent(MainStateEvent.EditBlog(Blog(
//            id = UUID.randomUUID().toString(),
//            userId = "123",
//            user = User("123", "", "Adron", "test@email.com"),
//            date = Date(),
//            content = "Hello, I just move to this area, hopefully I get to know everyone!\n" +
//                    "\n" +
//                    "I plan on throwing a party next weekend. If anyone wants to join please comment below. ",
//            likeCounter = 2,
//            commentCounter = 0,
//            comments = listOf(),
//            photos = listOf(Photo("test"), Photo("test2"), Photo("test3"), Photo("test4")),
//            likeUsers = hashMapOf("123" to true, "321" to true),
//            postalCode = "44504"
//        )))
        viewModel.setStateEvent(MainStateEvent.GetBlogs("44504"))
        //viewModel.setStateEvent(MainStateEvent.DeleteBlog("0377d1de-e102-45f1-974a-9de9ab643809"))


        val bottomSheet = BottomSheetBehavior.from(binding.addPostBottomSheet)
        binding.addPostButton.setOnClickListener {
            bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED)
        }

        binding.addImageButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*";
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.action = Intent.ACTION_GET_CONTENT;
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }

        binding.postButton.setOnClickListener {
            viewModel.setStateEvent(MainStateEvent.AddBlog(binding.contentEditText.text.toString()))
            bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }


        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
            }
        }
    }


    //Check location permission
    private fun checkPermission(permission: String, callback: (result: Boolean) -> Unit) {
        val request = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!request) {
            requestPermissions(arrayOf(permission), LOCATION_REQUEST_CODE)
        } else {
            callback.invoke(true)
        }
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        Log.d("Map","Getting current location....")
        val locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        LocationServices.getFusedLocationProviderClient(requireActivity())
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if(locationResult != null && locationResult.locations.isNotEmpty()){
                        val latestIndex = locationResult.locations.size - 1
                        val lat = locationResult.locations[latestIndex].latitude
                        val lng = locationResult.locations[latestIndex].longitude
                        val postalCode = getPostalCode(lat, lng)
                        viewModel.setStateEvent(MainStateEvent.SetPostalCode(postalCode))
                    }
                    super.onLocationResult(locationResult)
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                    locationAvailability?.isLocationAvailable?.let {

                    }
                    super.onLocationAvailability(locationAvailability)
                }
            }, Looper.getMainLooper())
    }

    private fun getPostalCode(lat: Double, lng: Double): String{
        val geocoder = Geocoder(context, Locale.getDefault())
        var postalCode = NEW_YORK_CITY
        return try{
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if(addresses.size == 1){
                postalCode = addresses[0].postalCode
            }
            postalCode
        } catch (e: Exception){
            Log.d("Map", "Error getting address: ${e.message}")
            postalCode
        }
    }



    private fun subscribeObservers(){
        viewModel.blogs.observe(viewLifecycleOwner, {
            blogAdapter.addItems(it)
            val recyclerView = binding.blogRecyclerview
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = blogAdapter
        })

        viewModel.addImages.observe(viewLifecycleOwner, {
            println("debug: add image!")
            if(this::addImageAdapter.isInitialized){
               addImageAdapter.notifyDataSetChanged()
            } else {
                // Initialize the adapter and recyclerview
                addImageAdapter = AddImageAdapter(it as ArrayList<Uri>, requireContext()){position ->
                    viewModel.setStateEvent(MainStateEvent.DeleteImage(position))
                }
                val recyclerView = binding.imageRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = addImageAdapter
            }
        })

        viewModel.postalCode.observe(viewLifecycleOwner, {
            binding.postalCodeText.text = it
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) return


        if(requestCode == PICK_IMAGE_REQUEST ){
            val list =  ArrayList<Uri>()
            if(data?.clipData != null && data.clipData!!.itemCount > 1){
                for(i in 0 until (data.clipData!!.itemCount)){
                    list.add(data.clipData!!.getItemAt(i).uri)
                }
                viewModel.setStateEvent(MainStateEvent.AddImage(list))
            } else if(data?.data != null){
                list.add(data.data!!)
                viewModel.setStateEvent(MainStateEvent.AddImage(list))
            }

        }

    }
}