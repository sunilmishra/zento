package com.codewithmisu.zento.provider

class ServiceProviderService(private val repository: ServiceProviderRepository) {

    fun saveProvider(providerRequest: ServiceProviderRequest) {
        repository.upsertProvider(providerRequest)
    }

    fun getProviders(zipcode: String): List<ServiceProviderResponse> {
        if (zipcode.isBlank()) throw IllegalArgumentException("Zipcode cannot be blank")
        return repository.findProviders(zipcode)
    }

    fun removeProvider(id: String) {
        repository.deleteProvider(id)
    }

    fun updateCategories(id: String, categories: List<ServiceCategory>) {
        repository.updateCategories(id, categories)
    }
}