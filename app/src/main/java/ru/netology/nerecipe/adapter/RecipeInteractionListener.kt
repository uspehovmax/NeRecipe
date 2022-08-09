package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.data.Recipe

interface RecipeInteractionListener {

    fun onRemoveButtonClicked(recipe: Recipe)
    fun onEditButtonClicked(recipe: Recipe)
    fun onRecipeCardClicked(recipe: Recipe)
    fun onLikesButtonClicked(recipe: Recipe)
    fun onShareButtonClicked(recipe: Recipe)
    fun onRecipeItemClicked(recipe: Recipe)


}