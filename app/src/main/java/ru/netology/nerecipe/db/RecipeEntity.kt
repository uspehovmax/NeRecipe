package ru.netology.nerecipe.db


import androidx.databinding.adapters.Converters
import androidx.room.*
import ru.netology.nerecipe.data.Region

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "region")
    val region: Region,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "picture")
    val picture: String,
    //@field:TypeConverters(Converter::class)
    @ColumnInfo(name = "stepsDescriptionRecipe")
    val stepsDescriptionRecipe: String ,//= arrayOf("|"),
    @ColumnInfo(name = "ingredients")
    val ingredients: String,
    @ColumnInfo(name = "likes")
    val likes: Int,
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean,
    @ColumnInfo(name = "shareCount")
    val shareCount: Int
)


