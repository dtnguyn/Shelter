package com.nguyen.shelter.api.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nguyen.shelter.api.entity.PropertyNetworkEntity
import com.nguyen.shelter.api.response.*
import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.model.Comment
import com.nguyen.shelter.model.Property
import com.nguyen.shelter.model.User
import com.nguyen.shelter.util.EntityMapper
import java.sql.Timestamp
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class BlogFirebaseMapper
@Inject
constructor(
    private val gson: Gson
) : EntityMapper<HashMap<String, Any>, Blog>{
    override fun mapFromEntity(entity: HashMap<String, Any>): Blog {
        return Blog(
            id = entity["id"] as String,
            userId = entity["user_id"] as String,
            user = gson.fromJson(entity["user"] as String, User::class.java),
            date = gson.fromJson(entity["date_string"] as String, Date::class.java),
            content = entity["content"] as String,
            notificationToken = entity["notification_token"] as String,
            likeCounter = (entity["like_counter"] as Long).toInt(),
            commentCounter = (entity["comment_counter"] as Long).toInt(),
            photos = gson.fromJson(entity["photos"] as String? ?: "", object : TypeToken<List<Photo>>() {}.type),
            comments = gson.fromJson(entity["comments"] as String? ?: "", object : TypeToken<List<Comment>>() {}.type),
            postalCode = entity["postal_code"] as String,
            likeUsers = entity["liked_users"] as HashMap<String, Boolean>,
            removeUsers = entity["removed_users"] as HashMap<String, Boolean>
        )
    }

    override fun mapToEntity(domainModel: Blog): HashMap<String, Any> {
        return hashMapOf(
            "id" to domainModel.id,
            "user_id" to domainModel.userId,
            "user" to gson.toJson(domainModel.user),
            "date" to domainModel.date,
            "date_string" to gson.toJson(domainModel.date),
            "content" to domainModel.content,
            "notification_token" to domainModel.notificationToken,
            "like_counter" to domainModel.likeCounter,
            "comment_counter" to domainModel.commentCounter,
            "photos" to gson.toJson(domainModel.photos),
            "comments" to gson.toJson(domainModel.comments),
            "postal_code" to domainModel.postalCode,
            "liked_users" to domainModel.likeUsers,
            "removed_users" to domainModel.removeUsers
        )
    }


}