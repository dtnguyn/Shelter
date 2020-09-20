package com.nguyen.shelter.ui.main.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.api.response.FloorPlan
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.databinding.ItemFloorPlanBinding

class FloorPlanAdapter(private val floorPlans: List<FloorPlan>, private val onClick: (Bundle) -> Unit) : RecyclerView.Adapter<FloorPlanAdapter.FloorPlanViewHolder>(){


    inner class FloorPlanViewHolder(private val binding: ItemFloorPlanBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(floorPlan: FloorPlan){

            binding.beds = if(floorPlan.beds == null) "N/A" else  floorPlan.beds!!.toInt().toString()
            binding.baths = if(floorPlan.baths == null) "N/A" else  floorPlan.baths!!.toInt().toString()
            binding.price = if(floorPlan.price == null) "N/A" else  floorPlan.price!!.toInt().toString()
            binding.area = if(floorPlan.area == null) "N/A" else  floorPlan.area!!.toInt().toString()

            binding.floorPlan = floorPlan

            binding.floorPlanImageView.setOnClickListener {
                if (floorPlan.photo == null) return@setOnClickListener
                val list = ArrayList<Photo>()
                list.add(floorPlan.photo!!)
                onClick.invoke(bundleOf(
                    "photos" to list
                ))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloorPlanViewHolder {
        return FloorPlanViewHolder(ItemFloorPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FloorPlanViewHolder, position: Int) {
        holder.bind(floorPlans[position])
    }

    override fun getItemCount(): Int {
        return floorPlans.size
    }
}