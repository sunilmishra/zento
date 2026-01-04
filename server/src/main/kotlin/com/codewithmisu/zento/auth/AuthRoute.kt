package com.codewithmisu.zento.auth

import com.codewithmisu.zento.profile.UserProfile
import com.codewithmisu.zento.user.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoute(authService: AuthService, userService: UserService) {

    route("/auth") {

        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = userService.getUser(request.email)
            if (user == null) {
                call.respond(status = HttpStatusCode.NotFound, message = "User not found")
                return@post
            }

            if (!userService.verifyPassword(request.password, user.password)) {
                call.respond(status = HttpStatusCode.BadRequest, message = "Wrong credentials")
                return@post
            }

            val accessToken = authService.createAccessToken(user.id!!)
            val refreshToken = authService.createRefreshToken(user.id!!)
            call.respond(
                HttpStatusCode.OK,
                LoginResponse(accessToken, refreshToken)
            )
        }

        post("/signup") {
            val request = call.receive<SignupRequest>()
            val user = userService.getUser(request.email)
            if (user != null) {
                call.respond(status = HttpStatusCode.Conflict, message = "User already exists")
                return@post
            }
            val userProfile = UserProfile(
                role = request.role,
                email = request.email,
                password = request.password,
                firstName = request.firstName,
                lastName = request.lastName,
                zipcode = request.zipcode
            )
            val userId = userService.saveUser(userProfile)
            call.respond(
                HttpStatusCode.OK,
                SignupResponse(userId, "User created successfully")
            )
        }

        post("/forgotPassword") {
            val request = call.receive<ForgotPasswordRequest>()
            val user = userService.getUser(request.email)
            if (user == null) {
                call.respond(status = HttpStatusCode.NotFound, message = "User not found")
                return@post
            }
            call.respond(
                HttpStatusCode.OK,
                ForgotPasswordResponse("Password reset link sent to your email.")
            )
        }

        post("/resetPassword") {
            val request = call.receive<ResetPasswordRequest>()
            val user = userService.getUser(request.email)
            if (user == null) {
                call.respond(status = HttpStatusCode.NotFound, message = "User not found")
                return@post
            }

            val hashedPassword = userService.hashPassword(request.newPassword)
            val updatedUser = user.copy(password = hashedPassword)
            userService.updateUser(updatedUser)
            call.respond(
                HttpStatusCode.OK,
                ResetPasswordResponse("Password reset successfully.")
            )
        }

        post("/refresh") {
            val request = call.receive<RefreshTokenRequest>()
            val isValid = authService.verifyRefreshToken(request.userId, request.refreshToken)
            if(!isValid) {
                call.respond(status = HttpStatusCode.Unauthorized, message = "Invalid refresh token")
                return@post
            }

            val accessToken = authService.createAccessToken(request.userId)
            val refreshToken = authService.createRefreshToken(request.userId)
            call.respond(
                status = HttpStatusCode.OK,
                message = TokenResponse(accessToken, refreshToken)
            )
        }
    }
}