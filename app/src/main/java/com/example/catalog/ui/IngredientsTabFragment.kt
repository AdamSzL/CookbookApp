package com.example.catalog.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.catalog.R
import com.example.catalog.core.ResultKeys
import com.example.catalog.data.RecipeRepository
import com.example.catalog.databinding.FragmentTabTextBinding

class IngredientsTabFragment : Fragment(R.layout.fragment_tab_text) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(MainActivity.TAG, "Ingredients child: onViewCreated")
        val binding = FragmentTabTextBinding.bind(view)
        val recipeId = requireArguments().getInt(ResultKeys.ARG_RECIPE_ID)
        val recipe = RecipeRepository.getById(recipeId) ?: return
        binding.tabText.text = recipe.ingredients.joinToString("\n") { "•  $it" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(MainActivity.TAG, "Ingredients child: onDestroyView")
    }

    companion object {
        fun newInstance(recipeId: Int) = IngredientsTabFragment().apply {
            arguments = Bundle().apply {
                putInt(ResultKeys.ARG_RECIPE_ID, recipeId)
            }
        }
    }
}
