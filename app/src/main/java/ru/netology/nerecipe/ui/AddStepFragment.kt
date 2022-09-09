package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.databinding.AddStepFragmentBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel

class AddStepFragment: Fragment() {

    private val args by navArgs<AddStepFragmentArgs>()

    private val addStepFragmentViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AddStepFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {

            binding.ok.setOnClickListener {
                onOkButtonClicked(binding)
            }
        }
    }.root

    private fun onOkButtonClicked(binding: AddStepFragmentBinding) {
        val resultBundle = Bundle(1)
        resultBundle.putString(RESULT_KEY, binding.stepDescription.text.toString())
        setFragmentResult(RESULT_KEY, resultBundle)
        findNavController().popBackStack() // возвращает на прошлый фрагмент
    }

    // чтобы передавать данные между фрагментами
    companion object {
        const val RESULT_KEY = "result_key"
    }

}

