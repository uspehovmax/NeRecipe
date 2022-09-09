package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.RecipeInteractionListener
import ru.netology.nerecipe.data.*
import ru.netology.nerecipe.databinding.RegionChooseFragmentBinding
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.util.SingleLiveEvent
import java.lang.StringBuilder

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: RecipeRepository = RoomRecipeRepositoryImpl(
        dao = AppDb.getInstance(context = application).recipeDao
    )
    private var regionFilter: List<Region> = Region.values().toList()
    lateinit var regionChoose: String
    var setRegionFilter = false

    val data = repository.data.map { list ->
        list.filter { regionFilter.contains(it.region) }
    }

    val fullRecipeViewEvent = SingleLiveEvent<Long>()

    // Эта LiveData хранит данные, которые редактируются или null
    val navigateToRecipeContentScreenEvent = SingleLiveEvent<Recipe?>()
    val shareRecipeContent = SingleLiveEvent<Recipe?>()

    val steps = StringBuilder()
    val currentRecipe = MutableLiveData<Recipe?>(null)
    val currentRegion = MutableLiveData<Region>(Region.Eastern)
    val currentDescription = MutableLiveData<String?>(null)
    val currentSteps = MutableLiveData<StringBuilder?>()
    var favoriteFilter: MutableLiveData<Boolean> = MutableLiveData()

    init {
        favoriteFilter.value = false
    }

    fun showRecipesByRegion(regions: List<Region>) {
        regionFilter = regions
        repository.update()
    }

    fun chooseRegion(region: Region): String {
        //regionChoose = region
        //repository.update()

        regionChoose = when (region) {
            Region.European -> R.id.radioButtonEuropean.toString()
            Region.Asian -> R.string.asian.toString()
            Region.PanAsian -> R.string.panasian.toString()
            Region.Eastern -> R.string.eastern.toString()
            Region.American -> R.string.american.toString()
            Region.Russian -> R.string.russian.toString()
            Region.Mediterranean -> R.string.mediterranean.toString()
        }
        return regionChoose
    }

    fun addStepDescription(step: String) {
        steps.append(step)
            .append("\n")
            .append("_________________________")
            .append("\n")
        currentSteps.value = steps
        repository.update()
    }

    fun onSaveButtonClicked(recipe: Recipe) {
        if (recipe.description.isBlank() && recipe.title.isBlank()) return
        val newRecipe = currentRecipe.value?.copy(
            description = recipe.description,
            title = recipe.title,
            region = recipe.region
        ) ?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            author = recipe.author,
            region = recipe.region,
            title = recipe.title,
            description = recipe.description,
            picture = recipe.picture,
            ingredients = recipe.ingredients,
            likes = recipe.likes,
            likedByMe = recipe.likedByMe,
            shareCount = recipe.shareCount,
            stepsDescriptionRecipe = recipe.stepsDescriptionRecipe
        )
        repository.save(newRecipe)
        currentRecipe.value = null //
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

    override fun onLikesButtonClicked(recipe: Recipe) {
        repository.likedByMe(recipe.id)
    }

    override fun onShareButtonClicked(recipe: Recipe) {
        repository.share(recipe.id)
        shareRecipeContent.value = recipe
    }

    override fun onRecipeItemClicked(recipe: Recipe) {
        navigateToRecipeContentScreenEvent.value = recipe
    }

}