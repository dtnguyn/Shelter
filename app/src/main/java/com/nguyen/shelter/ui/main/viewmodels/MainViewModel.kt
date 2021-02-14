package com.nguyen.shelter.ui.main.viewmodels

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseUser
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.model.*
import com.nguyen.shelter.repo.MainRepository
import com.nguyen.shelter.repo.MainRepository.Companion.RENT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel
@ViewModelInject
constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    private val _rentPropertyPageData: MutableLiveData<PagingData<PropertyCacheEntity>> = MutableLiveData()
    private val _salePropertyPageData: MutableLiveData<PagingData<PropertyCacheEntity>> = MutableLiveData()
    private val _rentPropertyFilter: MutableLiveData<PropertyFilter> = MutableLiveData()
    private val _salePropertyFilter: MutableLiveData<PropertyFilter> = MutableLiveData()
    private val _rentPropertyDetail: MutableLiveData<PropertyDetail> = MutableLiveData()
    private val _savedProperties: MutableLiveData<HashMap<String, Property?>> = MutableLiveData()
    private val _userBlogs: MutableLiveData<ArrayList<Blog>> = MutableLiveData()

    private val _rentLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _saleLoading: MutableLiveData<Boolean> = MutableLiveData(false)


    private val _currentUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()

    private var firstRentCall = true
    private var rentFilterChange = false
    private var firstSaleCall = true
    private var saleFilterChange = false
    private var networkAvailable = false

    val rentLoading: LiveData<Boolean> = Transformations.map(_rentLoading){
        it
    }

    val saleLoading: LiveData<Boolean> = Transformations.map(_saleLoading){
        it
    }

    val rentPropertyPageData: LiveData<PagingData<PropertyCacheEntity>> = Transformations.map(_rentPropertyPageData){
        it
    }

    val salePropertyPageData: LiveData<PagingData<PropertyCacheEntity>> = Transformations.map(_salePropertyPageData){
        it
    }

    val rentPropertyFilter: LiveData<PropertyFilter> = Transformations.map(_rentPropertyFilter){
        it
    }

    val salePropertyFilter: LiveData<PropertyFilter> = Transformations.map(_salePropertyFilter){
        it
    }

    val currentUser: LiveData<FirebaseUser> = Transformations.map(_currentUser){
        it
    }

    val error: LiveData<String> = Transformations.map(_error){
        it
    }

    val rentPropertyDetail: LiveData<PropertyDetail> = Transformations.map(_rentPropertyDetail){
        it
    }

    val savedProperties: LiveData<HashMap<String, Property?>> = Transformations.map(_savedProperties){
        it
    }

    val userBlogs: LiveData<ArrayList<Blog>> = Transformations.map(_userBlogs){
        it
    }

    @ExperimentalPagingApi
    fun setStateEvent(mainStateEvent: MainStateEvent){
        println("debug: setting state")
        viewModelScope.launch {
            when(mainStateEvent){
                is MainStateEvent.InitializeLiveData -> {
                    _rentPropertyFilter.value = PropertyFilter()
                }

                is MainStateEvent.CheckAuthentication -> {
                    mainRepository.checkAuthentication{response ->
                        _currentUser.value = response.data
                    }
                }

                is MainStateEvent.FacebookAuthenticate -> {
                    println("debug: logging in with facebook")
                    mainRepository.loginWithFacebook(mainStateEvent.token, mainStateEvent.activity){response ->
                        if(response.status){
                            _currentUser.value = response.data
                        } else {
                            _error.value = response.message
                        }
                    }
                }

                is MainStateEvent.GoogleAuthenticate -> {
                    mainRepository.loginWithGoogle(mainStateEvent.token, mainStateEvent.activity) {response ->
                        if(response.status){
                            _currentUser.value = response.data
                        } else {
                            _error.value = response.message
                        }
                    }
                }

                is MainStateEvent.Logout -> {
                    mainRepository.logout {response ->
                        handleCallback(response) {
                            _currentUser.value = response.data
                        }
                    }
                }

                is MainStateEvent.GetRentPropertyList -> {
                    if(!rentFilterChange && !firstRentCall) return@launch
                    println("DebugApp: Getting rent property list")
                    _rentLoading.value = true
                    mainRepository.getRentPropertyList(mainStateEvent.propertyFilter)
                        .cachedIn(viewModelScope)
                        .onEach {
                            println("DebugApp: emit paging rent data")
                            firstRentCall = false
                            _rentPropertyPageData.value = it
                            if(!rentFilterChange || !networkAvailable) _rentLoading.value = false
                            else rentFilterChange = false
                        }
                        .catch {

                        }
                        .launchIn(viewModelScope)


                }

                is MainStateEvent.GetSalePropertyList -> {
                    if(!saleFilterChange && !firstSaleCall) return@launch
                    println("DebugApp: Getting sale property list")
                    _saleLoading.value = true
                    mainRepository.getSalePropertyList(mainStateEvent.propertyFilter)
                        .cachedIn(viewModelScope)
                        .onEach {
                            println("DebugApp: emit paging sale data")
                            firstSaleCall = false
                            _salePropertyPageData.value = it
                            if(!saleFilterChange || !networkAvailable) _saleLoading.value = false
                            else saleFilterChange = false
                        }
                        .catch {

                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.GetPropertyDetail -> {
                    CoroutineScope(IO).launch {
                        val detail = mainRepository.getPropertyDetail(mainStateEvent.id)

                        withContext(Main){
                            if(detail != null) _rentPropertyDetail.value = detail
                            else _error.value = "Can't get property detail!"
                        }
                    }

                }

                is MainStateEvent.SaveRentPropertyFilter -> {
                    mainRepository.saveRentFilter(mainStateEvent.propertyFilter)
                }

                is MainStateEvent.SaveSalePropertyFilter -> {
                    mainRepository.saveSaleFilter(mainStateEvent.propertyFilter)
                }

                is MainStateEvent.GetPropertyFilter -> {
                    viewModelScope.launch(IO) {
                        val filter = mainRepository.getFilter(mainStateEvent.type)

                        withContext(Main){
                            if(mainStateEvent.type == RENT && _rentPropertyFilter.value != filter){
                                rentFilterChange = true
                                _rentPropertyFilter.value = filter
                            }
                            else if(_salePropertyFilter.value != filter) {
                                saleFilterChange = true
                                _salePropertyFilter.value = filter
                            }
                        }

                    }

                }

                is MainStateEvent.GetSavedProperties -> {
                    mainRepository.getSavedProperties {response ->
                        if(response.status) _savedProperties.value = response.data
                        else _error.value = response.message
                    }
                }

                is MainStateEvent.UpdatePropertySaveStatus -> {
                    _savedProperties.value?.let{savedList ->
//                        it[mainStateEvent.propertyId] = it[mainStateEvent.propertyId]?.not() ?: true
//                        mainRepository.updatePropertySaveStatus(it, mainStateEvent.property){response ->
//                            if(!response.status) _error.value = response.message
//                        }
//
//                        _savedProperties.value = it

                        savedList[mainStateEvent.propertyId] = if(savedList[mainStateEvent.propertyId] == null) mainStateEvent.property else null
                        _savedProperties.value = savedList
                        mainRepository.updatePropertySaveStatus(savedList, mainStateEvent.property){

                        }
                    }
                }

                is MainStateEvent.UpdateNetworkAvailable -> {
                    networkAvailable = mainStateEvent.status
                }


            }
        }
    }


    private fun <T> handleCallback(response: CallbackResponse<T>, successAction: () -> Unit){
        if(response.status){
            successAction.invoke()
        } else {
            _error.value = response.message
        }
    }

}




sealed class MainStateEvent{

    object InitializeLiveData: MainStateEvent()

    // Authentication
    object CheckAuthentication: MainStateEvent()
    object Logout: MainStateEvent()
    class FacebookAuthenticate(val token: AccessToken, val activity: Activity): MainStateEvent()
    class GoogleAuthenticate(val token: String, val activity: Activity): MainStateEvent()


    // Get properties from API
    class GetRentPropertyList(val propertyFilter: PropertyFilter): MainStateEvent()
    class GetSalePropertyList(val propertyFilter: PropertyFilter): MainStateEvent()
    class GetPropertyDetail(val id: String): MainStateEvent()
    class SaveRentPropertyFilter(val propertyFilter: PropertyFilter): MainStateEvent()
    class SaveSalePropertyFilter(val propertyFilter: PropertyFilter): MainStateEvent()
    class GetPropertyFilter(val type: String): MainStateEvent()

    //Others
    object GetSavedProperties: MainStateEvent()
    class UpdatePropertySaveStatus(val propertyId: String, val property: Property): MainStateEvent()
    class UpdateNetworkAvailable(val status: Boolean): MainStateEvent()

}
