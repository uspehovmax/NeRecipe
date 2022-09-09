package ru.netology.nerecipe.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.SettingsFragmentBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel
import java.util.*

class SettingsFragment: Fragment()  {

    private val args by navArgs<SettingsFragmentArgs>()

    private val settingsFragmentViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SettingsFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {
            //switchDark.setOnClickListener()  //.text = checkBoxEuropean.context.showRegion(Region.European)

            if (switchLanguage.isChecked)

            binding.ok.setOnClickListener {
                onOkButtonClicked(binding)
            }
        }
    }.root

    private fun onOkButtonClicked(binding: SettingsFragmentBinding) {

        // выбор темы
            val resultBundle = Bundle(1)
            setFragmentResult(RESULT_KEY, resultBundle)
            findNavController().popBackStack() // возвращает на прошлый фрагмент
    }


    // чтобы передавать данные между фрагментами
    companion object {
        const val RESULT_KEY = "result_key"
    }

}