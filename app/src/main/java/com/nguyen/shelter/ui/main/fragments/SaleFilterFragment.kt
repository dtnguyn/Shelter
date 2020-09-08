package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.nguyen.shelter.databinding.FragmentFilterSaleBinding
import com.nguyen.shelter.model.PropertyFilter
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.filter_location.*
import kotlinx.android.synthetic.main.filter_location.view.*
import kotlinx.android.synthetic.main.filter_others.view.*
import kotlinx.android.synthetic.main.filter_sale_others.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SaleFilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterSaleBinding
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
        binding = FragmentFilterSaleBinding.inflate(inflater, container, false)



        propTypes = hashMapOf(
            "any" to true,
            "apartment" to false,
            "single_family" to false,
            "multi_family" to false,
            "condo" to false,
            "mobile" to false,
            "land" to false,
            "farm" to false,
        )

         others = hashMapOf(
            "recreation" to false,
            "pool" to false,
            "outdoor_space" to false,
            "garage" to false,
            "central_air" to false,
            "fireplace" to false,
            "spa" to false,
            "dishwasher" to false,
            "doorman" to false,
            "elevator" to false,
            "laundry_room" to false,
            "no_fee" to false,
        )


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
            viewModel.setStateEvent(MainStateEvent.SaveSalePropertyFilter(propertyFilter))
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun subscribeObservers(){
        viewModel.rentPropertyFilter.observe(viewLifecycleOwner, {filter ->
            propertyFilter = filter
            propertyFilter.priceMin = 100000
            propertyFilter.priceMax = 10000000
            binding.filter = propertyFilter

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

            priceSaleSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.priceMin = rangeSlider.values[0].toInt()
                propertyFilter.priceMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            bedsSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.bedsMin = rangeSlider.values[0].toInt()
                propertyFilter.bedsMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            bathsSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.bathsMin = rangeSlider.values[0].toInt()
                propertyFilter.bathsMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            sqftSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.areaMin = rangeSlider.values[0].toInt()
                propertyFilter.areaMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            lotSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.lotMin = rangeSlider.values[0].toInt()
                propertyFilter.lotMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }

            ageSlider.addOnChangeListener { rangeSlider, _, _ ->
                propertyFilter.ageMin = rangeSlider.values[0].toInt()
                propertyFilter.ageMax = rangeSlider.values[1].toInt()
                binding.filter = propertyFilter
            }
        }

        binding.othersFeaturesInclude.apply {
            foreclosure_checkbox.setOnCheckedChangeListener { _, state ->
                propertyFilter.isForeclosure = state
            }

            open_house_checkbox.setOnCheckedChangeListener { _, state ->
                propertyFilter.hasOpenHouse = state
            }

            is_pending_checkbox.setOnCheckedChangeListener { _, state ->
                propertyFilter.isPending = state
            }

            not_yet_built_checkbox.setOnCheckedChangeListener { _, state ->
                propertyFilter.isNewPlan = state
            }

            contingent_checkbox.setOnCheckedChangeListener { _, state ->
                propertyFilter.isContingent = state
            }

            new_construction_checkbox.setOnCheckedChangeListener { _, state ->
                propertyFilter.isNewConstruction = state
            }
        }
    }

    private fun setUpTypeFilter(types: HashMap<String, Boolean>){
        val propTypeBinding = binding.propTypeInclude
        propTypeBinding.apply {
            anyClicked = true
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


            apartmentCardView.setOnClickListener {
                apartmentClicked = apartmentClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["apartment"] = apartmentClicked!!
            }

            singleCardView.setOnClickListener {
                singleClicked = singleClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["single_family"] = singleClicked!!
            }

            multiCardView.setOnClickListener {
                multiClicked = multiClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["multi_family"] = multiClicked!!
            }

            condoCardView.setOnClickListener {
                condoClicked = condoClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["condo"] = condoClicked!!
            }

            mobileCardView.setOnClickListener {
                mobileClicked = mobileClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["mobile"] = mobileClicked!!
            }

            farmCardView.setOnClickListener {
                farmClicked = farmClicked?.not() ?: true
                anyClicked = false
                types["any"] = anyClicked!!
                types["farm"] = farmClicked!!
            }

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
            dogClicked = true
            catClicked = false

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
            recreation_checkbox.setOnCheckedChangeListener { _, state ->
                others["recreation"] = state
            }

            pool_checkbox.setOnCheckedChangeListener { _, state ->
                others["pool"] = state
            }

            outdoor_space_checkbox.setOnCheckedChangeListener { _, state ->
                others["outdoor_space"] = state
            }

            garage_checkbox.setOnCheckedChangeListener { _, state ->
                others["garage"] = state
            }

            central_air_checkbox.setOnCheckedChangeListener { _, state ->
                others["central_air"] = state
            }

            fireplace_checkbox.setOnCheckedChangeListener { _, state ->
                others["fireplace"] = state
            }

            spa_checkbox.setOnCheckedChangeListener { _, state ->
                others["spa"] = state
            }

            dishwasher_checkbox.setOnCheckedChangeListener { _, state ->
                others["dishwasher"] = state
            }

            doorman_checkbox.setOnCheckedChangeListener { _, state ->
                others["doorman"] = state
            }

            elevator_checkbox.setOnCheckedChangeListener { _, state ->
                others["elevator"] = state
            }

            laundry_room_checkbox.setOnCheckedChangeListener { _, state ->
                others["laundry_room"] = state
            }

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