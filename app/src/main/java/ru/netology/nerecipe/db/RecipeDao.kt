package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.Region

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipeEntity)

    // ?????
    @Query("UPDATE recipes SET title = :title, description = :description, region = :region WHERE id = :id")
    fun updateById(
        id: Long, title: String,
        description: String, region: Region
    )

    fun save(recipe: RecipeEntity) =
        if (recipe.id == RecipeRepository.NEW_RECIPE_ID)
            insert(recipe)
        else updateById(recipe.id, recipe.title, recipe.description, recipe.region)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun delete(id: Long)

    @Query(
        """
        UPDATE recipes SET
        like = CASE WHEN addedToFavourites THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun likedByMe(id: Long)

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :text || '%'")
    fun search(text: String): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE region = :regionRecipe")
    fun getRegion(regionRecipe: Region): LiveData<List<RecipeEntity>>

    @Query("UPDATE recipes SET title = title")
    fun update()


}