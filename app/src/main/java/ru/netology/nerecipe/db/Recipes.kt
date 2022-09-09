package ru.netology.nerecipe.db

import ru.netology.nerecipe.data.Recipe

fun RecipeEntity.toModel(): Recipe {
    val recipe = Recipe(
        // мы не можем вытащить данные по названию колонки, а только по ее ид(или индексу)
        id = id,
        author = author,
        region = region,
        title = title,
        description = description,
        picture = picture,
        ingredients = ingredients,
        likes = likes,
        likedByMe = likedByMe,
        shareCount = shareCount,
        stepsDescriptionRecipe = stepsDescriptionRecipe,

    )
    return recipe
}

fun Recipe.toEntity() = RecipeEntity(
    id = id,
    author = author,
    region = region,
    title = title,
    description = description,
    picture = picture,
    ingredients = ingredients,
    likes = likes,
    likedByMe = likedByMe,
    shareCount = shareCount,
    stepsDescriptionRecipe = stepsDescriptionRecipe
)
