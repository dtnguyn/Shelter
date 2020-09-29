package com.nguyen.shelter.model

import java.util.*

data class Comment (
    var id: String,
    var user: User,
    var date: Date,
    var content: String
)
