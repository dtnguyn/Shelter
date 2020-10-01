package com.nguyen.shelter.ui.community.viewmodels

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.repo.BlogRepository

class BlogViewModel
@ViewModelInject
constructor(
    private val blogRepository: BlogRepository
): ViewModel() {

    companion object {
        const val NEW_YORK_CITY = "100001"
    }

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    private val _blogs: MutableLiveData<List<Blog>> = MutableLiveData()
    private val _addImages: MutableLiveData<ArrayList<Uri>> = MutableLiveData(arrayListOf())
    private val _postalCode: MutableLiveData<String> = MutableLiveData(NEW_YORK_CITY)


    val errorMessage: LiveData<String> = Transformations.map(_errorMessage){
        it
    }

    val blogs: LiveData<List<Blog>> = Transformations.map(_blogs){
        it
    }

    val addImages: LiveData<List<Uri>> = Transformations.map(_addImages){
        it
    }

    val postalCode: LiveData<String> = Transformations.map(_postalCode){
        it
    }


    fun setStateEvent(event: MainStateEvent){

        when(event){

            is MainStateEvent.AddBlog -> {
                blogRepository.addBlog(event.blogContent, _postalCode.value!!, _addImages.value!!){response ->
                    if(!response.status) _errorMessage.value = response.message
                }
            }

            is MainStateEvent.GetBlogs -> {
                blogRepository.getBlogs(event.postalCode){response ->
                    if(response.status) {
                        _blogs.value = response.data
                    } else _errorMessage.value = response.message
                }
            }

            is MainStateEvent.EditBlog -> {
                blogRepository.editBlog(event.blog){response ->
                    if(!response.status) _errorMessage.value = response.message
                }
            }

            is MainStateEvent.DeleteBlog -> {
                blogRepository.deleteBlog(event.id){response ->
                    if(!response.status) _errorMessage.value = response.message
                }
            }

            is MainStateEvent.AddImage -> {
                val list = _addImages.value!!
                for(image in event.newImages){
                    list.add(image)
                }
                _addImages.value = list
            }

            is MainStateEvent.DeleteImage -> {
                val list = _addImages.value!!
                list.removeAt(event.imagePosition)
                _addImages.value = list
            }

            is MainStateEvent.SetPostalCode -> {
                _postalCode.value = event.postalCode

            }

        }


    }


}



sealed class MainStateEvent{

    class AddBlog(val blogContent: String): MainStateEvent()
    class EditBlog(val blog: Blog): MainStateEvent()
    class DeleteBlog(val id: String): MainStateEvent()
    class GetBlogs(val postalCode: String): MainStateEvent()

    class AddImage(val newImages: List<Uri>): MainStateEvent()
    class DeleteImage(val imagePosition: Int): MainStateEvent()


    class SetPostalCode(val postalCode: String): MainStateEvent()


}