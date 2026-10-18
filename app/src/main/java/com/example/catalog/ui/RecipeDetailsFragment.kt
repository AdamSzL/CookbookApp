package com.example.catalog.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.catalog.R
import com.example.catalog.core.ResultKeys
import com.example.catalog.core.starsOf
import com.example.catalog.data.RecipeRepository
import com.example.catalog.databinding.FragmentRecipeDetailsBinding

class RecipeDetailsFragment : Fragment(R.layout.fragment_recipe_details) {
    val recipeId: Int get() = requireArguments().getInt(ResultKeys.ARG_RECIPE_ID)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(MainActivity.TAG, "Details($recipeId): onViewCreated")
        val binding = FragmentRecipeDetailsBinding.bind(view)
        val recipe = RecipeRepository.getById(recipeId) ?: run {
            binding.detailName.text = getString(R.string.not_found)
            return
        }

        binding.detailEmoji.text = recipe.emoji
        binding.detailName.text = recipe.name
        binding.detailMeta.text = getString(
            R.string.fmt_details_meta, recipe.timeMinutes, recipe.servings, recipe.difficulty
        )
        binding.detailRating.text = starsOf(RecipeRepository.getRating(recipeId))

        binding.rateButton.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(RatingDialogFragment.TAG) == null) {
                RatingDialogFragment.newInstance(RecipeRepository.getRating(recipeId) ?: 0)
                    .show(childFragmentManager, RatingDialogFragment.TAG)
            }
        }

        childFragmentManager.setFragmentResultListener(
            ResultKeys.RATING_PICKED, viewLifecycleOwner
        ) { _, result ->
            val rating = result.getInt(ResultKeys.ARG_RATING)
            RecipeRepository.setRating(recipeId, rating)
            binding.detailRating.text = starsOf(rating)

            val changed = Bundle().apply {
                putInt(ResultKeys.ARG_RECIPE_ID, recipeId)
            }
            parentFragmentManager.setFragmentResult(ResultKeys.RATING_CHANGED, changed)
        }

        val currentTab = childFragmentManager.findFragmentById(R.id.child_container)
        val tabId = if (currentTab is StepsTabFragment) R.id.tabSteps else R.id.tabIngredients
        binding.tabGroup.check(tabId)
        if (currentTab == null) showTab(tabId)

        binding.tabGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) showTab(checkedId)
        }
    }

    private fun showTab(buttonId: Int) {
        val current = childFragmentManager.findFragmentById(R.id.child_container)
        val ingredients = buttonId == R.id.tabIngredients
        if (ingredients && current is IngredientsTabFragment) return
        if (!ingredients && current is StepsTabFragment) return

        val tab = if (ingredients) {
            IngredientsTabFragment.newInstance(recipeId)
        } else {
            StepsTabFragment.newInstance(recipeId)
        }
        childFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            replace(R.id.child_container, tab)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(MainActivity.TAG, "Details($recipeId): onDestroyView")
    }

    companion object {
        fun newInstance(recipeId: Int) = RecipeDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(ResultKeys.ARG_RECIPE_ID, recipeId)
            }
        }
    }
}
