package com.nguyen.shelter.ui.main.adapters

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.nguyen.shelter.api.response.CommunityFeature
import kotlinx.android.synthetic.main.list_group.view.*
import kotlinx.android.synthetic.main.list_item.view.*


class FeaturesExpandableListAdapter
    (private val features: List<CommunityFeature>,
     private val featureItems: HashMap<String, List<String>>,
     private val context: Context) : BaseExpandableListAdapter() {


    @SuppressLint("InflateParams")
    override fun getGroupView(
        position: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        val view = convertView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(com.nguyen.shelter.R.layout.list_group, null)


        view.group_title.text = getGroup(position) as String

        return view

    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {

        val view = convertView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(com.nguyen.shelter.R.layout.list_item, null)


        val groupItem = getChild(listPosition, expandedListPosition) as String

        view.group_item.text = groupItem

        return view
    }


    override fun getGroupCount(): Int {
        return features.size
    }

    override fun getChildrenCount(listPosition: Int): Int {
        val listTitle = features[listPosition].category
        val size = featureItems[listTitle]!!.size
        println("debug: size $size")
        return featureItems[listTitle]!!.size
    }

    override fun getGroup(position: Int): Any {
        return features[position].category ?: "N/A"
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        val listTitle = features[listPosition].category
        return featureItems[listTitle]?.get(expandedListPosition) ?: "N/A"
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getChildId(listPosition: Int, listExpandedPosition: Int): Long {
        return listExpandedPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }


    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}