package com.nguyen.shelter.ui.main.viewmodels

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseUser
import com.nguyen.shelter.db.entity.PropertyCacheEntity
import com.nguyen.shelter.model.CallbackResponse
import com.nguyen.shelter.model.PropertyFilter
import com.nguyen.shelter.repo.MainRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel
@ViewModelInject
constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    private val _rentPropertyPageData: MutableLiveData<PagingData<PropertyCacheEntity>> = MutableLiveData()
    private val _salePropertyPageData: MutableLiveData<PagingData<PropertyCacheEntity>> = MutableLiveData()
    private val _rentPropertyFilter: MutableLiveData<PropertyFilter> = MutableLiveData(PropertyFilter())
    private val _salePropertyFilter: MutableLiveData<PropertyFilter> = MutableLiveData(PropertyFilter())
    private val _currentUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val _error: MutableLiveData<String> = MutableLiveData()

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
                    println("debug: GetRentPropertyList state ${rentPropertyFilter.value}")
                    mainRepository.getRentPropertyList(_rentPropertyFilter.value!!)
                        .onEach {
                            println("debug: Success")
                            _rentPropertyPageData.value = it
                        }
                        .catch {
                            println("debug: Error")
                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.GetSalePropertyList -> {
                    mainRepository.getSalePropertyList(_salePropertyFilter.value!!)
                        .onEach {
                            _salePropertyPageData.value = it
                        }
                        .catch {

                        }
                        .launchIn(viewModelScope)
                }

                is MainStateEvent.GetPropertyDetail -> {
                    mainRepository.getPropertyDetail(mainStateEvent.id){
                        println("debug: Getting detail $it")
                    }
                }

                is MainStateEvent.SaveRentFilter -> {
                    _rentPropertyFilter.value = mainStateEvent.filter
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

    // Get properties from API
    object GetRentPropertyList: MainStateEvent()
    object GetSalePropertyList: MainStateEvent()
    class GetPropertyDetail(val id: String): MainStateEvent()


    class SaveRentFilter(val filter: PropertyFilter): MainStateEvent()

    // Authentication
    object CheckAuthentication: MainStateEvent()
    object Logout: MainStateEvent()
    class FacebookAuthenticate(val token: AccessToken, val activity: Activity): MainStateEvent()
    class GoogleAuthenticate(val token: String, val activity: Activity): MainStateEvent()



}