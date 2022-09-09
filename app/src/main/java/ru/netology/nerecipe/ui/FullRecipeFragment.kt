package ru.netology.nerecipe.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.RecipesAdapter
import ru.netology.nerecipe.adapter.showRegion
import ru.netology.nerecipe.databinding.FullRecipeViewBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel

class FullRecipeFragment : Fragment() {

    private val args by navArgs<FullRecipeFragmentArgs>()

    private val fullRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //переход к фрагменту CreateRecipeFragment
        fullRecipeViewModel.navigateToRecipeContentScreenEvent.observe(this) { recipe ->
            //val direction = FeedFragmentDirections.actionFeedFragmentToNewRecipeFragment(recipe)
            val direction = FullRecipeFragmentDirections.actionFullRecipeFragmentToCreateRecipeFragment (recipe)
            findNavController().navigate(direction)
        }
    }

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
            binding.fullRecipeView.title.text = fullRecipe.title
            binding.fullRecipeView.authorName.text = fullRecipe.author
            binding.fullRecipeView.recipeDescription.text = fullRecipe.description
            binding.fullRecipeView.region.text = context?.showRegion(fullRecipe.region)
            binding.fullRecipeView.favorite.text = fullRecipe.likes.toString()
            binding.fullRecipeView.share.text = fullRecipe.shareCount.toString()

            binding.ingredients.text = fullRecipe.ingredients

            binding.listViewSteps.text = fullRecipe.stepsDescriptionRecipe

        }

        fullRecipeViewModel.shareRecipeContent.observe(viewLifecycleOwner) { recipeContent ->
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
}