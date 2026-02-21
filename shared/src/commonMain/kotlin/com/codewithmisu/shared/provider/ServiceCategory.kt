package com.codewithmisu.shared.provider

import kotlinx.serialization.Serializable

@Serializable
enum class ServiceCategory(
    val displayName: String,
    val description: String
) {

    PLUMBING_FIXTURES(
        "Plumbing & Fixtures",
        "All plumbing repairs, installation of faucets, sinks, drains, water heaters, and geysers."
    ),

    ELECTRICAL_APPLIANCES(
        "Electrical & Appliances",
        "Wiring, AC, heaters, ovens, refrigerators, microwaves, and other household appliances."
    ),

    CLEANING_MAINTENANCE(
        "Home Cleaning & Maintenance",
        "Regular and deep cleaning, carpet cleaning, chimney and fan maintenance, and general upkeep."
    ),

    PAINTING_DECORATION(
        "Painting & Decoration",
        "Interior and exterior painting, minor home decoration, and touch-ups."
    ),

    CARPENTRY_FURNITURE(
        "Carpentry & Furniture",
        "Furniture assembly, door and window repairs, wall mounting, flooring, curtains, and blinds."
    ),

    GARDEN_OUTDOOR(
        "Garden & Outdoor",
        "Lawn care, garden maintenance, tree trimming, balcony, terrace, and roof work."
    ),

    SECURITY_SMART_HOME(
        "Home Security & Smart Setup",
        "Installation of smart devices, security cameras, and alarm systems for home safety."
    ),

    PEST_HANDYMAN(
        "Pest & Handyman Services",
        "Pest control, general small repairs, and home inspection services."
    ),

    APPLIANCE_REPAIR(
        "Appliance Installation & Repair",
        "Repair and installation of household appliances including AC, refrigerator, dishwasher, and oven."
    ),

    ROOFING_STRUCTURAL(
        "Roofing & Structural Work",
        "Roof repair and minor structural fixes around the home."
    );
}

