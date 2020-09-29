package com.nguyen.shelter.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyen.shelter.api.mapper.BlogFirebaseMapper
import com.nguyen.shelter.api.response.FloorPlan
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.model.CallbackResponse

class BlogRepository(
    private val blogMapper: BlogFirebaseMapper,
    private val db: FirebaseFirestore
) {


    fun addBlog(blog: Blog, callback: (CallbackResponse<Unit>) -> Unit){

        val blogMap = blogMapper.mapToEntity(blog)

        db.collection("blogs").document(blog.id)
            .set(blogMap)
            .addOnSuccessListener {
                callback.invoke(CallbackResponse(true, "Add post successfully.", null))
            }
            .addOnFailureListener {
                callback.invoke(CallbackResponse(false, "Error when adding post: ${it.message}", null))
            }

    }


    fun getBlogs(postalCode: String, callback: (CallbackResponse<List<Blog>>) -> Unit) {

        val collectionRef = db.collection("blogs")
        val blogs = ArrayList<Blog>()


        collectionRef
            .whereEqualTo("postal_code", postalCode)
            .get()
            .addOnSuccessListener {documents ->
                for(document in documents){
                    blogs.add(blogMapper.mapFromEntity(document.data as HashMap<String, Any>))
                }
                callback.invoke(CallbackResponse(true, "", blogs))
            }.addOnFailureListener {
                callback.invoke(CallbackResponse(false, "Error when getting posts: ${it.message}", null))
            }

    }

    fun editBlog(blog: Blog, callback: (CallbackResponse<Unit>) -> Unit){
        val blogMap = blogMapper.mapToEntity(blog)

        db.collection("blogs").document(blog.id)
            .set(blogMap)
            .addOnSuccessListener {
                callback.invoke(CallbackResponse(true, "Edit post successfully.", null))
            }
            .addOnFailureListener {
                callback.invoke(CallbackResponse(false, "Error when editing post: ${it.message}", null))
            }
    }


    fun deleteBlog(id: String, callback: (CallbackResponse<Unit>) -> Unit){
        db.collection("blogs").document(id)
            .delete()
            .addOnSuccessListener {
                callback.invoke(CallbackResponse(true, "Delete post successfully.", null))
            }
            .addOnFailureListener {
                callback.invoke(CallbackResponse(false, "Error when deleting post: ${it.message}", null))
            }
    }


}