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
import ru.netology.nerecipe.adapter.showRegion
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.RecipeRepository
//import ru.netology.nerecipe.databinding.RegionChooseFragment //
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.databinding.NewRecipeFragmentBinding
import ru.netology.nerecipe.databinding.RegionChooseBinding
import ru.netology.nerecipe.databinding.RegionFilterBinding
import ru.netology.nerecipe.ui.CreateRecipeFragment.Companion.REQUEST_KEY
import ru.netology.nerecipe.ui.CreateRecipeFragment.Companion.RESULT_KEY
import ru.netology.nerecipe.viewModel.RecipeViewModel

class RegionChooseFragment : Fragment() {

    private val args by navArgs<RegionChooseFragmentArgs>()

    //??????
    //private val regionChooseViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RegionChooseBinding.inflate(layoutInflater, container, false).also { binding ->

        val thisRecipe = args.currentRecipe
        if (thisRecipe != null) {
            with(binding) {
                regionRecipeRadioGroup.check(R.id.checkBoxEuropean) // по умолчанию при редактировании
                radioButtonEuropean.text = radioButtonEuropean.context.showRegion(Region.European)
                radioButtonAsian.text = radioButtonAsian.context.showRegion(Region.Asian)
                radioButtonPanasian.text = radioButtonPanasian.context.showRegion(Region.PanAsian)
                radioButtonEastern.text = radioButtonEastern.context.showRegion(Region.Eastern)
                radioButtonAmerican.text = radioButtonAmerican.context.showRegion(Region.American)
                radioButtonRussian.text = radioButtonRussian.context.showRegion(Region.Russian)
                radioButtonMediterranean.text =
                    radioButtonMediterranean.context.showRegion(Region.Mediterranean)

                binding.ok.setOnClickListener {
                    onOkButtonClicked(binding)
                }
            }
        }

    }.root

    private fun onOkButtonClicked(binding: RegionChooseBinding) {
        val currentRecipe = Recipe(

            id = args.currentRecipe?.id ?: RecipeRepository.NEW_RECIPE_ID,
            region = getCheckedRegion(binding.regionRecipeRadioGroup.checkedRadioButtonId),
            title = args.currentRecipe?.title.toString()

        )

        if (emptyFieldsCheck(recipe = currentRecipe)) {
            val resultBundle = Bundle(1)
            resultBundle.putParcelable(RESULT_KEY, currentRecipe)
            setFragmentResult(REQUEST_KEY, resultBundle)
            findNavController().popBackStack() // возвращает на прошлый фрагмент
        }
    }

    // преобразуем выбор в вид кухни/регион
    private fun getCheckedRegion(checkedId: Int) = when (checkedId) {
        R.id.radioButtonEuropean -> Region.European
        R.id.radioButtonAsian -> Region.Asian
        R.id.radioButtonPanasian -> Region.PanAsian
        R.id.radioButtonEastern -> Region.Eastern
        R.id.radioButtonAmerican -> Region.American
        R.id.radioButtonRussian -> Region.Russian
        R.id.radioButtonMediterranean -> Region.Mediterranean
        else -> throw IllegalArgumentException("Unknown region: $checkedId")
    }

    private fun emptyFieldsCheck(recipe: Recipe): Boolean {
        return if (recipe.title.isBlank() && recipe.description.isBlank()) {
            Toast.makeText(activity, "Write in title and description fields", Toast.LENGTH_LONG).show()
            false
        } else true
    }

    // чтобы передавать данные между фрагментами
    companion object {
        const val CHECKBOX_KEY = "checkBoxContent"
    }
}
