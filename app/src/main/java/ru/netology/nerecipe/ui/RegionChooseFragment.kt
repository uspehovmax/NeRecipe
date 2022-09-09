package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.showRegion
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.databinding.RegionChooseFragmentBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel

class RegionChooseFragment : Fragment() {

    private val args by navArgs<RegionChooseFragmentArgs>()

    private val regionChooseViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RegionChooseFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {
            radioButtonEuropean.text = radioButtonEuropean.context.showRegion(Region.European)
            radioButtonAsian.text = radioButtonAsian.context.showRegion(Region.Asian)
            radioButtonPanasian.text = radioButtonPanasian.context.showRegion(Region.PanAsian)
            radioButtonEastern.text = radioButtonEastern.context.showRegion(Region.Eastern)
            radioButtonAmerican.text = radioButtonAmerican.context.showRegion(Region.American)
            radioButtonRussian.text = radioButtonRussian.context.showRegion(Region.Russian)
            radioButtonMediterranean.text =
                radioButtonMediterranean.context.showRegion(Region.Mediterranean)
            radioButtonEuropean.isChecked = true // по умолчанию

            binding.ok.setOnClickListener {
                onOkButtonClicked(binding)
            }
        }
    }.root

    private fun onOkButtonClicked(binding: RegionChooseFragmentBinding) {
        binding.regionRecipeRadioGroup


        val currentRegion = getCheckedRegion(binding.regionRecipeRadioGroup.checkedRadioButtonId)
        regionChooseViewModel.chooseRegion(currentRegion)
        val resultBundle = Bundle(1)
        resultBundle.putParcelable(RADIOBUTTON_RESULT, currentRegion)
        setFragmentResult(RADIOBUTTON_REQUEST, resultBundle)
        findNavController().popBackStack() // возвращает на прошлый фрагмент
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




    // чтобы передавать данные между фрагментами
    companion object {
        const val RADIOBUTTON_REQUEST = "radioButtonRequestKey"
        const val RADIOBUTTON_RESULT = "radioButtonContent"

    }
}

