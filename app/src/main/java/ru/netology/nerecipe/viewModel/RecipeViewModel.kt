package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nerecipe.adapter.RecipeInteractionListener
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.RoomRecipeRepositoryImpl
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.util.SingleLiveEvent

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    //private val repository: RecipeRepository = InMemoryRecipeRepositoryImpl
    private val repository: RecipeRepository = RoomRecipeRepositoryImpl(
        dao = AppDb.getInstance(context = application).recipeDao
    )

    private var regionFilter: List<Region> = Region.values().toList()

    //val data get() = repository.data
    var setRegionFilter = false

    val data = repository.data.map { list ->
        list.filter { regionFilter.contains(it.region) }
    }

    val fullRecipeViewEvent = SingleLiveEvent<Long>() //?????

    // Эта LiveData хранит текст рецепта, который редактируется, или null, если новый текст добавляется пользователем
    val navigateToRecipeContentScreenEvent = SingleLiveEvent<Recipe?>()
    val currentRecipe = MutableLiveData<Recipe?>(null)
    var favoriteFilter: MutableLiveData<Boolean> = MutableLiveData()

    init {
        favoriteFilter.value = false
    }

    fun showRecipesByRegion(region: List<Region>) {
        regionFilter = region
        repository.update()
    }

    fun onSaveButtonClicked(recipe: Recipe) { // нужно научить, когда пришел новый рец, а когда неновый для редактирования
        if (recipe.description.isBlank() && recipe.title.isBlank()) return
        val newRecipe = currentRecipe.value?.copy( // создание копии рец с новым содержимым
            description = recipe.description,
            title = recipe.title,
            region = recipe.region
        ) ?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            author = "Unknown author",
            //val publicationDate: Date, ???
            region = recipe.region,
            //type = recipe.type,
            title = recipe.title,
            description = recipe.description,
            picture = recipe.picture,
            stepsDescription = recipe.stepsDescription,
            pictureSteps = recipe.pictureSteps,
            ingredients = recipe.ingredients,
            likes = recipe.likes,
            likedByMe = recipe.likedByMe,
            shareCount = recipe.shareCount
        )
        repository.save(newRecipe)
        currentRecipe.value = null // сброс контента сохраненного рец в строке, где мы его печатали
    }

    fun onAddButtonClicked() {
        navigateToRecipeContentScreenEvent.call()
    }

    fun searchRecipeByName(recipeTitle: String) = repository.search(recipeTitle)

    override fun onRemoveButtonClicked(recipe: Recipe) = repository.delete(recipe.id)

    override fun onEditButtonClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeContentScreenEvent.value = recipe
    }

    override fun onRecipeCardClicked(recipe: Recipe) {
        fullRecipeViewEvent.value = recipe.id
    }

    override fun onLikesButtonClicked(recipe: Recipe) = repository.likedByMe(recipe.id)

    override fun onShareButtonClicked(recipe: Recipe) = repository.share(recipe.id)

    override fun onRecipeItemClicked(recipe: Recipe) {
        navigateToRecipeContentScreenEvent.value = recipe
    }

}