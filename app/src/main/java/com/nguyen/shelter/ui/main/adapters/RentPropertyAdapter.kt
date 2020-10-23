package com.nguyen.shelter.ui.main.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.databinding.PropertyItemBinding
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import com.skydoves.transformationlayout.TransformationLayout
import javax.inject.Inject


class RentPropertyAdapter
@Inject
constructor(
    private val cacheMapper: PropertyCacheMapper,
    private val detailOnClick: (Bundle, TransformationLayout) -> Unit
): PagingDataAdapter<PropertyCacheEntity, RentPropertyAdapter.RentPropertyViewHolder>(
    PROPERTY_COMPARATOR
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentPropertyViewHolder {
        return RentPropertyViewHolder(PropertyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: RentPropertyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }




    inner class RentPropertyViewHolder(private val binding: PropertyItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(property: PropertyCacheEntity?) {
            property?.let {
                val prop = cacheMapper.mapFromEntity(property)
                val features = prop.features

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

                    if(priceMax != null && priceMin != null){
                        val price: String? =  if(prop.features.priceMax!! > prop.features.priceMin!!) "$" + prop.features.priceMin!!.toInt() + "-$" + prop.features.priceMax!!.toInt()
                        else prop.features.priceMin!!.toInt().toString()
                        binding.price = price
                    } else binding.price = "$${prop.price}"
                }

                val transformationLayout = binding.transformationLayout
                transformationLayout.transitionName = prop.id

                binding.container.setOnClickListener {
                    println("debug: ${transformationLayout.transitionName}")
                    val photo = if(prop.photos.isNotEmpty()) prop.photos[0].url else ""
                    val bundle = transformationLayout.getBundle("TransformationParams")
                    bundle.putParcelable("prop", prop)
                    bundle.putString("photo", prop.photos[0].url)
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