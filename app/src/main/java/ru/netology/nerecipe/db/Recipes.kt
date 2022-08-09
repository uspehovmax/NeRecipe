package ru.netology.nerecipe.db

import ru.netology.nerecipe.data.Recipe

fun RecipeEntity.toModel(): Recipe {
    val recipe = Recipe(
        // мы не можем вытащить данные по названию колонки, а только по ее ид(или индексу)
        id = id,
        author = author,
        region = region,
        //type = type,
        //publicationDate = publicationDate
        title = title,
        description = description,
        pictureId = pictureId,
        stepsDescription = stepsDescription,
        pictureSteps = pictureSteps,
        ingredients = ingredients,
        likes = likes,
        likedByMe = likedByMe,
        shareCount = shareCount
    )
    return recipe
}

fun Recipe.toEntity() = RecipeEntity(
    id = id,
    author = author,
    region = region,
    //type = type,
    //publicationDate = publicationDate
    title = title,
    description = description,
    pictureId = pictureId,
    stepsDescription = stepsDescription,
    pictureSteps = pictureSteps,
    ingredients = ingredients,
    likes = likes,
    likedByMe = likedByMe,
    shareCount = shareCount
)
