package com.nguyen.shelter.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.databinding.ItemImageSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.zip.Inflater

class ImageSliderAdapter(private val images: ArrayList<Photo>): SliderViewAdapter<ImageSliderAdapter.ImageViewHolder>() {

    class ImageViewHolder(private val binding: ItemImageSliderBinding) : SliderViewAdapter.ViewHolder(binding.root) {

        fun bind(imageUrl: String){
            binding.imageUrl = imageUrl
        }
    }

    fun addImages(imageList: List<Photo>){
        for (image in imageList){
            if(image.url == images[0].url) continue
            images.add(image)
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ImageViewHolder {
        return ImageViewHolder(ItemImageSliderBinding.inflate(LayoutInflater.from(parent?.context), parent, false))
    }

    override fun onBindViewHolder(viewHolder: ImageViewHolder?, position: Int) {
        images[position].url?.let { viewHolder?.bind(it) }
    }
}