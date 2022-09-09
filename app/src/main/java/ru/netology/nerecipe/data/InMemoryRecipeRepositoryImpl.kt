package ru.netology.nerecipe.data

import androidx.lifecycle.MutableLiveData

object InMemoryRecipeRepositoryImpl : RecipeRepository {
    private var nextId = 1L
    private var recipes = emptyList<Recipe>()

    override val data = MutableLiveData(recipes)

    override fun save(recipe: Recipe) {
        if (recipe.id == RecipeRepository.NEW_RECIPE_ID) insert(recipe) else update(recipe)
    }

    override fun delete(recipeId: Long) {
        recipes =
            recipes.filter { it.id != recipeId }
        data.value = recipes
    }

    override fun likedByMe(recipeId: Long) {
        recipes =
            recipes.map {
                if (it.id == recipeId) {
                    it.copy(likedByMe = !it.likedByMe, likes = it.likes+1)
                } else {
                    it
                }
            }
    data .value = recipes
}

override fun share(recipeId: Long) {
    recipes = recipes.map {
        it.copy(shareCount = it.shareCount + 1)
    }
    data.value = recipes
}

override fun search(recipeName: String) {
    recipes.find {
        it.title == recipeName
    } ?: throw RuntimeException("Ничего не найдено")
    data.value = recipes
}

override fun getRegion(region: Region) {
    recipes.find {
        it.region == region
    }
    data.value = recipes
}

override fun update() {
    data.value = recipes
}

private fun update(recipe: Recipe) {
    recipes = recipes.map {
        if (it.id == recipe.id) recipe else it
    }
    data.value = recipes
}

private fun insert(recipe: Recipe) {
    recipes =
        listOf(
            recipe.copy(
                id = nextId++
            )
        ) + recipes
    data.value = recipes
}
}