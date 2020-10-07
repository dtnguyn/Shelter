package com.nguyen.shelter.ui.community.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nguyen.shelter.databinding.ItemAddImageBinding
import com.nguyen.shelter.model.PhotoUri

class AddImageAdapter(private val images: ArrayList<PhotoUri>, private val imageOnClick: (Int) -> Unit): RecyclerView.Adapter<AddImageAdapter.AddImageViewHolder>() {

    inner class AddImageViewHolder(private val binding: ItemAddImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: PhotoUri, position: Int) {

            binding.imageUrl = image.uri.toString()

            binding.addImage.setOnClickListener {
                imageOnClick.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddImageViewHolder {
        return AddImageViewHolder(
            ItemAddImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddImageViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun refreshImages(){
        images.clear()
        notifyDataSetChanged()
    }


}