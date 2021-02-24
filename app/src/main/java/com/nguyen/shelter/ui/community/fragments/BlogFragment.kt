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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentBlogBinding
import com.nguyen.shelter.model.PhotoUri
import com.nguyen.shelter.ui.community.adapters.AddImageAdapter
import com.nguyen.shelter.ui.community.adapters.BlogAdapter
import com.nguyen.shelter.ui.community.adapters.CommentAdapter
import com.nguyen.shelter.ui.community.viewmodels.BlogViewModel
import com.nguyen.shelter.ui.community.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.fragments.DialogError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class BlogFragment : Fragment() {


    companion object{
        const val PICK_IMAGE_REQUEST = 1
        const val LOCATION_REQUEST_CODE = 1
    }

    val viewModel: BlogViewModel by viewModels()

    private lateinit var blogAdapter: BlogAdapter
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var addImageAdapter: AddImageAdapter

    private lateinit var binding: FragmentBlogBinding
    private lateinit var addBottomSheet: BottomSheetBehavior<ConstraintLayout>
    private lateinit var reportBottomSheet: BottomSheetBehavior<ConstraintLayout>
    private lateinit var commentBottomSheet: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =  FragmentBlogBinding.inflate(inflater, container, false)

        subscribeObservers()


        if(viewModel.postalCode.value == null) {
            println("debug: again: ")
            checkLocationPermission(fun(result: Boolean){
                if(result) getCurrentLocation()
            })
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.setStateEvent(MainStateEvent.GetMessagingToken)
        viewModel.setStateEvent(MainStateEvent.CheckAuthentication)


        blogAdapter = BlogAdapter(
            arrayListOf(),
            viewModel.area.value,
            viewModel.postalCode.value,
            onBlogLongClick = fun(blog){
                viewModel.setStateEvent(MainStateEvent.SetFocusBlog(blog))
                val bottomSheet = BlogActionBottomFragment(blog.isOwner){action ->
                    performAction(action)
                }
                bottomSheet.show(requireActivity().supportFragmentManager, "Action Bottom Sheet")
            },
            onLikeClick = fun(blog){
                viewModel.setStateEvent(MainStateEvent.LikeBlog(blog))
                viewModel.setStateEvent(MainStateEvent.SetFocusBlog(blog))
            },
            onCommentClick = fun(blog){
                commentBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                binding.commentInclude.commentCount = blog.commentCounter
                binding.commentInclude.isLoading = true
                if(this::commentAdapter.isInitialized) commentAdapter.clearComments()
                viewModel.setStateEvent(MainStateEvent.GetComments(blog.id))
                viewModel.setStateEvent(MainStateEvent.SetFocusBlog(blog))
            },
            onPhotosClick = fun(bundle){
                findNavController().navigate(R.id.photo_detail_fragment, bundle)
            }
        )

        viewInit()



        return binding.root
    }

    private fun viewInit(){
        binding.apply {
            addBottomSheet = BottomSheetBehavior.from(binding.addInclude.addPostBottomSheet)
            addPostButton.setOnClickListener {
                addInclude.postButton.text = getString(R.string.post)
                addInclude.contentEditText.setText("")
                addImageAdapter.refreshImages()
                addInclude.addEditPostText.text = getString(R.string.add_post)
                addBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED)
            }

            reportBottomSheet = BottomSheetBehavior.from(binding.reportInclude.reportContainer)
            reportInclude.reportButton.setOnClickListener {
                val reportContent = binding.reportInclude.reportEditText.text.toString()
                if(reportContent.isBlank()) return@setOnClickListener
                viewModel.currentFocusBlog.value?.id?.let {
                    viewModel.setStateEvent(MainStateEvent.ReportBlog(reportContent, it))
                    reportBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            commentBottomSheet = BottomSheetBehavior.from(binding.commentInclude.commentContainer)
            commentInclude.addCommentButton.setOnClickListener {
                val commentContent = commentInclude.commentEditText.text.toString()
                if(commentContent.isBlank()) return@setOnClickListener
                commentInclude.commentEditText.setText("")
                (activity as MainActivity?)?.hideKeyboard()

                viewModel.setStateEvent(MainStateEvent.AddComment(commentContent))
            }

            addInclude.addImageButton.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
            }

            addInclude.postButton.setOnClickListener {
                if(addInclude.postButton.text == getString(R.string.post)){
                    viewModel.setStateEvent(MainStateEvent.AddBlog(addInclude.contentEditText.text.toString()))
                } else {
                    viewModel.setStateEvent(MainStateEvent.EditBlog(addInclude.contentEditText.text.toString()))
                }

                addBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED

                addInclude.contentEditText.setText("")
            }

            val locationListener = View.OnClickListener {
                val dialog = DialogPostalCode(viewModel, requireActivity())
                dialog.show(requireActivity().supportFragmentManager, getString(R.string.postal_code_dialog))
            }
            postalCodeText.setOnClickListener(locationListener)
            locationIc.setOnClickListener(locationListener)
        }
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
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    println("DebugApp: location permission granted")
                    getCurrentLocation()
                }
            }
        }
    }


    //Check location permission
    private fun checkLocationPermission(callback: (result: Boolean) -> Unit) {
        val request = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!request) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
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
                        setPostalCodeAndArea(lat, lng)
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

    private fun setPostalCodeAndArea(lat: Double, lng: Double){
        val geocoder = Geocoder(context, Locale.getDefault())
        try{
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if(addresses.size == 1 && addresses[0].postalCode != null && addresses[0].subAdminArea != null){
                viewModel.setStateEvent(MainStateEvent.SetPostalCode(addresses[0].postalCode))
                viewModel.setStateEvent(MainStateEvent.SetArea(addresses[0].subAdminArea))
            } else {
                println("debug: 2")
                viewModel.setStateEvent(MainStateEvent.SetPostalCode(null))
                viewModel.setStateEvent(MainStateEvent.SetArea(null))
            }
        } catch (e: Exception){
            println("debug: 3")
            viewModel.setStateEvent(MainStateEvent.SetPostalCode(null))
            viewModel.setStateEvent(MainStateEvent.SetArea(null))
        }
    }


    private fun subscribeObservers(){
        println("DebugApp: Test create view")
        viewModel.isLoading.observe(viewLifecycleOwner, {
            binding.isLoading = it
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            val dialogError = DialogError(it, requireActivity())
            dialogError.show(requireActivity().supportFragmentManager, "Dialog Error")
        })

        viewModel.blogs.observe(viewLifecycleOwner, {
            blogAdapter.refresh(it)
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
                addImageAdapter = AddImageAdapter(it as ArrayList<PhotoUri>){position ->
                    viewModel.setStateEvent(MainStateEvent.DeleteImage(position))
                }
                val recyclerView = binding.addInclude.imageRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = addImageAdapter
            }
        })

        viewModel.postalCode.observe(viewLifecycleOwner, {
            if(viewModel.blogs.value?.isNotEmpty() == true) return@observe
            binding.postalCodeText.text = it
            viewModel.setStateEvent(MainStateEvent.GetBlogs)
            blogAdapter.changePostalCode(it)
        })

        viewModel.area.observe(viewLifecycleOwner, {
            blogAdapter.changeArea(it)
        })

        viewModel.currentFocusBlog.observe(viewLifecycleOwner, {blog ->
            binding.addInclude.contentEditText.setText(blog.content)
            binding.reportInclude.blogId = blog.id
        })

        viewModel.addResponse.observe(viewLifecycleOwner, {blog ->
            (activity as MainActivity?)?.hideKeyboard()
            blogAdapter.addItem(blog)
            binding.blogRecyclerview.smoothScrollToPosition(0)
        })

        viewModel.editResponse.observe(viewLifecycleOwner, {blog ->
            blogAdapter.editItem(blog)
        })


        viewModel.deleteResponse.observe(viewLifecycleOwner, {id ->
            blogAdapter.removeItem(id)
        })

        viewModel.removeResponse.observe(viewLifecycleOwner, {id ->
            blogAdapter.removeItem(id)
        })

        viewModel.comments.observe(viewLifecycleOwner, {comments ->
            binding.commentInclude.isLoading = false
            commentAdapter = CommentAdapter(
                comments,
                deleteOnClick = fun(comment){
                    viewModel.setStateEvent(MainStateEvent.DeleteComment(comment.id))
                }
            )
            binding.commentInclude.commentRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            binding.commentInclude.commentRecyclerview.adapter = commentAdapter
        })

        viewModel.addCommentResponse.observe(viewLifecycleOwner, {comment ->
            commentAdapter.addItem(comment)
            blogAdapter.addComment(comment.blogId)
            binding.commentInclude.commentRecyclerview.smoothScrollToPosition(0)
            binding.commentInclude.commentCount = viewModel.currentFocusBlog.value?.commentCounter
        })

        viewModel.deleteCommentResponse.observe(viewLifecycleOwner, { commentId ->
            commentAdapter.deleteItem(commentId)
            viewModel.currentFocusBlog.value?.let {
                blogAdapter.deleteComment(it.id)
            }

        })

    }


    private fun performAction(action: String){

        when(action){
            "edit" -> {
                viewModel.setStateEvent(MainStateEvent.ReplaceAddImages)
                binding.addInclude.postButton.text = getString(R.string.save)
                binding.addInclude.addEditPostText.text = getString(R.string.edit_post)
                addBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED)
            }

            "delete" -> {
                viewModel.setStateEvent(MainStateEvent.DeleteBlog)
            }

            "report" -> {
                reportBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            }

            "remove" -> {
                viewModel.setStateEvent(MainStateEvent.RemoveBlog)
            }
        }

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