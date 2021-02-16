package com.nguyen.shelter.ui.main.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.R
import com.nguyen.shelter.databinding.PropertyItemBinding
import com.nguyen.shelter.databinding.SalePropertyItemBinding
import com.nguyen.shelter.model.Property
import com.skydoves.transformationlayout.TransformationLayout

class SavedAdapter(private val savedList: List<Property>, private val detailOnClick: (Bundle, TransformationLayout) -> Unit): RecyclerView.Adapter<SavedAdapter.BaseViewHolder>() {

    abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(property: Property)
    }

    inner class RentSavedViewHolder(private val binding: PropertyItemBinding): BaseViewHolder(binding.root){
        override fun bind(property: Property) {
            val features = property.features

            binding.property = property

            features.apply {
                if(bedsMax != null && bedsMin != null){
                    val beds: String? = if(property.features.bedsMax!! > property.features.bedsMin!!) "" + property.features.bedsMin!!.toInt() + "-" + property.features.bedsMax!!.toInt()
                    else property.features.bedsMin!!.toInt().toString()
                    binding.beds = beds
                } else binding.beds = property.beds.toString()

                if(bathsMax != null && bathsMin != null){
                    val baths: String? = if(property.features.bathsMax!! > property.features.bathsMin!!) "" + property.features.bathsMin!!.toInt() + "-" + property.features.bathsMax!!.toInt()
                    else property.features.bathsMin!!.toInt().toString()
                    binding.baths = baths
                } else binding.baths = property.baths.toString()

                if(areaMax != null && areaMin != null){
                    val area: String? =  if(property.features.areaMax!! > property.features.areaMin!!) "" + property.features.areaMin!!.toInt() + "-" + property.features.areaMax!!.toInt()
                    else property.features.areaMin!!.toInt().toString()
                    binding.area = area
                } else binding.area = property.buildingSize

                if(priceMax != null && priceMin != null){
                    val price: String? =  if(property.features.priceMax!! > property.features.priceMin!!) "$" + property.features.priceMin!!.toInt() + "-$" + property.features.priceMax!!.toInt()
                    else property.features.priceMin!!.toInt().toString()
                    binding.price = price
                } else binding.price = "$${property.price}"
            }

            val transformationLayout = binding.transformationLayout
            transformationLayout.transitionName = property.id

            binding.container.setOnClickListener {
                try {
                    println("debug: ${transformationLayout.transitionName}")
                    val photo = if(property.photos.isNotEmpty()) property.photos[0].url else ""
                    val bundle = binding.transformationLayout.getBundle("TransformationParams")
                    bundle.putParcelable("prop", property)
                    bundle.putString("photo", property.photos[0].url)
                    detailOnClick.invoke(bundle, binding.transformationLayout)
                } catch (e: Exception){
                    println("debug: ${e.message}")
                }

            }
        }
    }

    inner class SaleSavedViewHolder(private val binding: SalePropertyItemBinding): BaseViewHolder(binding.root){
        override fun bind(property: Property) {
            val features = property.features

            binding.url = if(property.photos.isNotEmpty()) property.photos[0].url else property.thumbnail
            binding.property = property

            features.apply {
                if(bedsMax != null && bedsMin != null){
                    val beds: String? = if(property.features.bedsMax!! > property.features.bedsMin!!) "" + property.features.bedsMin!!.toInt() + "-" + property.features.bedsMax!!.toInt()
                    else property.features.bedsMin!!.toInt().toString()
                    binding.beds = beds
                } else binding.beds = property.beds.toString()

                if(bathsMax != null && bathsMin != null){
                    val baths: String? = if(property.features.bathsMax!! > property.features.bathsMin!!) "" + property.features.bathsMin!!.toInt() + "-" + property.features.bathsMax!!.toInt()
                    else property.features.bathsMin!!.toInt().toString()
                    binding.baths = baths
                } else binding.baths = property.baths.toString()

                if(areaMax != null && areaMin != null){
                    val area: String? =  if(property.features.areaMax!! > property.features.areaMin!!) "" + property.features.areaMin!!.toInt() + "-" + property.features.areaMax!!.toInt()
                    else property.features.areaMin!!.toInt().toString()
                    binding.area = area
                } else binding.area = property.buildingSize
            }

            val transformationLayout = binding.transformationLayout
            transformationLayout.transitionName = property.id
            binding.container.setOnClickListener {
//                val bundle = transformationLayout.getBundle("TransformationParams")
//                val photo = if(property.photos.isNotEmpty()) property.photos[0].url else ""
//                bundle.putParcelable("prop", property)
//                bundle.putString("photo", photo)
//                detailOnClick.invoke(bundle, binding.transformationLayout)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when(savedList[position].status){
            "for_rent" -> R.layout.property_item
            else -> R.layout.sale_property_item
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            R.layout.property_item -> RentSavedViewHolder(PropertyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> SaleSavedViewHolder(SalePropertyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(savedList[position])
    }

    override fun getItemCount(): Int {
        return savedList.size
    }
}




