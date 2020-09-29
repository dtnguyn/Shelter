package com.nguyen.shelter.model

import com.nguyen.shelter.api.response.Photo
import java.util.*

data class Blog (
    var id: String,
    var userId: String,
    var user: User,
    var date: Date,
    var content: String,
    var likeCounter: Int,
    var commentCounter: Int,
    var photos: List<Photo>,
    var comments: List<Comment>,
    var likeUsers: HashMap<String, Boolean>,
    var postalCode: String
)