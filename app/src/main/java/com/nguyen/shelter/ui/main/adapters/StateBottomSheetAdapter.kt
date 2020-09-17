package com.nguyen.shelter.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.R
import kotlinx.android.synthetic.main.item_state.view.*

class StateBottomSheetAdapter(private val items: Array<String>, private val itemOnClick : (String) -> Unit) : RecyclerView.Adapter<StateBottomSheetAdapter.BottomSheetViewHolder>(){

    inner class BottomSheetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: String){
            itemView.item_name.text = item

            itemView.setOnClickListener {
                itemOnClick.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_state, parent, false))
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}


