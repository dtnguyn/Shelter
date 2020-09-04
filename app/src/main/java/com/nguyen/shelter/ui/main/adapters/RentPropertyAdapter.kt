package com.nguyen.shelter.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.databinding.PropertyItemBinding
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.db.mapper.PropertyCacheMapper
import javax.inject.Inject


class RentPropertyAdapter
@Inject
constructor(
    private val cacheMapper: PropertyCacheMapper,
    private val detailOnClick: (String) -> Unit,
): PagingDataAdapter<PropertyCacheEntity, RentPropertyAdapter.RentPropertyViewHolder>(
    PROPERTY_COMPARATOR
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentPropertyViewHolder {
        return RentPropertyViewHolder(PropertyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: RentPropertyViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }




    inner class RentPropertyViewHolder(private val binding: PropertyItemBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(property: PropertyCacheEntity?, position: Int) {
            property?.let {
                val props = cacheMapper.mapFromEntity(property)
                val features = props.features

                binding.property = props

                features.apply {
                    if(bedsMax != null && bedsMin != null){
                        val beds: String? = if(props.features.bedsMax!! > props.features.bedsMin!!) "" + props.features.bedsMin!!.toInt() + "-" + props.features.bedsMax!!.toInt()
                        else props.features.bedsMin!!.toInt().toString()
                        binding.beds = beds
                    } else binding.beds = "N/A"

                    if(bathsMax != null && bathsMin != null){
                        val baths: String? = if(props.features.bathsMax!! > props.features.bathsMin!!) "" + props.features.bathsMin!!.toInt() + "-" + props.features.bathsMax!!.toInt()
                        else props.features.bathsMin!!.toInt().toString()
                        binding.baths = baths
                    } else binding.baths = "N/A"

                    if(areaMax != null && areaMin != null){
                        val area: String? =  if(props.features.areaMax!! > props.features.areaMin!!) "" + props.features.areaMin!!.toInt() + "-" + props.features.areaMax!!.toInt()
                        else props.features.areaMin!!.toInt().toString()
                        binding.area = area
                    } else binding.area = "N/A"

                    if(priceMax != null && priceMin != null){
                        val price: String? =  if(props.features.priceMax!! > props.features.priceMin!!) "" + props.features.priceMin!!.toInt() + "-$" + props.features.priceMax!!.toInt()
                        else props.features.priceMin!!.toInt().toString()
                        binding.price = price
                    } else binding.price = "N/A"
                }

                binding.container.setOnClickListener {
                    detailOnClick.invoke(property.id)
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