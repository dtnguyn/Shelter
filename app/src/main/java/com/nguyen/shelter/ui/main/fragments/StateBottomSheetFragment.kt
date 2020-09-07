package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nguyen.shelter.R
import com.nguyen.shelter.ui.main.adapters.BottomSheetAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_bottom_state.view.*


class StateBottomSheetFragment(private val stateOnClick: (String) -> Unit) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_state, container, false)

        val states = resources.getStringArray(R.array.state_code)

        val adapter = BottomSheetAdapter(states){
            stateOnClick.invoke(it)
            dismiss()
        }
        val recyclerView = view.state_recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        return view
    }


}