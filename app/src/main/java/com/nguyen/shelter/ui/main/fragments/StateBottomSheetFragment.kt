package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nguyen.shelter.R
import com.nguyen.shelter.ui.main.adapters.StateBottomSheetAdapter
import kotlinx.android.synthetic.main.fragment_bottom_state.view.*


class StateBottomSheetFragment(private val stateOnClick: (String) -> Unit) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_state, container, false)

        val states = resources.getStringArray(R.array.state_code)

        val adapter = StateBottomSheetAdapter(states){
            stateOnClick.invoke(it)
            dismiss()
        }
        val recyclerView = view.states_recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        return view
    }


}