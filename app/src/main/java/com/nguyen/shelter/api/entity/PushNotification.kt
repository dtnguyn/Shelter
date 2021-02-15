package com.nguyen.shelter.api.entity

data class PushNotification(
    val data: NotificationData,
    val to: String
)