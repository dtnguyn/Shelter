package com.nguyen.shelter.ui.main.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.databinding.SalePropertyItemBinding
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.skydoves.transformationlayout.TransformationLayout
import javax.inject.Inject


class SalePropertyAdapter
@Inject
constructor(
    private val cacheMapper: PropertyCacheMapper,
    private val detailOnClick: (Bundle, TransformationLayout) -> Unit
): PagingDataAdapter<PropertyCacheEntity, SalePropertyAdapter.SalePropertyViewHolder>(
    PROPERTY_COMPARATOR
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalePropertyViewHolder {
        return SalePropertyViewHolder(SalePropertyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: SalePropertyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }




    inner class SalePropertyViewHolder(private val binding: SalePropertyItemBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(property: PropertyCacheEntity?) {
            property?.let {
                val prop = cacheMapper.mapFromEntity(property)
                val features = prop.features

                println("debug: ${prop.photos} ${property.photos}")

                binding.url = if(prop.photos.isNotEmpty()) prop.photos[0].url else prop.thumbnail
                binding.property = prop

                features.apply {
                    if(bedsMax != null && bedsMin != null){
                        val beds: String? = if(prop.features.bedsMax!! > prop.features.bedsMin!!) "" + prop.features.bedsMin!!.toInt() + "-" + prop.features.bedsMax!!.toInt()
                        else prop.features.bedsMin!!.toInt().toString()
                        binding.beds = beds
                    } else binding.beds = prop.beds.toString()

                    if(bathsMax != null && bathsMin != null){
                        val baths: String? = if(prop.features.bathsMax!! > prop.features.bathsMin!!) "" + prop.features.bathsMin!!.toInt() + "-" + prop.features.bathsMax!!.toInt()
                        else prop.features.bathsMin!!.toInt().toString()
                        binding.baths = baths
                    } else binding.baths = prop.baths.toString()

                    if(areaMax != null && areaMin != null){
                        val area: String? =  if(prop.features.areaMax!! > prop.features.areaMin!!) "" + prop.features.areaMin!!.toInt() + "-" + prop.features.areaMax!!.toInt()
                        else prop.features.areaMin!!.toInt().toString()
                        binding.area = area
                    } else binding.area = prop.buildingSize
                }

                val transformationLayout = binding.transformationLayout
                transformationLayout.transitionName = prop.id
                binding.container.setOnClickListener {
                    val bundle = transformationLayout.getBundle("TransformationParams")
                    val photo = if(prop.photos.isNotEmpty()) prop.photos[0].url else ""
                    bundle.putString("id", prop.id)
                    bundle.putString("photo", photo)
                    detailOnClick.invoke(bundle, binding.transformationLayout)
                }
            }



        }

    }


    companion object {
        private val PROPERTY_COMPARATOR = object : DiffUtil.ItemCallback<PropertyCacheEntity>() {
            override fun areItemsTheSame(oldItem: PropertyCacheEntity, newItem: PropertyCacheEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PropertyCacheEntity, newItem: PropertyCacheEntity): Boolean =
                oldItem.id == newItem.id
        }
    }


}