package com.nguyen.shelter.ui.community.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.ItemBlogBinding
import com.nguyen.shelter.model.Blog

class BlogAdapter(private val blogs: ArrayList<Blog>): RecyclerView.Adapter<BlogAdapter.BaseViewHolder>() {


    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: Any?)
    }

    class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView){
        override fun bind(item: Any?) {
            TODO("Not yet implemented")
        }

    }

    class BlogViewHolder(private val binding: ItemBlogBinding) : BaseViewHolder(binding.root){
        override fun bind(item: Any?) {
            binding.blog = item as Blog
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
                HeaderViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }

            R.layout.item_blog -> {
                BlogViewHolder(ItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

            else -> {
                BlogViewHolder(ItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if(position != 0){
            holder.bind(blogs[position - 1])
        }

    }

    override fun getItemCount(): Int = blogs.size + 1

    fun addItems(list: List<Blog>){
        for(blog in list){
            blogs.add(blog)
            notifyItemInserted(blogs.size - 1)
        }

    }

}