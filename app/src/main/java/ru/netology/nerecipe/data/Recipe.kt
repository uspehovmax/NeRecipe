package ru.netology.nerecipe.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.netology.nerecipe.R

@Serializable
@Parcelize
data class Recipe(
    val id: Long,
    val author: String = "Unknown",
    var region: Region = Region.American, // enum категория / кухня
    val title: String = "",
    val description: String = "",
    var picture: String = "",
    var ingredients: String = "",
    val likes: Int = 50,
    val likedByMe: Boolean = false,
    val shareCount: Int = 0,
    val stepsDescriptionRecipe: String = ""

): Parcelable

@Serializable
@Parcelize
enum class Region: Parcelable {
    European,
    Asian,
    PanAsian,
    Eastern,
    American,
    Russian,
    Mediterranean
}
