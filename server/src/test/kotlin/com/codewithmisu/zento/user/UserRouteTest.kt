package com.codewithmisu.zento.user

import com.codewithmisu.zento.module
import com.codewithmisu.zento.profile.UserProfile
import com.codewithmisu.zento.profile.UserRole
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRouteTest {

    val userProfile = UserProfile(
        role = UserRole.ServiceProvider,
        email = "john.doe@example.com",
        password = "password123",
        firstName = "John",
        lastName = "Doe",
        zipcode = "12345",
    )

    @Test
    fun testGetUser() = testApplication {
        application {
            module()
        }

        val userService = mockk<UserService>()
        every { userService.findUserBy(any()) } returns userProfile

        val response = client.get("/user/1")
        assertEquals(HttpStatusCode.OK, response.status)

        val profile = response.body<UserProfile>()
        assertEquals(userProfile.email, profile.email)
    }
}