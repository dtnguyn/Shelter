package com.nguyen.shelter.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.transition.TransitionInflater
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.databinding.FragmentDetailBinding
import com.nguyen.shelter.ui.main.MainActivity
import com.nguyen.shelter.ui.main.adapters.ImageSliderAdapter
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


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
        photosUrl?.let {
            adapter = ImageSliderAdapter(arrayListOf(Photo(it)))
            imageSlider.setSliderAdapter(adapter)
        }




        subscribeObservers()

        val id = arguments?.getString("id")
        id?.let {
            viewModel.setStateEvent(MainStateEvent.GetPropertyDetail(id))
        }




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("id")
        view.scrollView.transitionName = id!!
    }

    private fun subscribeObservers(){
        viewModel.rentPropertyDetail.observe(viewLifecycleOwner, {propDetail ->

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
            }

            adapter.addImages(propDetail.photos)

            val features = propDetail.features


            features.apply {
                if(bedsMax != null && bedsMin != null){
                    val beds: String? = if(bedsMax!! > bedsMin!!) "" + bedsMin!!.toInt() + "-" + bedsMax!!.toInt()
                    else bedsMin!!.toInt().toString()
                    binding.beds = beds
                } else binding.beds = "N/A"

                if(bathsMax != null && bathsMin != null){
                    val baths: String? = if(bathsMax!! > bathsMin!!) "" + bathsMin!!.toInt() + "-" + bathsMax!!.toInt()
                    else bathsMin!!.toInt().toString()
                    binding.baths = baths
                } else binding.baths = "N/A"

                if(areaMax != null && areaMin != null){
                    val area: String? =  if(areaMax!! > areaMin!!) "" + areaMin!!.toInt() + "-" + areaMax!!.toInt()
                    else areaMin!!.toInt().toString()
                    binding.sqft = area
                } else binding.sqft = "N/A"

                if(priceMax != null && priceMin != null){
                    val price: String? =  if(priceMax!! > priceMin!!) "$" + priceMin!!.toInt() + "-$" + priceMax!!.toInt()
                    else priceMin!!.toInt().toString()
                    binding.price = price
                } else binding.price = "N/A"
            }
        })
    }


}