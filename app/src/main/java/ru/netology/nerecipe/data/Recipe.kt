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
    val region: Region = Region.European, // enum категория / кухня
    val title: String,
    val description: String = "",
    val picture: String = "",
    var stepsDescription: List<String> = emptyList<String>(),
    var pictureSteps: List<Int> = emptyList<Int>(),
    var ingredients: String = "",
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val shareCount: Int = 0,
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

/*
enum class Type {
    Bakery,
    Breakfast,
    Desserts,
    Drinks,
    Grilled,
    MainDish,
    Porridge,
    Salad,
    SideDish,
    Soup
}
*/
