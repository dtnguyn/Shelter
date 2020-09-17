package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nguyen.shelter.R
import com.nguyen.shelter.api.response.School
import com.nguyen.shelter.ui.main.adapters.MapBottomSheetAdapter
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.android.synthetic.main.fragment_map.view.map_bottom_recyclerview
import kotlinx.android.synthetic.main.fragment_map.view.map_bottom_sheet_button


class MapFragment : Fragment() {

    private lateinit var latLng: LatLng
    private lateinit var houseMarker: Marker
    private var schoolList: List<School>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        arguments?.let {
            latLng = LatLng(it.getDouble("lat"), it.getDouble("lng"))
            val bottomSheet = BottomSheetBehavior.from(view.map_bottom_sheet)
            view.map_bottom_sheet_button.setOnClickListener {
                bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            schoolList = it.getParcelableArrayList("schools")
            schoolList?.let {schools ->
                val adapter = MapBottomSheetAdapter(schools)
                val recyclerView = view.map_bottom_recyclerview
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
            }

        }

        view.back_button.setOnClickListener {
            findNavController().popBackStack()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        println("debug: latLng $latLng")
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.map_style
            )
        )

        houseMarker = googleMap.addMarker(
            MarkerOptions().position(latLng).draggable(false)
                .title("Current house")
                .icon(
                    BitmapDescriptorFactory
                        .fromResource(R.drawable.house_location)
                )
        )

        println("debug: schoolList $schoolList")
        schoolList?.forEach { school ->
            if(school.latitude != null && school.longitude != null){
                googleMap.addMarker(
                    MarkerOptions().position(LatLng(school.latitude!!.toDouble(), school.longitude!!.toDouble())).draggable(false)
                        .title(school.name)
                        .icon(
                            BitmapDescriptorFactory
                                .fromResource(R.drawable.school_location)
                        )
                )
            }

        }
    }
}