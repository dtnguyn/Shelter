package com.nguyen.shelter.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.nguyen.shelter.api.mapper.BlogFirebaseMapper
import com.nguyen.shelter.api.mapper.CommentFirebaseMapper
import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.model.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BlogRepository(
    private val blogMapper: BlogFirebaseMapper,
    private val commentMapper: CommentFirebaseMapper,
    private val db: FirebaseFirestore,
    private val storageRef: StorageReference,
    private val auth: FirebaseAuth
) {


    fun addBlog(blogContent: String, postalCode: String, images: List<PhotoUri>, callback: (CallbackResponse<Blog>) -> Unit){
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
                    postalCode = postalCode,
                    isOwner = true
                )
                val blogMap = blogMapper.mapToEntity(blog)
                db.collection("blogs").document(blog.id)
                    .set(blogMap)
                    .addOnSuccessListener {
                        callback.invoke(CallbackResponse(true, "Add post successfully.", blog))
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

        println("debug: postal code: $postalCode")
        collectionRef
            .whereEqualTo("postal_code", postalCode)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {documents ->
                println("debug: document size: ${documents.size()}")
                for(document in documents){
                    val blog = blogMapper.mapFromEntity(document.data as HashMap<String, Any>)
                    auth.currentUser?.let { user ->
                        blog.isLiked = blog.likeUsers[user.uid] == true
                        blog.isOwner = blog.userId == auth.currentUser?.uid
                        if(blog.removeUsers[user.uid] != true) blogs.add(blog)
                    } ?: blogs.add(blog)
                }
                callback.invoke(CallbackResponse(true, "", blogs))
            }.addOnFailureListener {
                println("debug: ${it.message}")
                callback.invoke(CallbackResponse(false, "Error when getting posts: ${it.message}", null))
            }

    }

    fun editBlog(oldBlog: Blog, newContent: String, newImages: List<PhotoUri>, callback: (CallbackResponse<Blog>) -> Unit){
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
                        callback.invoke(CallbackResponse(true, "Edit post successfully.", editedBlog))
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

    fun removeBlog(blog: Blog, callback: (CallbackResponse<Unit>) -> Unit){
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }
        blog.removeUsers[auth.currentUser!!.uid] = true
        val blogMap = blogMapper.mapToEntity(blog)
        db.collection("blogs").document(blog.id)
            .set(blogMap)
            .addOnSuccessListener {
                callback.invoke(CallbackResponse(true, "Edit post successfully.", null))
            }
            .addOnFailureListener {e ->
                callback.invoke(CallbackResponse(false, "Error when editing post: ${e.message}", null))
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

    fun reportBlog(reportContent: String, blogId: String, callback: (CallbackResponse<Unit>) -> Unit) {

        val reportMap = hashMapOf(
            "blog_id" to blogId,
            "report_content" to reportContent
        )

        db.collection("reports").document(blogId)
            .set(reportMap)
            .addOnSuccessListener {
                println("debug: report success")
                callback.invoke(CallbackResponse(true, "Report post successfully.", null))
            }
            .addOnFailureListener {
                println("debug: report fail ${it.message}")
                callback.invoke(CallbackResponse(false, "Error when reporting post: ${it.message}", null))
            }
    }

    fun likeBlog(blog: Blog, callback: (CallbackResponse<Unit>) -> Unit){
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }

        val userId = auth.currentUser!!.uid


        blog.likeUsers[userId] = blog.likeUsers[userId] != true
        blog.isLiked = blog.likeUsers[userId] == true
        blog.likeCounter = if (blog.likeUsers[userId] == true) blog.likeCounter + 1 else blog.likeCounter - 1

        val blogMap = blogMapper.mapToEntity(blog)

        db.collection("blogs").document(blog.id)
            .set(blogMap)
            .addOnSuccessListener {
                callback.invoke(CallbackResponse(true, "Like post successfully.", null))
            }
            .addOnFailureListener {e ->
                callback.invoke(CallbackResponse(false, "Error when liking post: ${e.message}", null))
            }
    }

    fun getComments(blogId: String, callback: (CallbackResponse<ArrayList<Comment>>) -> Unit){

        val collectionRef = db.collection("comments")
        val comments = ArrayList<Comment>()

        collectionRef
            .whereEqualTo("blog_id", blogId)
            .get()
            .addOnSuccessListener {documents ->
                for(document in documents){
                    val comment = commentMapper.mapFromEntity(document.data as HashMap<String, String>)
                    auth.currentUser?.let { user ->
                        if(user.uid == comment.user.id) comment.isOwner = true
                    }
                    comments.add(comment)
                }
                callback.invoke(CallbackResponse(true, "Get comments successfully", comments))
            }.addOnFailureListener {
                callback.invoke(CallbackResponse(false, "Error when getting comments: ${it.message}", null))
            }

    }


    fun addComment(blog: Blog, content: String, callback: (CallbackResponse<Comment>) -> Unit){
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }

        val comment = Comment(
            id = UUID.randomUUID().toString(),
            blogId= blog.id,
            content = content,
            user = User(
                id = auth.currentUser!!.uid,
                avatar = auth.currentUser!!.photoUrl.toString(),
                name = auth.currentUser!!.displayName ?: "Unknown",
                email = auth.currentUser!!.email ?: "Unknown"
            ),
            date = Date(),
            isOwner = true
        )

        blog.commentCounter += 1

        val blogMap = blogMapper.mapToEntity(blog)
        val commentMap = commentMapper.mapToEntity(comment)

        val blogRef = db.collection("blogs").document(blog.id)
        val commentRef = db.collection("comments").document(comment.id)

        db.runBatch { batch ->
            // Update the blog
            batch.set(blogRef, blogMap)
            // Update the comment
            batch.set(commentRef, commentMap)

        }.addOnCompleteListener {
            callback.invoke(CallbackResponse(true, "Add comment successfully.", comment))
        }.addOnFailureListener {e ->
            callback.invoke(CallbackResponse(false, "Error when adding comment: ${e.message}", null))
        }
    }

    fun deleteComment(blog: Blog, id: String, callback: (CallbackResponse<String>) -> Unit){
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }

        blog.commentCounter -= 1
        val blogMap = blogMapper.mapToEntity(blog)


        val blogRef = db.collection("blogs").document(blog.id)
        val commentRef = db.collection("comments").document(id)

        db.runBatch { batch ->
            // Update the blog
            batch.set(blogRef, blogMap)
            // Update the comment
            batch.delete(commentRef)

        }.addOnCompleteListener {
            callback.invoke(CallbackResponse(true, "Delete comment successfully.", id))
        }.addOnFailureListener {e ->
            callback.invoke(CallbackResponse(false, "Error when deleting comment: ${e.message}", null))
        }
    }

    fun getUserBlogs(callback: (response: CallbackResponse<ArrayList<Blog>>) -> Unit) {
        if(auth.currentUser == null) {
            callback.invoke(CallbackResponse(false, "User haven't logged in.", null))
            return
        }
        val collectionRef = db.collection("blogs")
        val blogs = ArrayList<Blog>()

        collectionRef
            .whereEqualTo("user_id", auth.currentUser!!.uid)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {documents ->
                for(document in documents){
                    val blog = blogMapper.mapFromEntity(document.data as HashMap<String, Any>)
                    auth.currentUser?.let { user ->
                        blog.isLiked = blog.likeUsers[user.uid] == true
                        blog.isOwner = blog.userId == auth.currentUser?.uid
                        if(blog.removeUsers[user.uid] != true) blogs.add(blog)
                    } ?: blogs.add(blog)
                }
                callback.invoke(CallbackResponse(true, "", blogs))
            }.addOnFailureListener {
                println("debug: ${it.message}")
                callback.invoke(CallbackResponse(false, "Error when getting posts: ${it.message}", null))
            }
    }

}