package com.nguyen.shelter.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.nguyen.shelter.api.mapper.BlogFirebaseMapper
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.model.CallbackResponse
import com.nguyen.shelter.model.PhotoUri
import com.nguyen.shelter.model.User
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BlogRepository(
    private val blogMapper: BlogFirebaseMapper,
    private val db: FirebaseFirestore,
    private val storageRef: StorageReference,
    private val auth: FirebaseAuth
) {


    fun addBlog(blogContent: String, postalCode: String, images: List<PhotoUri>, callback: (CallbackResponse<Unit>) -> Unit){
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }

        println("debug: prepare adding images: ${images.size}")
        addImagesToStorage(images){
            println("debug: added images: ${it.data?.size}")
            if(it.status){
                val blog = Blog(
                    id = UUID.randomUUID().toString(),
                    userId = auth.currentUser!!.uid,
                    user = User(
                        id = auth.currentUser!!.uid,
                        avatar = auth.currentUser!!.photoUrl.toString(),
                        name = auth.currentUser!!.displayName ?: "Unknown",
                        email = auth.currentUser!!.email ?: "Unknown"
                    ),
                    date = Date(),
                    content = blogContent,
                    photos = it.data!!,
                    postalCode = postalCode
                )
                val blogMap = blogMapper.mapToEntity(blog)
                db.collection("blogs").document(blog.id)
                    .set(blogMap)
                    .addOnSuccessListener {
                        callback.invoke(CallbackResponse(true, "Add post successfully.", null))
                    }
                    .addOnFailureListener {
                        callback.invoke(CallbackResponse(false, "Error when adding post: ${it.message}", null))
                    }
            } else {
                callback.invoke(CallbackResponse(it.status, it.message, null))
            }
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

    fun editBlog(oldBlog: Blog, newContent: String, newImages: List<PhotoUri>, callback: (CallbackResponse<Unit>) -> Unit){
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }

        println("debug: prepare adding images: ${newImages.size}")
        addImagesToStorage(newImages){
            println("debug: added images: ${it.data?.size}")
            if(it.status){
                val editedBlog = Blog(
                    id = oldBlog.id,
                    userId = auth.currentUser!!.uid,
                    user = User(
                        id = auth.currentUser!!.uid,
                        avatar = auth.currentUser!!.photoUrl.toString(),
                        name = auth.currentUser!!.displayName ?: "Unknown",
                        email = auth.currentUser!!.email ?: "Unknown"
                    ),
                    date = oldBlog.date,
                    content = newContent,
                    photos = it.data!!,
                    postalCode = oldBlog.postalCode
                )
                val blogMap = blogMapper.mapToEntity(editedBlog)
                db.collection("blogs").document(editedBlog.id)
                    .set(blogMap)
                    .addOnSuccessListener {
                        callback.invoke(CallbackResponse(true, "Edit post successfully.", null))
                    }
                    .addOnFailureListener {e ->
                        callback.invoke(CallbackResponse(false, "Error when editing post: ${e.message}", null))
                    }
            } else {
                callback.invoke(CallbackResponse(it.status, it.message, null))
            }
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

    private fun addImagesToStorage(images: List<PhotoUri>, callback: (CallbackResponse<List<Photo>>) -> Unit){

        val photos = ArrayList<Photo>()
        if(images.isEmpty()) callback.invoke(CallbackResponse(true, "", photos))
        for(image in images){
            if(image.isUploaded) {
                photos.add(Photo(image.url!!))
                if(photos.size == images.size) callback.invoke(CallbackResponse(true, "", photos))
                continue
            }
            val postImagesRef = storageRef.child("images/postImages/${UUID.randomUUID()}")
            val uploadTask = postImagesRef.putFile(image.uri)

            uploadTask
                .addOnSuccessListener {
                    println("debug: Add Success")
                }
                .addOnFailureListener{
                    println("debug: Add Fail ${it.message}")
                }
                .continueWithTask {
                    if (!it.isSuccessful) {
                        it.exception?.let {e ->
                            throw e
                        }
                    }
                    postImagesRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val downloadUrl = task.result!!.toString()
                        println("download url $downloadUrl")
                        photos.add(Photo(downloadUrl))
                        if(photos.size == images.size) callback.invoke(CallbackResponse(true, "", photos))
                    } else {println("debug: Can't add images.")
                        callback.invoke(CallbackResponse(false, "Can't add images.", null))
                    }
                }
        }
    }

    fun checkAuthentication(callback: (CallbackResponse<FirebaseUser?>) -> Unit) {
        if(auth.currentUser != null){
            callback.invoke(CallbackResponse(true, "User has logged in.", auth.currentUser!!))
        } else {
            callback.invoke(CallbackResponse(false, "User has logged in.", null))
        }
    }

}