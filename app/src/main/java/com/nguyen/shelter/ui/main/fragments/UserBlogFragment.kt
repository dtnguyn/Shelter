package com.nguyen.shelter.ui.main.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.FragmentUserBinding
import com.nguyen.shelter.databinding.FragmentUserBlogBinding
import com.nguyen.shelter.model.PhotoUri
import com.nguyen.shelter.ui.community.adapters.AddImageAdapter
import com.nguyen.shelter.ui.community.adapters.BlogAdapter
import com.nguyen.shelter.ui.community.adapters.CommentAdapter
import com.nguyen.shelter.ui.community.fragments.BlogActionBottomFragment
import com.nguyen.shelter.ui.community.fragments.BlogFragment
import com.nguyen.shelter.ui.community.fragments.DialogPostalCode
import com.nguyen.shelter.ui.community.viewmodels.BlogViewModel
import com.nguyen.shelter.ui.community.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.adapters.UserBlogAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_blog.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserBlogFragment : Fragment() {

    private val viewModel: BlogViewModel by viewModels()
    private lateinit var binding: FragmentUserBlogBinding
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var addImageAdapter: AddImageAdapter

    private lateinit var blogAdapter: UserBlogAdapter
    private lateinit var addBottomSheet: BottomSheetBehavior<ConstraintLayout>
    private lateinit var reportBottomSheet: BottomSheetBehavior<ConstraintLayout>
    private lateinit var commentBottomSheet: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBlogBinding.inflate(inflater, container, false)

        viewInit()

        subscribeObservers()


        viewModel.setStateEvent(MainStateEvent.GetUserBlogs)


        return binding.root
    }

    private fun subscribeObservers(){
        viewModel.blogs.observe(viewLifecycleOwner, {blogs ->
            blogAdapter = UserBlogAdapter(
                userBlogs = blogs,
                onBlogLongClick = fun(blog){
                    viewModel.setStateEvent(MainStateEvent.SetFocusBlog(blog))
                    val bottomSheet = BlogActionBottomFragment(blog.isOwner){action ->
                        performAction(action)
                    }
                    bottomSheet.show(requireActivity().supportFragmentManager, "Action Bottom Sheet")
                },
                onPhotosClick = fun(bundle){
                    findNavController().navigate(R.id.photo_detail_fragment, bundle)
                },
                onCommentClick = fun(blog){
                    commentBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.commentInclude.commentCount = blog.commentCounter
                    binding.commentInclude.isLoading = true
                    if(this::commentAdapter.isInitialized) commentAdapter.clearComments()
                    viewModel.setStateEvent(MainStateEvent.GetComments(blog.id))
                    viewModel.setStateEvent(MainStateEvent.SetFocusBlog(blog))
                },
                onLikeClick = fun(blog){
                    viewModel.setStateEvent(MainStateEvent.LikeBlog(blog))
                    viewModel.setStateEvent(MainStateEvent.SetFocusBlog(blog))
                }
            )
            val recyclerView = binding.userBlogRecyclerview
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = blogAdapter
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            val dialogError = DialogError(it, requireActivity())
            dialogError.show(requireActivity().supportFragmentManager, "Dialog Error")
        })


        viewModel.addImages.observe(viewLifecycleOwner, {
            println("debug: add image!")
            if(this::addImageAdapter.isInitialized){
                addImageAdapter.notifyDataSetChanged()
            } else {
                // Initialize the adapter and recyclerview
                addImageAdapter = AddImageAdapter(it as ArrayList<PhotoUri>){ position ->
                    viewModel.setStateEvent(MainStateEvent.DeleteImage(position))
                }
                val recyclerView = binding.addInclude.imageRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                recyclerView.adapter = addImageAdapter
            }
        })


        viewModel.currentFocusBlog.observe(viewLifecycleOwner, {blog ->
            binding.addInclude.contentEditText.setText(blog.content)
            binding.reportInclude.blogId = blog.id
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

    private fun viewInit(){
//        binding.apply {
//            addBottomSheet = BottomSheetBehavior.from(binding.addInclude.addPostBottomSheet)
//
//            reportBottomSheet = BottomSheetBehavior.from(binding.reportInclude.reportContainer)
//            reportInclude.reportButton.setOnClickListener {
//                val reportContent = binding.reportInclude.reportEditText.text.toString()
//                if(reportContent.isBlank()) return@setOnClickListener
//                viewModel.currentFocusBlog.value?.id?.let {
//                    viewModel.setStateEvent(MainStateEvent.ReportBlog(reportContent, it))
//                    reportBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
//                }
//
//            }
//
//            commentBottomSheet = BottomSheetBehavior.from(binding.commentInclude.commentContainer)
//            commentInclude.addCommentButton.setOnClickListener {
//                val commentContent = commentInclude.commentEditText.text.toString()
//                if(commentContent.isBlank()) return@setOnClickListener
//                commentInclude.commentEditText.setText("")
//                (activity as MainActivity?)?.hideKeyboard()
//
//                viewModel.setStateEvent(MainStateEvent.AddComment(commentContent))
//            }
//
//            addInclude.addImageButton.setOnClickListener {
//                val intent = Intent()
//                intent.type = "image/*"
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                intent.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
//                    BlogFragment.PICK_IMAGE_REQUEST
//                )
//            }
//
//            addInclude.postButton.setOnClickListener {
//                if(addInclude.postButton.text == getString(R.string.post)){
//                    viewModel.setStateEvent(MainStateEvent.AddBlog(addInclude.contentEditText.text.toString()))
//                } else {
//                    viewModel.setStateEvent(MainStateEvent.EditBlog(addInclude.contentEditText.text.toString()))
//                }
//
//                addBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
//
//                addInclude.contentEditText.setText("")
//            }
//        }
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


        if(requestCode == BlogFragment.PICK_IMAGE_REQUEST){
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