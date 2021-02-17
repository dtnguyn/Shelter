package com.nguyen.shelter.model

import com.nguyen.shelter.api.response.Photo
import com.nguyen.shelter.ui.community.viewmodels.MainStateEvent
import java.util.*

data class Blog (
    var id: String,
    var userId: String = "",
    var user: User = User(),
    var notificationToken: String,
    var date: Date,
    var content: String,
    var likeCounter: Int = 0,
    var commentCounter: Int = 0,
    var photos: List<Photo> = listOf(),
    var comments: List<Comment> = listOf(),
    var likeUsers: HashMap<String, Boolean> = hashMapOf(),
    var removeUsers: HashMap<String, Boolean> = hashMapOf(),
    var postalCode: String = "",
    var isLiked: Boolean = false,
    var isOwner: Boolean = false
)