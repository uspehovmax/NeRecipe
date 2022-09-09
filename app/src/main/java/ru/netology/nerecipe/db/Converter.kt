package ru.netology.nerecipe.db

import androidx.room.TypeConverter
import java.io.File.separator
import java.util.*
import java.util.stream.Collectors

class Converter {
    @TypeConverter
    fun fromRecipeEntity(stepsDescriptionRecipe: Array<String>): String {
        return stepsDescriptionRecipe.joinToString("|")
    }

    @TypeConverter
    fun toRecipeEntity(data: String): Array<String> {
        return data.split("|") as Array<String>
    }
}