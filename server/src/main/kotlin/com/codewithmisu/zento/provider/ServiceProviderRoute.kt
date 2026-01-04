package com.codewithmisu.zento.provider

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.providerRoute(service: ServiceProviderService) {

    route("/providers") {

        get("/{zipcode}") {
            val request = call.pathParameters
            val zipcode = request["zipcode"] ?: ""
            if (zipcode.isEmpty()) {
                call.respond(status = HttpStatusCode.BadRequest, message = "Invalid request")
                return@get
            }
            val providers = service.getProviders(zipcode)
            call.respond(status = HttpStatusCode.OK, message = providers)
        }

        /**
         * Only authenticated users can create/update/delete a provider.
         */
        authenticate("auth-jwt") {

            post("/create") {
                val providerRequest = call.receive<ServiceProviderRequest>()
                service.saveProvider(providerRequest)
                call.respond(HttpStatusCode.Created, "Provider created successfully")
            }

            delete("/{id}") {
                val request = call.pathParameters
                val id = request["id"] ?: ""
                if (id.isEmpty()) {
                    call.respond(
                        status = HttpStatusCode.BadRequest, message = "Invalid request"
                    )
                    return@delete
                }
                service.removeProvider(id)
                call.respond(HttpStatusCode.OK, "Provider deleted successfully")
            }

            put("/{id}/catalogs") {
                val request = call.pathParameters
                val id = request["id"] ?: ""
                if (id.isEmpty()) {
                    call.respond(
                        status = HttpStatusCode.BadRequest, message = "Invalid request"
                    )
                    return@put
                }

                val categories = call.receive<List<ServiceCategory>>()
                service.updateCategories(id, categories)
                call.respond(HttpStatusCode.OK, "Categories updated successfully")
            }
        }
    }
}
