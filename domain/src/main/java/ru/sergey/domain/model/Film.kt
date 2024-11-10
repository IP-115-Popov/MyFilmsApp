package ru.sergey.domain.model

data class Film(
    val id: Long,
    val localized_name: String,
    val name: String,
    val year: Int,
    val rating: Double,
    val image_url: String,
    val description: String,
    val genres: List<String>,
) {
    fun getLocalizedNameOrDefault(default: String) =
        if (localized_name == null || localized_name == "")
            if (name == null || name == "")
                default
            else name
        else localized_name

    fun getNameOrDefault(default: String) =
        if (name == null || name == "") default
        else name
}

