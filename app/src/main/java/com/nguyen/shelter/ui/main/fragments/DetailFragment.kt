package com.nguyen.shelter.ui.main.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.nguyen.shelter.R
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.api.response.WorkingHour
import com.nguyen.shelter.databinding.FragmentDetailBinding
import com.nguyen.shelter.model.PropertyDetail
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.adapters.FloorPlanAdapter
import com.nguyen.shelter.ui.main.adapters.ImageSliderAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailBinding
    private lateinit var adapter: ImageSliderAdapter
    private lateinit var imageSlider: SliderView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        val params = arguments?.getParcelable<TransformationLayout.Params>("TransformationParams")
        onTransformationEndContainer(params)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        (activity as MainActivity?)?.hideActionBar()

        binding.finishLoading = false

        binding.priceShimmerContainer.startShimmer()
        binding.addressShimmerContainer.startShimmer()


        imageSlider = binding.detailImageSlider

        val photosUrl: String? = arguments?.getString("photo")
        photosUrl?.let { url ->
            adapter = ImageSliderAdapter(arrayListOf(Photo(url))){bundle ->
                findNavController().navigate(R.id.photo_detail_fragment, bundle)
            }
            imageSlider.setSliderAdapter(adapter)
        }




        subscribeObservers()

        val id = arguments?.getString("id")
        id?.let {
            viewModel.setStateEvent(MainStateEvent.GetPropertyDetail(id))
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("id")
        view.scrollView.transitionName = id!!
    }

    private fun subscribeObservers(){
        viewModel.rentPropertyDetail.observe(viewLifecycleOwner, { propDetail ->
            fragmentInit(propDetail)
        })
    }
    
    private fun fragmentInit(propDetail: PropertyDetail){
        binding.apply {
            finishLoading = true
            addressShimmerContainer.stopShimmer()
            leaseTermInclude.leaseTermShimmerContainer.stopShimmer()
            typeInclude.typesShimmerContainer.stopShimmer()
            featuresInclude.featuresShimmerContainer.stopShimmer()
            featuresInclude.featuresShimmerContainer.stopShimmer()
            contactInclude.contactShimmerContainer.stopShimmer()
            priceShimmerContainer.stopShimmer()
            detail = propDetail
            fullDescription = propDetail.description
            trimDescription = propDetail.description.subSequence(
                0,
                propDetail.description.length / 4
            ).toString() + ".....(Read more)"
            isFullDescription = false
            descriptionInclude.descriptionText.setOnClickListener {
                isFullDescription = isFullDescription!!.not()
            }
            workSchedule = propDetail.office.workingHours?.let { getSortedWorkDaysString(it) }

            otherFeaturesButton.setOnClickListener {
                val dialog = DialogOtherDetailFeatures(propDetail)
                dialog.show(requireActivity().supportFragmentManager, "Other features dialog")
            }

            mapButton.setOnClickListener {
                println("debug: before latLng ${propDetail.address.latitude?.toDouble()} ${propDetail.address.longitude?.toDouble()}")
                val bundle = bundleOf(
                    "lat" to propDetail.address.latitude?.toDouble(),
                    "lng" to propDetail.address.longitude?.toDouble(),
                    "schools" to propDetail.schools
                )
                findNavController().navigate(R.id.map_fragment, bundle)
            }

            applyOnlineButton.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(propDetail.url))
                startActivity(browserIntent)
            }

        }

        choosePropTypeImg(propDetail.category)

        adapter.addImages(propDetail.photos)
        val features = propDetail.features



        val recyclerView = binding.floorPlanInclude.floorPlanRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val filteredList = propDetail.floorPlans.filter { floorPlan -> floorPlan.name != null }
        recyclerView.adapter = FloorPlanAdapter(filteredList){bundle ->
            println("debug: photo before ${bundle.getParcelableArrayList<Photo>("photos")}")
            findNavController().navigate(R.id.photo_detail_fragment, bundle)
        }

        features.apply {
            if (bedsMax != null && bedsMin != null) {
                val beds: String? =
                    if (bedsMax!! > bedsMin!!) "" + bedsMin!!.toInt() + "-" + bedsMax!!.toInt()
                    else bedsMin!!.toInt().toString()
                binding.beds = beds
            } else binding.beds = "N/A"

            if (bathsMax != null && bathsMin != null) {
                val baths: String? =
                    if (bathsMax!! > bathsMin!!) "" + bathsMin!!.toInt() + "-" + bathsMax!!.toInt()
                    else bathsMin!!.toInt().toString()
                binding.baths = baths
            } else binding.baths = "N/A"

            if (areaMax != null && areaMin != null) {
                val area: String? =
                    if (areaMax!! > areaMin!!) "" + areaMin!!.toInt() + "-" + areaMax!!.toInt()
                    else areaMin!!.toInt().toString()
                binding.sqft = area
            } else binding.sqft = "N/A"

            if (priceMax != null && priceMin != null) {
                val price: String? =
                    if (priceMax!! > priceMin!!) "$" + priceMin!!.toInt() + "-$" + priceMax!!.toInt()
                    else priceMin!!.toInt().toString()
                binding.price = price
            } else binding.price = "N/A"
        }
    }

    private fun getSortedWorkDaysString(workingHour: List<WorkingHour>): String{
        if(workingHour.isEmpty()) return ""

        val workDays = workingHour[0].workDays!!
        val startHour = workingHour[0].startWorkingHour
        val endHour = workingHour[0].endWorkingHour

        if (workDays.isEmpty()) return ""
        val sortedWorkDays = arrayListOf<String>()
        val map = hashMapOf<String, Boolean>()
        for (day in workDays) {
            map[day.decapitalize(Locale.ROOT)] = true
        }

        if(map["monday"] == true) sortedWorkDays.add("Monday")
        if(map["tuesday"] == true) sortedWorkDays.add("Tuesday")
        if(map["wednesday"] == true) sortedWorkDays.add("Wednesday")
        if(map["thursday"] == true) sortedWorkDays.add("Thursday")
        if(map["friday"] == true) sortedWorkDays.add("Friday")
        if(map["saturday"] == true) sortedWorkDays.add("Saturday")
        if(map["sunday"] == true) sortedWorkDays.add("Sunday")

        val hour: String

        hour = if(startHour == "By Appointment") startHour
        else "from $startHour to $endHour"

        return "${sortedWorkDays[0]} to ${sortedWorkDays[sortedWorkDays.lastIndex]}, $hour"
    }


    private fun choosePropTypeImg(type: String){
        when(type){
            "apartment" -> {
                binding.typeInclude.typeImg.setImageResource(R.drawable.apartment_selected)
            }

            "single_family" -> {
                binding.typeInclude.typeImg.setImageResource(R.drawable.single_family_selected)
            }

            "multi_familty" -> {
                binding.typeInclude.typeImg.setImageResource(R.drawable.multi_family_selected)
            }

            "condo" -> {
                binding.typeInclude.typeImg.setImageResource(R.drawable.condo_selected)
            }

            "mobile" -> {
                binding.typeInclude.typeImg.setImageResource(R.drawable.mobile_selected)
            }

            "land" -> {
                binding.typeInclude.typeImg.setImageResource(R.drawable.land_selected)
            }

            "farm" -> {
                binding.typeInclude.typeImg.setImageResource(R.drawable.farm_selected)
            }

            else -> {
                binding.typeInclude.typeImg.setImageResource(R.drawable.apartment_selected)
            }
        }
    }








}