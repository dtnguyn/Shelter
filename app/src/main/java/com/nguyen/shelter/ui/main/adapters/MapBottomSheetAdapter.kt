package com.nguyen.shelter.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.R
import com.nguyen.shelter.api.response.School
import com.nguyen.shelter.databinding.ItemSchoolBinding
import kotlinx.android.synthetic.main.item_map_group.view.*

class MapBottomSheetAdapter(private val schools: List<School>): RecyclerView.Adapter<MapBottomSheetAdapter.BaseViewHolder>() {


    abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Any)
    }

    class GroupViewHolder(itemView: View): BaseViewHolder(itemView){
        override fun bind(item: Any) {
            val title = item as String
            itemView.group_title.text = title
        }

    }

    class SchoolViewHolder(private val binding: ItemSchoolBinding): BaseViewHolder(binding.root){
        override fun bind(item: Any) {
            val school = item as School
            var schoolType = ""

            for(i in school.educationLevels.indices){
                val type = if(i == 0) school.educationLevels[i]
                                else ", ${school.educationLevels[i]}"
                schoolType += type
            }

            schoolType += ", ${school.type}"

            binding.school = school
            binding.schoolType = schoolType

        }

    }

    override fun getItemViewType(position: Int): Int {

//        if(position == 0){
//            if(totalGroup > groupCounter) groupCounter++
//            return R.layout.item_map_group
//        } else {
//            return R.layout.item_school
//        }

        return R.layout.item_school

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return when(viewType){
            R.layout.item_map_group -> {
                GroupViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            }
            R.layout.item_school -> {
                SchoolViewHolder(ItemSchoolBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> SchoolViewHolder(ItemSchoolBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder){
            is GroupViewHolder -> {
                holder.bind("Schools")
            }

            is SchoolViewHolder -> {
                holder.bind(schools[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return schools.size
    }
}