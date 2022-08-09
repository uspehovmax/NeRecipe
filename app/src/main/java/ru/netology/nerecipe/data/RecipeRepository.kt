package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData

interface RecipeRepository {

    val data: LiveData<List<Recipe>>

    fun save(recipe: Recipe)
    fun delete(recipeId: Long)
    fun likedByMe(recipeId: Long)
    fun share(recipeId: Long)
    fun search(recipeName: String)
    fun getRegion(region: Region)
    fun update()


    companion object {
        const val NEW_RECIPE_ID = 0L
    }

}