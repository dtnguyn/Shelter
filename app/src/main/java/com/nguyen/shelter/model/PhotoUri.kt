package com.nguyen.shelter.model

import android.net.Uri

data class PhotoUri (
    var uri: Uri,
    var isUploaded: Boolean,
    var url: String? = null
)