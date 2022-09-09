package ru.netology.nerecipe.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.RecipesAdapter
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.databinding.FavoriteFragmentBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel

class FavoriteRecipeFragment : Fragment() {

    private val favoriteRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FavoriteFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = RecipesAdapter(favoriteRecipeViewModel)
        binding.recipesRecycler.adapter = adapter

        favoriteRecipeViewModel.data.observe(viewLifecycleOwner) { recipes ->

            val favouriteRecipes = recipes.filter { it.likedByMe }
            adapter.submitList(favouriteRecipes)

            val emptyList = recipes.none { it.likedByMe }
            binding.textEmptyList.visibility =
                if (emptyList) View.VISIBLE else View.GONE
            binding.iconEmptyList.visibility =
                if (emptyList) View.VISIBLE else View.GONE
        }

        //организация перехода к фрагменту fullRecipeFragment
        favoriteRecipeViewModel.fullRecipeViewEvent.observe(viewLifecycleOwner) { recipeCardId ->
            val direction =
                FavoriteRecipeFragmentDirections.actionFavoriteRecipeFragmentToFullRecipeFragment(
                    recipeCardId
                )
            findNavController().navigate(direction)
        }

        binding.fabAdd.setOnClickListener {
            favoriteRecipeViewModel.onAddButtonClicked()
        }

        //организация перехода к фрагменту CreateRecipeFragment
        favoriteRecipeViewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) { recipe ->
            val direction =
                FavoriteRecipeFragmentDirections.actionFavoriteRecipeFragmentToCreateRecipeFragment(
                    recipe
                )
            findNavController().navigate(direction)
        }

        favoriteRecipeViewModel.shareRecipeContent.observe(viewLifecycleOwner) { recipeContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, recipeContent)
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(intent, getString(R.string.chooser_share_recipe))
            startActivity(shareIntent)
        }


    }.root

    override fun onResume() {
        super.onResume()
        // показываем новый экран в нашем приложении
        // данная ф-ция будет вызвана при завершении NewOrEditedRecipeFragment
        setFragmentResultListener(
            requestKey = CreateRecipeFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != CreateRecipeFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(
                CreateRecipeFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            favoriteRecipeViewModel.onSaveButtonClicked(newRecipe)
        }
    }

}


