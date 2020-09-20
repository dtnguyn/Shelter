package com.nguyen.shelter.ui.main.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.databinding.ItemPhotoViewPagerBinding

class PhotoDetailAdapter(private val photos: List<Photo>, private val context: Context): RecyclerView.Adapter<PhotoDetailAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemPhotoViewPagerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String?){
            println("debug: photos url $url")
            //binding.url = url
            Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.detailPhoto)


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ItemPhotoViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(photos[position].url)
    }

    override fun getItemCount(): Int {
        println("debug: photos size ${photos.size}")
        return photos.size
    }


}