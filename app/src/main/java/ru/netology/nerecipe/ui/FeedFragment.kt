package ru.netology.nerecipe.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.RecipesAdapter
import ru.netology.nerecipe.databinding.FeedFragmentBinding
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.viewModel.RecipeViewModel

class FeedFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //организация перехода к фрагменту CreateRecipeFragment
        viewModel.navigateToRecipeContentScreenEvent.observe(this) { recipe ->
            //val direction = FeedFragmentDirections.actionFeedFragmentToNewRecipeFragment(recipe)
            val direction = FeedFragmentDirections.actionFeedFragmentToNewRecipeFragment(recipe)
            findNavController().navigate(direction)
        }
    }

    override fun onResume() {
        super.onResume()
        // вызвов при завершении CreateRecipeFragment
        setFragmentResultListener(
            requestKey = CreateRecipeFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != CreateRecipeFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(
                CreateRecipeFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newRecipe)
        }

        // вызвов при завершении RegionFilterFragment
        setFragmentResultListener(
            requestKey = RegionFilterFragment.CHECKBOX_KEY
        ) { requestKey, bundle ->
            if (requestKey != RegionFilterFragment.CHECKBOX_KEY) return@setFragmentResultListener
            val regions = bundle.getParcelableArrayList<Region>(
                RegionFilterFragment.CHECKBOX_KEY
            ) ?: return@setFragmentResultListener
            viewModel.showRecipesByRegion(regions)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val adapter = RecipesAdapter(viewModel)
        binding.recipesRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }

        binding.fabAdd.setOnClickListener {
            viewModel.onAddButtonClicked()
        }

        if (viewModel.setRegionFilter) {
            binding.fabUndo.isVisible = viewModel.setRegionFilter
            binding.fabAdd.visibility = View.GONE
            binding.fabUndo.setOnClickListener {
                viewModel.showRecipesByRegion(Region.values().toList())
                viewModel.setRegionFilter = false
                binding.fabUndo.visibility = View.GONE
                binding.fabAdd.visibility = View.VISIBLE
                viewModel.data.observe(viewLifecycleOwner) { recipes ->
                    adapter.submitList(recipes)
                }
            }
        } else {
            binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        adapter.submitList(viewModel.data.value)
                        return true
                    }
                    var recipeList = adapter.currentList
                    recipeList = recipeList.filter { recipe ->
                        recipe.title.lowercase().contains(newText.lowercase())
                    }
                    viewModel.searchRecipeByName(newText)

                    if (recipeList.isEmpty()) {
                        Toast.makeText(context, "No recipes", Toast.LENGTH_SHORT).show()
                        adapter.submitList(recipeList)
                    } else {
                        adapter.submitList(recipeList)
                    }
                    return true
                }
            })
        }

        //переход к фрагменту fullRecipeFragment
        viewModel.fullRecipeViewEvent.observe(viewLifecycleOwner) { recipeCardId ->
            binding.search.setQuery("", false)
            val direction =
                FeedFragmentDirections.actionFeedFragmentToFullRecipeFragment(recipeCardId)
            findNavController().navigate(direction)
        }

        viewModel.shareRecipeContent.observe(viewLifecycleOwner) { recipeContent ->
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


