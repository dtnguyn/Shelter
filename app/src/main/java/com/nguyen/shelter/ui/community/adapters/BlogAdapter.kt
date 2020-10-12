package com.nguyen.shelter.ui.community.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.ItemBlogBinding
import com.nguyen.shelter.model.Blog
import java.text.SimpleDateFormat


class BlogAdapter(
    private val blogs: ArrayList<Blog>,
    private val context: Context,
    private val onBlogLongClick: (Blog) -> Unit,
    private val onLikeClick: (Blog) -> Unit,
    private val onCommentClick: (Blog) -> Unit
): RecyclerView.Adapter<BlogAdapter.BaseViewHolder>() {


    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: Any?)
    }

    class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView){
        override fun bind(item: Any?) {
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

            if(item.photos.isNotEmpty() && !item.photos[0].url.isNullOrBlank()){
                binding.image = item.photos[0].url
            } else {
                binding.blogImage.visibility = View.GONE
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
                    LayoutInflater.from(parent.context).inflate(
                        viewType,
                        parent,
                        false
                    )
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
        }

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
}