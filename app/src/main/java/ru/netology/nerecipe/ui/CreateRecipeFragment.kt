package ru.netology.nerecipe.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.adapter.showRegion
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.databinding.CreateRecipeFragmentBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel

class CreateRecipeFragment : Fragment() {
    private var regions: Region = Region.European

    private val args by navArgs<CreateRecipeFragmentArgs>()

    private val createRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private var filePath: Uri? = null
    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createRecipeViewModel.currentSteps.value?.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CreateRecipeFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        imageView = binding.dishPicture

        // вызвов при завершении  при завершении RegionChooseFragment
        setFragmentResultListener(
            requestKey = RegionChooseFragment.RADIOBUTTON_REQUEST
        ) { requestKey, bundle ->
            if (requestKey != RegionChooseFragment.RADIOBUTTON_REQUEST) return@setFragmentResultListener
            val currentRegion = bundle.getParcelable<Region>(
                RegionChooseFragment.RADIOBUTTON_RESULT
            ) ?: return@setFragmentResultListener
            createRecipeViewModel.chooseRegion(currentRegion)
            binding.textViewRegion.text = context?.showRegion(currentRegion)//createRecipeViewModel.regionChoose.toString()
            regions = currentRegion
        }

        // вызвов при завершении AddStepFragment
        setFragmentResultListener(
            requestKey = AddStepFragment.RESULT_KEY
        ) { requestKey, bundle ->
            if (requestKey != AddStepFragment.RESULT_KEY) return@setFragmentResultListener
            val stepDescription = bundle.getString(
                AddStepFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            if (stepDescription.isBlank()) {
                Toast.makeText(
                    activity,
                    "Step description is blank - not add in recipe",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                createRecipeViewModel.addStepDescription(stepDescription)
                binding.listViewSteps.text = createRecipeViewModel.currentSteps.value.toString()
            }
        }

        val thisRecipe = args.currentRecipe

        if (thisRecipe != null) {
            regions = thisRecipe.region
                with(binding) {
                title.setText(thisRecipe.title)
                recipeDescription.setText(thisRecipe.description)
                authorName.setText(thisRecipe.author)
                textViewRegion.text = context?.showRegion(regions)
                ingredients.setText(thisRecipe.ingredients)
            }
        } else {
            binding.textViewRegion.text = context?.showRegion(regions)
        }

        binding.title.requestFocus()

        binding.region.setOnClickListener {
            createRecipeViewModel.currentRegion.observe(viewLifecycleOwner) { currentRegion ->
                val direction =
                    CreateRecipeFragmentDirections.actionCreateRecipeFragmentToRegionChooseFragment(
                        currentRegion
                    )
                findNavController().navigate(direction)
            }
        }

        binding.dishPicture.setOnClickListener {
            choosePicture()
        }
        
        binding.addStepButton.setOnClickListener {
            createRecipeViewModel.currentDescription.observe(viewLifecycleOwner) { stepDescription ->
                val direction =
                    CreateRecipeFragmentDirections.actionCreateRecipeFragmentToAddStepFragment(
                        stepDescription
                    )
                findNavController().navigate(direction)
            }
        }

        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }

    }.root

    private fun onOkButtonClicked(binding: CreateRecipeFragmentBinding) {

        val currentRecipe = Recipe(
            id = args.currentRecipe?.id ?: RecipeRepository.NEW_RECIPE_ID,
            author = binding.authorName.text.toString(),
            region = regions,
            title = binding.title.text.toString(),
            description = binding.recipeDescription.text.toString(),
            ingredients = binding.ingredients.text.toString(),
            stepsDescriptionRecipe = binding.listViewSteps.text.toString()
        )
        if (emptyFieldsCheck(recipe = currentRecipe)) {

            val resultBundle = Bundle(1)
            resultBundle.putParcelable(RESULT_KEY, currentRecipe) // ????
            setFragmentResult(REQUEST_KEY, resultBundle)
            findNavController().popBackStack() // переход на прошлый фрагмент
        }
    }

    private fun emptyFieldsCheck(recipe: Recipe): Boolean {
        return if (recipe.title.isBlank() && recipe.description.isBlank()) {
            Toast.makeText(activity, "Write in title and description fields", Toast.LENGTH_LONG)
                .show()
            false
        } else true
    }

    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "newContent"
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        image.launch("image/*")
    }

    private val image = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let { it ->
            filePath = it
            imageView?.setImageURI(it)
        }
    }

}

