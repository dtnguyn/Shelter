package com.nguyen.shelter.ui.community.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyen.shelter.R
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.databinding.FragmentBlogBinding
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.model.User
import com.nguyen.shelter.ui.community.adapters.BlogAdapter
import com.nguyen.shelter.ui.community.viewmodels.BlogViewModel
import com.nguyen.shelter.ui.community.viewmodels.MainStateEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.filter_sort.*
import java.util.*
import kotlin.collections.HashMap


@AndroidEntryPoint
class BlogFragment : Fragment() {


    private val viewModel: BlogViewModel by viewModels()

    private lateinit var adapter: BlogAdapter
    private lateinit var binding: FragmentBlogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =  FragmentBlogBinding.inflate(inflater, container, false)

        subscribeObservers()

//        viewModel.setStateEvent(MainStateEvent.AddBlog(
//            Blog(
//                id = UUID.randomUUID().toString(),
//                user = User("123", "", "Adron", "test@email.com"),
//                date = Date(),
//                content = "Hi, I just move to this area, hopefully I get to know everyone!\n" +
//                        "\n" +
//                        "I plan on throwing a party next weekend. If anyone wants to join please comment below. ",
//                likeCounter = 2,
//                commentCounter = 0,
//                comments = listOf(),
//                photos = listOf(Photo("test"), Photo("test2"), Photo("test3"), Photo("test4")),
//                likeUsers = hashMapOf("123" to true, "321" to true),
//                postalCode = "44504"
//            )
//        ))



        adapter = BlogAdapter(arrayListOf())

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
        return binding.root
    }


    private fun subscribeObservers(){
        viewModel.blogs.observe(viewLifecycleOwner, {
            adapter.addItems(it)
            val recyclerView = binding.blogRecyclerview
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        })
    }

}