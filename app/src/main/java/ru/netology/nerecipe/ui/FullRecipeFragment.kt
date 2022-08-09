package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.RecipesAdapter
import ru.netology.nerecipe.databinding.FullRecipeViewBinding
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.viewModel.RecipeViewModel

class FullRecipeFragment : Fragment() {

    private val args by navArgs<FullRecipeFragmentArgs>()

    private val fullRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FullRecipeViewBinding.inflate(layoutInflater, container, false).also { binding ->
        val viewHolder =
            RecipesAdapter.ViewHolder(binding.fullRecipeView, fullRecipeViewModel)
        fullRecipeViewModel.data.observe(viewLifecycleOwner) { recipes ->
            val fullRecipe = recipes.find { it.id == args.recipeCardId } ?: run {
                findNavController().navigateUp() // рецепт был удален
                return@observe
            }
            viewHolder.bind(fullRecipe)
            // fullRecipeView
            binding.fullRecipeView.title.text = fullRecipe.title
            binding.fullRecipeView.authorName.text = fullRecipe.author
            binding.fullRecipeView.recipeDescription.text = fullRecipe.description
            binding.fullRecipeView.region.text = fullRecipe.region.toString()
            binding.fullRecipeView.favorite.text = fullRecipe.likes.toString()
            binding.fullRecipeView.share.text = fullRecipe.shareCount.toString()

            // ingredients
            binding.ingredients.text = fullRecipe.ingredients

            // listViewSteps
            binding.listViewSteps.adapter

            //binding.recipeDescription.text = fullRecipe.description
            //binding.recipeImage.setImageResource(R.drawable.)
            //binding.recipeImage.visibility =
            //    if (fullRecipe.picture.isBlank()) View.GONE else View.VISIBLE

        }

        //организация перехода к фрагменту NewOrEditedRecipeFragment
        fullRecipeViewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) { recipe ->
            val direction =
                FullRecipeFragmentDirections.actionFullRecipeFragmentToCreateRecipeFragment(
                    recipe
                )
            findNavController().navigate(direction)
        }

        // показываем новый экран в нашем приложении
        // данная ф-ция будет вызвана при завершении NewOrEditedRecipeFragment
        setFragmentResultListener(
            requestKey = CreateRecipeFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != CreateRecipeFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(
                CreateRecipeFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            fullRecipeViewModel.onSaveButtonClicked(newRecipe)
        }

    }.root
}