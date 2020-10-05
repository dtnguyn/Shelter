package com.nguyen.shelter.ui.community.viewmodels

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.model.PhotoUri
import com.nguyen.shelter.repo.BlogRepository
import com.nguyen.shelter.ui.community.fragments.BlogActionBottomFragment

class BlogViewModel
@ViewModelInject
constructor(
    private val blogRepository: BlogRepository
): ViewModel() {

    companion object {
        const val NEW_YORK_CITY = "10001"
    }

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    private val _blogs: MutableLiveData<ArrayList<Blog>> = MutableLiveData()
    private val _addImages: MutableLiveData<ArrayList<PhotoUri>> = MutableLiveData(arrayListOf())
    private val _postalCode: MutableLiveData<String> = MutableLiveData(NEW_YORK_CITY)
    private val _currentUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val _isOwner: MutableLiveData<Boolean> = MutableLiveData()
    private val _currentFocusBlog: MutableLiveData<Blog> = MutableLiveData()
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)


    val errorMessage: LiveData<String> = Transformations.map(_errorMessage){
        it
    }

    val blogs: LiveData<ArrayList<Blog>> = Transformations.map(_blogs){
        it
    }

    val addImages: LiveData<List<PhotoUri>> = Transformations.map(_addImages){
        it
    }

    val postalCode: LiveData<String> = Transformations.map(_postalCode){
        it
    }

    val currentUser: LiveData<FirebaseUser> = Transformations.map(_currentUser){
        it
    }

    val isOwner: LiveData<Boolean> = Transformations.map(_isOwner){
        it
    }

    val currentFocusBlog: LiveData<Blog> = Transformations.map(_currentFocusBlog){
        it
    }

    val isLoading: LiveData<Boolean> = Transformations.map(_isLoading){
        it
    }

    fun setStateEvent(event: MainStateEvent){

        when(event){

            is MainStateEvent.CheckAuthentication -> {
                blogRepository.checkAuthentication {response ->

                    if(response.status){
                        println("debug: authentication ${response.data}")
                        _currentUser.value = response.data!!
                    }
                }
            }

            is MainStateEvent.AddBlog -> {
                _isLoading.value = true
                blogRepository.addBlog(event.blogContent, _postalCode.value!!, _addImages.value!!){response ->
                    _isLoading.value = false
                    _addImages.value?.clear()
                    if(!response.status) _errorMessage.value = response.message
                    else setStateEvent(MainStateEvent.GetBlogs)
                }
            }

            is MainStateEvent.GetBlogs -> {
                _isLoading.value = true
                blogRepository.getBlogs(_postalCode.value!!){response ->
                    if(response.status) {
                        println("debug: blogs size: ${response.data?.size}")
                        _blogs.value?.clear()
                        _blogs.value = response.data as ArrayList<Blog>
                        _isLoading.value = false
                    } else _errorMessage.value = response.message
                }
            }

            is MainStateEvent.EditBlog -> {
                _isLoading.value = true
                _currentFocusBlog.value?.let {blog ->
                    blogRepository.editBlog(blog, event.newContent, _addImages.value!!){response ->
                        _isLoading.value = false
                        _addImages.value?.clear()
                        if(!response.status) _errorMessage.value = response.message
                        else setStateEvent(MainStateEvent.GetBlogs)
                    }
                }
            }

            is MainStateEvent.DeleteBlog -> {
                _isLoading.value = true
                _currentFocusBlog.value?.let {
                    blogRepository.deleteBlog(it.id){response ->
                        _isLoading.value = false
                        if(!response.status) _errorMessage.value = response.message
                        else setStateEvent(MainStateEvent.GetBlogs)
                    }
                }

            }

            is MainStateEvent.AddImage -> {
                val list = _addImages.value!!
                for(image in event.newImages){
                    list.add(PhotoUri(image, false))
                }
                _addImages.value = list
            }

            is MainStateEvent.ReplaceAddImages -> {
                currentFocusBlog.value?.let {blog ->
                    val list = _addImages.value!!
                    list.clear()
                    for(photo in blog.photos){
                        list.add(PhotoUri(Uri.parse(photo.url),true, photo.url))
                    }
                    _addImages.value = list
                }
            }

            is MainStateEvent.DeleteImage -> {
                val list = _addImages.value!!
                list.removeAt(event.imagePosition)
                _addImages.value = list
            }

            is MainStateEvent.SetPostalCode -> {
                _postalCode.value = event.postalCode

            }

            is MainStateEvent.IsBlogOwner -> {
                println("debug: IsBlogOwner called")
                _currentUser.value?.let {user ->
                    println("debug: IsBlogOwner ${user.uid == event.userId} ${user.uid} ${event.userId}")
                    _isOwner.value = user.uid == event.userId
                }
            }

            is MainStateEvent.SetFocusBlog -> {
                _currentFocusBlog.value = event.blog
            }
        }
    }


}



sealed class MainStateEvent{

    object CheckAuthentication: MainStateEvent()

    class AddBlog(val blogContent: String): MainStateEvent()
    class EditBlog(val newContent: String) : MainStateEvent()
    object DeleteBlog : MainStateEvent()
    object GetBlogs: MainStateEvent()

    class AddImage(val newImages: List<Uri>): MainStateEvent()
    object ReplaceAddImages : MainStateEvent()
    class DeleteImage(val imagePosition: Int): MainStateEvent()


    class SetPostalCode(val postalCode: String): MainStateEvent()
    class IsBlogOwner(val userId: String): MainStateEvent()
    class SetFocusBlog(val blog: Blog) : MainStateEvent()



}