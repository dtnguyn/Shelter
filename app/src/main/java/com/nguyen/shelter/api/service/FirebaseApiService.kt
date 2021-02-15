package com.nguyen.shelter.api.service

import com.nguyen.shelter.api.entity.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val SERVER_KEY = "AAAAOWbNB2k:APA91bGJGj0MleSnq3ZlZp9cCm9lVcmntsGKHxEjWfWliE5A4EeRGVlOkHTFFE8ctFPj1QiaUJJr6PyLea2YEnByQbgNOStSyvQLWKdvWDjKDq_FXEd72WlnsezPHuh1gegdJ-KBBS-1"
private const val CONTENT_TYPE = "application/json"

interface FirebaseApiService {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}