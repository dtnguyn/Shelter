package com.nguyen.shelter.ui.main.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.databinding.ItemBlogBinding
import com.nguyen.shelter.model.Blog
import java.text.SimpleDateFormat

class UserBlogAdapter(
    private val userBlogs: ArrayList<Blog>,
    private val onBlogLongClick: (Blog) -> Unit,
    private val onLikeClick: (Blog) -> Unit,
    private val onCommentClick: (Blog) -> Unit,
    private val onPhotosClick: (Bundle) -> Unit
): RecyclerView.Adapter<UserBlogAdapter.UserBlogViewHolder>() {

    inner class UserBlogViewHolder(val binding: ItemBlogBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SimpleDateFormat")
        fun bind(blog: Blog){
            binding.blog = blog

            val dateString = SimpleDateFormat("dd MMM yyyy").format(blog.date)
            val timeString = SimpleDateFormat("HH:mm").format(blog.date)

            binding.dateString = dateString
            binding.timeString = timeString

            binding.blogContainer.setOnLongClickListener {

                onBlogLongClick.invoke(blog)

                true
            }

            binding.likeButton.setOnClickListener {
                onLikeClick.invoke(blog)
                binding.blog = blog
            }

            binding.commentButton.setOnClickListener {
                onCommentClick.invoke(blog)
            }

            when {

                blog.photos.size > 1 -> {
                    val adapter = ImageSliderAdapter(blog.photos as ArrayList<Photo>){ bundle ->
                        onPhotosClick.invoke(bundle)
                    }
                    binding.blogImageSlider.setSliderAdapter(adapter)
                    binding.blogImage.visibility = View.INVISIBLE
                    binding.blogImageSlider.visibility = View.VISIBLE
                    binding.multipleIcon.visibility = View.VISIBLE
                }
                blog.photos.size == 1 -> {
                    binding.image = blog.photos[0].url
                    binding.blogImage.visibility = View.VISIBLE
                    binding.blogImageSlider.visibility = View.INVISIBLE
                    binding.multipleIcon.visibility = View.INVISIBLE
                }
                else -> {
                    binding.blogImage.visibility = View.GONE
                    binding.blogImageSlider.visibility = View.GONE
                    binding.multipleIcon.visibility = View.GONE
                }
            }

        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBlogViewHolder {
        return UserBlogViewHolder(ItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserBlogViewHolder, position: Int) {
        holder.bind(userBlogs[position])
    }

    override fun getItemCount(): Int {
        return userBlogs.size
    }

    fun editItem(blog: Blog) {
        var editPosition: Int? = null
        for(i in userBlogs.indices){
            if(userBlogs[i].id == blog.id) {
                editPosition = i
                break
            }
        }
        editPosition?.let {
            userBlogs[it].apply {
                photos = blog.photos
                content = blog.content
            }
            notifyItemChanged(it)
        }
    }

    fun removeItem(id: String){
        var removePosition: Int? = null
        for(i in userBlogs.indices){
            if(userBlogs[i].id == id) {
                removePosition = i
                break
            }

        }
        removePosition?.let {
            userBlogs.removeAt(it)
            notifyItemRemoved(it)
        }
    }

    fun addComment(blogId: String){
        var position: Int? = null
        for(i in 0 until  userBlogs.size){
            if(userBlogs[i].id == blogId) {
                position = i
                break
            }
        }
        position?.let{
            notifyItemChanged(it)
        }
    }

    fun deleteComment(blogId: String){
        var position: Int? = null
        for(i in 0 until  userBlogs.size){
            if(userBlogs[i].id == blogId) {
                position = i
                break
            }
        }
        position?.let{
            notifyItemChanged(it )
        }
    }
}
