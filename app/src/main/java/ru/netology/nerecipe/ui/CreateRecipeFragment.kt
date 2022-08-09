package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.R
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.databinding.NewRecipeFragmentBinding
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.viewModel.RecipeViewModel

class CreateRecipeFragment : Fragment() {

    private val args by navArgs<CreateRecipeFragmentArgs>()

    //?????
    private val CreateRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = NewRecipeFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val thisRecipe = args.currentRecipe
        if (thisRecipe != null) {
            with(binding) {
                title.setText(thisRecipe.title)
                recipeDescription.setText(thisRecipe.description)
                authorName.setText(thisRecipe.author)
                region.setText(thisRecipe.region.toString())
                ingredients.setText(thisRecipe.ingredients)

            }
        }
        binding.title.requestFocus()

//        binding.regionRecipeRadioGroup.setOnCheckedChangeListener { _, _ ->
//            getCheckedRegion(binding.regionRecipeRadioGroup.checkedRadioButtonId)
//        }

        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }

    }.root

    private fun onOkButtonClicked(binding: NewRecipeFragmentBinding) {
        val currentRecipe = args.currentRecipe.let {
            Recipe(

                id = args.currentRecipe?.id ?: RecipeRepository.NEW_RECIPE_ID,
                author = binding.authorName.text.toString(),
                region = getCheckedRegion(R.id.regionRecipeRadioGroup),
                title = binding.title.text.toString(),
                description = binding.recipeDescription.text.toString(),
                picture = binding.avatar.toString() ,

                //stepsDescription =
                //pictureSteps =
                ingredients = binding.ingredients.text.toString(),

                likes = binding.favorite.lineCount,
                //likedByMe = binding.
                shareCount = binding.share.lineCount

            )
        }
        if (emptyFieldsCheck(recipe = currentRecipe )) {
            val resultBundle = Bundle(1)
            resultBundle.putParcelable(RESULT_KEY, currentRecipe) // ????
            setFragmentResult(REQUEST_KEY, resultBundle)
            findNavController().popBackStack() // возвращает на прошлый фрагмент
        }

    }

    // преобразуем отмеченную галочку в регион
    private fun getCheckedRegion(checkedId: Int) = when (checkedId) {
        R.id.checkBoxEuropean -> Region.European
        R.id.checkBoxAsian -> Region.Asian
        R.id.checkBoxPanasian -> Region.PanAsian
        R.id.checkBoxEastern -> Region.Eastern
        R.id.checkBoxAmerican -> Region.American
        R.id.checkBoxRussian -> Region.Russian
        R.id.checkBoxMediterranean -> Region.Mediterranean
        else -> throw IllegalArgumentException("Unknown type: $checkedId")
    }

    private fun emptyFieldsCheck(recipe: Recipe): Boolean {
        return if (recipe.title.isBlank() && recipe.description.isBlank()) {
            Toast.makeText(activity, "Write in title and description fields", Toast.LENGTH_LONG).show()
            false
        } else true
    }

    // чтобы передавать данные между фрагментами
    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "newContent"
    }
}

