package com.nguyen.shelter.ui.community.viewmodels

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

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    private val _blogs: MutableLiveData<List<Blog>> = MutableLiveData()


    val errorMessage: LiveData<String> = Transformations.map(_errorMessage){
        it
    }

    val blogs: LiveData<List<Blog>> = Transformations.map(_blogs){
        it
    }


    fun setStateEvent(event: MainStateEvent){

        when(event){

            is MainStateEvent.AddBlog -> {
                blogRepository.addBlog(event.blog){response ->
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

        }


    }


}



sealed class MainStateEvent{

    class AddBlog(val blog: Blog): MainStateEvent()
    class EditBlog(val blog: Blog): MainStateEvent()
    class DeleteBlog(val id: String): MainStateEvent()
    class GetBlogs(val postalCode: String): MainStateEvent()


}