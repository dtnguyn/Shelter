package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.nguyen.shelter.databinding.FragmentFilterRentBinding
import com.nguyen.shelter.model.PropertyFilter
import com.nguyen.shelter.repo.MainRepository.Companion.RENT
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.filter_location.*
import kotlinx.android.synthetic.main.filter_location.view.*
import kotlinx.android.synthetic.main.filter_others.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class RentFilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterRentBinding
    private lateinit var propertyFilter: PropertyFilter
    private lateinit var propTypes: HashMap<String, Boolean>
    private lateinit var others: HashMap<String, Boolean>

    private val viewModel: MainViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity?)?.hideActionBar()
    }


    @ExperimentalPagingApi
    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterRentBinding.inflate(inflater, container, false)

        subscribeObservers()

        viewModel.setStateEvent(MainStateEvent.GetPropertyFilter(RENT))


        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            propertyFilter.city = city_edit_text.text.toString()
            propertyFilter.postalCode = zip_code_edit_text.text.toString()

            var typesQuery = ""
            for(item in propTypes){
                if(item.key == "any" && item.value) {
                    typesQuery = "any"
                    break
                }
                if(item.value){
                    typesQuery += item.key + ","
                }
            }
            propertyFilter.type = typesQuery


            var othersQuery = ""
            for(item in others){
                if(item.value){
                    othersQuery += item.key + ","
                }
            }
            propertyFilter.features = othersQuery

            println("debug: $propertyFilter")
            viewModel.setStateEvent(MainStateEvent.SaveRentPropertyFilter(propertyFilter))
            findNavController().popBackStack()
        }

        return binding.root
    }


    private fun convertStringToMap(stringValue: String?): HashMap<String, Boolean>{
        if (stringValue == null) return HashMap()
        val list =  stringValue.split(",")
        val map: HashMap<String, Boolean> = HashMap()
        for(item in list){
            val trimItem = item.trim()
            if(trimItem.isBlank()) continue
            map[trimItem] = true
        }
        println("debug: $map")
        return map
    }

    private fun subscribeObservers(){
        viewModel.rentPropertyFilter.observe(viewLifecycleOwner, {filter ->

            propertyFilter = filter
            propertyFilter.apply {
                if(priceMin == null) priceMin = 0
                if(priceMax == null) priceMax = 3000
                if(lotMin == null) lotMin = 0
                if(lotMax == null) lotMax = 5000
                if(areaMin == null) areaMin = 0
                if(areaMax == null) areaMax = 5000
            }
            binding.filter = propertyFilter

            propTypes = convertStringToMap(propertyFilter.type)
            others = convertStringToMap(propertyFilter.features)

            setUpSortFilter()
            setUpLocationFilter()
            setUpFeatureFilter()
            setUpTypeFilter(propTypes)
            setUpPetFilter()
            setUpOthersFilter(others)
        })
    }

    private fun setUpSortFilter(){

        binding.sortInclude.apply {
            sortOption = "relevance"
            relevance.setOnClickListener {
                sortOption = "relevance"
                propertyFilter.sort = "relevance"
            }

            date.setOnClickListener {
                sortOption = "date"
                propertyFilter.sort = "newest"
            }

            priceMin.setOnClickListener {
                sortOption = "priceMin"
                propertyFilter.sort = "price_low"
            }

            priceMax.setOnClickListener {
                sortOption = "priceMax"
                propertyFilter.sort = "price_high"
            }

            sqft.setOnClickListener {
                sortOption = "sqft"
                propertyFilter.sort = "sqft_high"
            }

            photos.setOnClickListener {
                sortOption = "photos"
                propertyFilter.sort = "photos"
            }
        }
    }

    private fun setUpLocationFilter(){
        binding.locationInclude.apply {
            state_button.text = propertyFilter.stateCode
            city_edit_text.setText(propertyFilter.city)
            state_button.setOnClickListener {
                //findNavController().navigate(R.id.state_fragment_bottom)
                val bottomSheet = StateBottomSheetFragment{stateCode ->
                    propertyFilter.stateCode = stateCode
                    state_button.text = stateCode
                }
                activity?.supportFragmentManager?.let { manager -> bottomSheet.show(manager, "STATE_CODE") }
            }

            city_edit_text.setOnClickListener {

            }
        }
    }

    private fun setUpFeatureFilter(){
        binding.featuresInclude.apply {

            priceSlider.values = mutableListOf(propertyFilter.priceMin?.toFloat(), propertyFilter.priceMax?.toFloat())
            priceSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.priceMin = rangeSlider.values[0].toInt()
                propertyFilter.priceMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            bedsSlider.values = mutableListOf(propertyFilter.bedsMin?.toFloat(), propertyFilter.bedsMax?.toFloat())
            bedsSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.bedsMin = rangeSlider.values[0].toInt()
                propertyFilter.bedsMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            bathsSlider.values = mutableListOf(propertyFilter.bathsMin?.toFloat(), propertyFilter.bathsMax?.toFloat())
            bathsSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.bathsMin = rangeSlider.values[0].toInt()
                propertyFilter.bathsMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            println("debug: ${propertyFilter.areaMin} ${propertyFilter.areaMax}")
            sqftSlider.values = mutableListOf(propertyFilter.areaMin?.toFloat(), propertyFilter.areaMax?.toFloat())
            sqftSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.areaMin = rangeSlider.values[0].toInt()
                propertyFilter.areaMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            lotSlider.values = mutableListOf(propertyFilter.lotMin?.toFloat(), propertyFilter.lotMax?.toFloat())
            lotSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.lotMin = rangeSlider.values[0].toInt()
                propertyFilter.lotMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }
        }
    }

    private fun setUpTypeFilter(types: HashMap<String, Boolean>){
        val propTypeBinding = binding.propTypeInclude
        propTypeBinding.apply {

            anyClicked = types["any"]
            anyCardView.setOnClickListener {
                anyClicked = anyClicked?.not() ?: true
                if(anyClicked!!){
                    apartmentClicked = false
                    singleClicked = false
                    multiClicked = false
                    condoClicked = false
                    landClicked = false
                    farmClicked =false
                    mobileClicked = false
                }
                types["any"] = anyClicked!!
            }

            apartmentClicked = types["apartment"]
            apartmentCardView.setOnClickListener {
                apartmentClicked = apartmentClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["apartment"] = apartmentClicked!!
            }

            singleClicked = types["single_family"]
            singleCardView.setOnClickListener {
                singleClicked = singleClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["single_family"] = singleClicked!!
            }

            multiClicked = types["multi_family"]
            multiCardView.setOnClickListener {
                multiClicked = multiClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["multi_family"] = multiClicked!!
            }

            condoClicked = types["condo"]
            condoCardView.setOnClickListener {
                condoClicked = condoClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["condo"] = condoClicked!!
            }

            mobileClicked = types["mobile"]
            mobileCardView.setOnClickListener {
                mobileClicked = mobileClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["mobile"] = mobileClicked!!
            }

            farmClicked = types["farm"]
            farmCardView.setOnClickListener {
                farmClicked = farmClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["farm"] = farmClicked!!
            }

            landClicked = types["land"]
            landCardView.setOnClickListener {
                landClicked = landClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["land"] = landClicked!!
            }
        }
    }

    private fun setUpPetFilter() {
        val petsBinding = binding.petInclude
        petsBinding.apply {
            dogClicked = propertyFilter.allow_dogs
            catClicked = propertyFilter.allow_cats

            dogCardView.setOnClickListener {
                dogClicked = dogClicked?.not() ?: true
                propertyFilter.allow_dogs = dogClicked
            }

            catCardView.setOnClickListener {
                catClicked = catClicked?.not() ?: true
                propertyFilter.allow_cats = catClicked
            }

        }
    }

    private fun setUpOthersFilter(others: HashMap<String, Boolean>){
        binding.othersInclude.apply {
            this.recreation_checkbox!!

            recreation_checkbox.isChecked = others["recreation"] ?: false
            recreation_checkbox.setOnCheckedChangeListener { _, state ->
                others["recreation"] = state
            }

            pool_checkbox.isChecked = others["pool"] ?: false
            pool_checkbox.setOnCheckedChangeListener { _, state ->
                others["pool"] = state
            }

            outdoor_space_checkbox.isChecked = others["outdoor_space"] ?: false
            outdoor_space_checkbox.setOnCheckedChangeListener { _, state ->
                others["outdoor_space"] = state
            }

            garage_checkbox.isChecked = others["garage"] ?: false
            garage_checkbox.setOnCheckedChangeListener { _, state ->
                others["garage"] = state
            }

            central_air_checkbox.isChecked = others["central_air"] ?: false
            central_air_checkbox.setOnCheckedChangeListener { _, state ->
                others["central_air"] = state
            }

            fireplace_checkbox.isChecked = others["fireplace"] ?: false
            fireplace_checkbox.setOnCheckedChangeListener { _, state ->
                others["fireplace"] = state
            }

            spa_checkbox.isChecked = others["spa"] ?: false
            spa_checkbox.setOnCheckedChangeListener { _, state ->
                others["spa"] = state
            }

            dishwasher_checkbox.isChecked = others["dishwasher"] ?: false
            dishwasher_checkbox.setOnCheckedChangeListener { _, state ->
                others["dishwasher"] = state
            }

            doorman_checkbox.isChecked = others["doorman"] ?: false
            doorman_checkbox.setOnCheckedChangeListener { _, state ->
                others["doorman"] = state
            }

            elevator_checkbox.isChecked = others["elevator"] ?: false
            elevator_checkbox.setOnCheckedChangeListener { _, state ->
                others["elevator"] = state
            }

            laundry_room_checkbox.isChecked = others["laundry_room"] ?: false
            laundry_room_checkbox.setOnCheckedChangeListener { _, state ->
                others["laundry_room"] = state
            }

            no_fee_checkbox.isChecked = others["no_fee"] ?: false
            no_fee_checkbox.setOnCheckedChangeListener { _, state ->
                others["no_fee"] = state
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity?)?.showActionBar()
    }



}