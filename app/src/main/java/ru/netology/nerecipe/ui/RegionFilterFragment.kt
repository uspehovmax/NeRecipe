package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.adapter.showRegion
import ru.netology.nerecipe.databinding.RegionFilterBinding //
import ru.netology.nerecipe.data.Region
import ru.netology.nerecipe.viewModel.RecipeViewModel

class RegionFilterFragment : Fragment() {

    private val regionFilterViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RegionFilterBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {
            checkBoxEuropean.text = checkBoxEuropean.context.showRegion(Region.European)
            checkBoxAsian.text = checkBoxAsian.context.showRegion(Region.Asian)
            checkBoxPanasian.text = checkBoxPanasian.context.showRegion(Region.PanAsian)
            checkBoxEastern.text = checkBoxEastern.context.showRegion(Region.Eastern)
            checkBoxAmerican.text = checkBoxAmerican.context.showRegion(Region.American)
            checkBoxRussian.text = checkBoxRussian.context.showRegion(Region.Russian)
            checkBoxMediterranean.text =
                checkBoxMediterranean.context.showRegion(Region.Mediterranean)

            binding.ok.setOnClickListener {
                onOkButtonClicked(binding)
            }
        }

    }.root

    private fun onOkButtonClicked(binding: RegionFilterBinding) {

        val regionList = arrayListOf<Region>()
        var checkedCount = 7
        val nothingIsChecked = 0

        if (binding.checkBoxEuropean.isChecked) {
            regionList.add(Region.European)
            regionFilterViewModel.setRegionFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxAsian.isChecked) {
            regionList.add(Region.Asian)
            regionFilterViewModel.setRegionFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxPanasian.isChecked) {
            regionList.add(Region.PanAsian)
            regionFilterViewModel.setRegionFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxEastern.isChecked) {
            regionList.add(Region.Eastern)
            regionFilterViewModel.setRegionFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxAmerican.isChecked) {
            regionList.add(Region.American)
            regionFilterViewModel.setRegionFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxRussian.isChecked) {
            regionList.add(Region.Russian)
            regionFilterViewModel.setRegionFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxMediterranean.isChecked) {
            regionList.add(Region.Mediterranean)
            regionFilterViewModel.setRegionFilter = true
        } else {
            --checkedCount
        }

        if (checkedCount == nothingIsChecked) {
            Toast.makeText(activity, "You'd select at least one region", Toast.LENGTH_LONG).show()
        } else {
            regionFilterViewModel.showRecipesByRegion(regionList)
            val resultBundle = Bundle(1)
            resultBundle.putParcelableArrayList(CHECKBOX_KEY, regionList) // ?????
            setFragmentResult(CHECKBOX_KEY, resultBundle)
            findNavController().popBackStack()
        }
    }

    // чтобы передавать данные между фрагментами
    companion object {
        const val CHECKBOX_KEY = "checkBoxContent"
    }
}