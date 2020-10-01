package com.nguyen.shelter.model

import com.nguyen.shelter.api.response.Photo
import java.util.*

data class Blog (
    var id: String,
    var userId: String = "",
    var user: User = User(),
    var date: Date,
    var content: String,
    var likeCounter: Int = 0,
    var commentCounter: Int = 0,
    var photos: List<Photo> = listOf(),
    var comments: List<Comment> = listOf(),
    var likeUsers: HashMap<String, Boolean> = hashMapOf(),
    var postalCode: String = ""
)