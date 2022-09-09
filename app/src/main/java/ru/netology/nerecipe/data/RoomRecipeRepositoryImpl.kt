package ru.netology.nerecipe.data


import androidx.lifecycle.map
import ru.netology.nerecipe.db.RecipeDao
import ru.netology.nerecipe.db.toEntity
import ru.netology.nerecipe.db.toModel
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.data.Recipe

class RoomRecipeRepositoryImpl(
    private val dao: RecipeDao
) : RecipeRepository {

    override var data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun save(recipe: Recipe) {
        dao.save(recipe.toEntity())
    }

    override fun delete(recipeId: Long) {
        dao.delete(recipeId)
    }

    override fun likedByMe(recipeId: Long) {
       dao.likedByMe(recipeId)
    }

    override fun share(recipeId: Long) {
        dao.share(recipeId)
    }

    override fun search(recipeName: String) {
        dao.search(recipeName)
    }

    override fun getRegion(region: Region) {
        dao.getRegion(region)
    }

    override fun update() {
        dao.update()
    }
}