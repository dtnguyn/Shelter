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

class CommentFirebaseMapper
@Inject
constructor(
    private val gson: Gson
) : EntityMapper<HashMap<String, String>, Comment>{


    override fun mapFromEntity(entity: HashMap<String, String>): Comment {
        return Comment(
            id = entity["id"] ?: "",
            blogId = entity["blog_id"] ?: "",
            date = gson.fromJson(entity["date"], Date::class.java),
            user = gson.fromJson(entity["user"], User::class.java),
            content = entity["content"] ?: ""
        )
    }

    override fun mapToEntity(domainModel: Comment): HashMap<String, String> {
        return hashMapOf(
            "id" to domainModel.id,
            "blog_id" to domainModel.blogId,
            "date" to gson.toJson(domainModel.date),
            "user" to gson.toJson(domainModel.user),
            "content" to domainModel.content
        )

    }


}