package com.nguyen.shelter.ui.community.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.R
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.databinding.BlogCollapseAreaBinding
import com.nguyen.shelter.databinding.ItemBlogBinding
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.ui.community.fragments.BlogFragment
import com.nguyen.shelter.ui.community.fragments.DialogPostalCode
import com.nguyen.shelter.ui.main.adapters.ImageSliderAdapter
import java.text.SimpleDateFormat


class BlogAdapter(
    private val blogs: ArrayList<Blog>,
    private var area: String?,
    private var postalCode: String?,
    private val onBlogLongClick: (Blog) -> Unit,
    private val onLikeClick: (Blog) -> Unit,
    private val onCommentClick: (Blog) -> Unit,
    private val onPhotosClick: (Bundle) -> Unit
): RecyclerView.Adapter<BlogAdapter.BaseViewHolder>() {


    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: Any?)
    }

     inner class HeaderViewHolder(private val binding: BlogCollapseAreaBinding) : BaseViewHolder(binding.root){
        override fun bind(item: Any?) {

            println("debug: header: $area $postalCode")

            when {
                area != null -> {
                    binding.area = area
                }
                postalCode != null -> {
                    binding.area = "area $postalCode"
                }
                else -> {
                    binding.area = "you"
                }
            }
        }

    }

    inner class BlogViewHolder(private val binding: ItemBlogBinding) : BaseViewHolder(binding.root){
        @SuppressLint("SimpleDateFormat", "UseCompatLoadingForDrawables")
        override fun bind(item: Any?) {
            binding.blog = item as Blog

            val dateString = SimpleDateFormat("dd MMM yyyy").format(item.date)
            val timeString = SimpleDateFormat("HH:mm").format(item.date)

            binding.dateString = dateString
            binding.timeString = timeString

            binding.blogContainer.setOnLongClickListener {

                onBlogLongClick.invoke(item)

                true
            }

            binding.likeButton.setOnClickListener {
                onLikeClick.invoke(item)
                binding.blog = item
            }

            binding.commentButton.setOnClickListener {
                onCommentClick.invoke(item)
            }

            when {

                item.photos.size > 1 -> {
                    val adapter = ImageSliderAdapter(item.photos as ArrayList<Photo>){bundle ->
                        onPhotosClick.invoke(bundle)
                    }
                    binding.blogImageSlider.setSliderAdapter(adapter)
                    binding.blogImage.visibility = View.INVISIBLE
                    binding.blogImageSlider.visibility = View.VISIBLE
                    binding.multipleIcon.visibility = View.VISIBLE
                }
                item.photos.size == 1 -> {
                    binding.image = item.photos[0].url
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

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) {
            R.layout.blog_collapse_area
        } else {
            R.layout.item_blog
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            R.layout.blog_collapse_area -> {
                HeaderViewHolder(
                    BlogCollapseAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            R.layout.item_blog -> {
                BlogViewHolder(
                    ItemBlogBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                BlogViewHolder(
                    ItemBlogBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if(position != 0){
            holder.bind(blogs[position - 1])
        } else holder.bind(null)

    }

    override fun getItemCount(): Int = blogs.size + 1

    fun refresh(list: List<Blog>){
        blogs.clear()

        for(blog in list){
            blogs.add(blog)
        }

        notifyDataSetChanged()
    }

    fun addItem(blog: Blog) {
        blogs.add(0, blog)
        notifyItemInserted(1)
    }

    fun editItem(blog: Blog) {
        var editPosition: Int? = null
        for(i in 0 until  blogs.size){
            if(blogs[i].id == blog.id) {
                editPosition = i
                break
            }
        }
        editPosition?.let {
            blogs[it].apply {
                photos = blog.photos
                content = blog.content
            }
            notifyItemChanged(it + 1)
        }
    }

    fun removeItem(id: String){
        var removePosition: Int? = null
        for(i in 0 until  blogs.size){
            if(blogs[i].id == id) {
                removePosition = i
                break
            }

        }
        removePosition?.let {
            blogs.removeAt(it)
            notifyItemRemoved(it + 1)
        }
    }

    fun addComment(blogId: String){
        var position: Int? = null
        for(i in 0 until  blogs.size){
            if(blogs[i].id == blogId) {
                position = i
                break
            }
        }
        position?.let{
            notifyItemChanged(it + 1)
        }
    }

    fun deleteComment(blogId: String){
        var position: Int? = null
        for(i in 0 until  blogs.size){
            if(blogs[i].id == blogId) {
                position = i
                break
            }
        }
        position?.let{
            notifyItemChanged(it + 1)
        }
    }

    fun changeArea(newArea: String?) {
        area = newArea
        notifyItemChanged(0)
    }

    fun changePostalCode(newPostalCode: String?) {
        postalCode = newPostalCode
        notifyItemChanged(0)
    }
}