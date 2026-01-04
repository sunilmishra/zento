package com.codewithmisu.zento.user

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.userRoute(userService: UserService) {

    route("/user") {

        get("/{id}") {
            val request = call.pathParameters
            val id = request["id"] ?: ""
            if (id.isEmpty()) {
                call.respond(status = HttpStatusCode.BadRequest, message = "Invalid request")
                return@get
            }
            val user = userService.findUserBy(id)
            if (user == null) {
                call.respond(status = HttpStatusCode.NotFound, message = "User not found")
                return@get
            }
            call.respond(HttpStatusCode.OK, user)
        }

        get("/{id}/active") {
            val request = call.pathParameters
            val id = request["id"] ?: ""
            if (id.isEmpty()) {
                call.respond(status = HttpStatusCode.BadRequest, message = "Invalid request")
                return@get
            }
            val user = userService.findUserBy(id)
            if (user == null) {
                call.respond(status = HttpStatusCode.NotFound, message = "User not found")
                return@get
            }
            userService.markActive(user.email)
            call.respond(HttpStatusCode.OK, "User marked as active")
        }
    }
}
