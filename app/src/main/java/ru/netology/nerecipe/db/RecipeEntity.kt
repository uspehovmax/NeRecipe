package ru.netology.nerecipe.db


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
//    @ColumnInfo(name = "region")
//    val type: Type,
//    @ColumnInfo(name = "region")
//    val publicationDate: Date,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "picture")
    val picture: String,
    @ColumnInfo(name = "stepsDescription")
    val stepsDescription: List<String> = emptyList<String>(),
    @ColumnInfo(name = "pictureSteps")
    val pictureSteps: List<Int> = emptyList<Int>(),
    @ColumnInfo(name = "ingredients")
    val ingredients: String,
    @ColumnInfo(name = "likes")
    val likes: Int,
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean,
    @ColumnInfo(name = "shareCount")
    val shareCount: Int
)


