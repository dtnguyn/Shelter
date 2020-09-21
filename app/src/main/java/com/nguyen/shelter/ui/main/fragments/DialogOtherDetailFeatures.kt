package com.nguyen.shelter.ui.main.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.paging.ExperimentalPagingApi
import com.nguyen.shelter.R
import com.nguyen.shelter.api.response.CommunityFeature
import com.nguyen.shelter.model.PropertyDetail
import com.nguyen.shelter.ui.main.adapters.FeaturesExpandableListAdapter
import kotlinx.android.synthetic.main.detail_others.view.*


class DialogOtherDetailFeatures(private val propDetail: PropertyDetail): DialogFragment() {

    @ExperimentalPagingApi
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.detail_others, null)

            val hashMap = getCommunityFeaturesHashMap(propDetail.community)
            val expandAdapter = FeaturesExpandableListAdapter(
                propDetail.community,
                hashMap,
                requireContext()
            )

            view.expand_view.setAdapter(expandAdapter)

            builder.setView(view)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getCommunityFeaturesHashMap(features: List<CommunityFeature>): HashMap<String, List<String>>{
        val hashMap = HashMap<String, List<String>>()
        for (feature in features){
            val title = feature.category
            title?.let {
                hashMap.put(it, feature.featuresString ?: listOf())
            }
        }
        println("debug: $hashMap")
        return hashMap
    }


}