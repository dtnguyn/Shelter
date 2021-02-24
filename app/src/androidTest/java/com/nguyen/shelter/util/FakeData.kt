package com.nguyen.shelter.util

import com.nguyen.shelter.model.Blog
import com.nguyen.shelter.model.User
import java.util.*

object FakeData {

    const val fakeString =  "HELLO THIS IS A TEST STRING"

    val fakeBlogs = arrayListOf(
        Blog(
            id = "1",
            userId = "123",
            user = User(name = "Test User", email = "testemail@email.com"),
            content = "This is a test blog post 1",
            date = Date(),
            notificationToken = ""
        ),
        Blog(
            id = "2",
            userId = "123",
            user = User(name = "Test User", email = "testemail@email.com"),
            content = "This is a test blog post 2",
            date = Date(),
            notificationToken = ""
        ),
        Blog(
            id = "3",
            userId = "123",
            user = User(name = "Test User", email = "testemail@email.com"),
            content = "This is a test blog post 3",
            date = Date(),
            notificationToken = ""
        ),
        Blog(
            id = "4",
            userId = "123",
            user = User(name = "Test User", email = "testemail@email.com"),
            content = "This is a test blog post 4",
            date = Date(),
            notificationToken = ""
        ),
        Blog(
            id = "5",
            userId = "123",
            user = User(name = "Test User", email = "testemail@email.com"),
            content = "This is a test blog post 5",
            date = Date(),
            notificationToken = ""
        ),
        Blog(
            id = "6",
            userId = "123",
            user = User(name = "Test User", email = "testemail@email.com"),
            content = "This is a test blog post 6",
            date = Date(),
            notificationToken = ""
        ),
    )
}