package com.example.catalog.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalog.R
import com.example.catalog.core.ResultKeys
import com.example.catalog.data.RecipeRepository
import com.example.catalog.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment(R.layout.fragment_recipe_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(MainActivity.TAG, "List: onViewCreated")
        val binding = FragmentRecipeListBinding.bind(view)

        val adapter = RecipeAdapter(
            items = RecipeRepository.getAll(),
            ratingOf = RecipeRepository::getRating
        ) { recipe ->
            val result = Bundle().apply {
                putInt(ResultKeys.ARG_RECIPE_ID, recipe.id)
            }
            parentFragmentManager.setFragmentResult(ResultKeys.RECIPE_SELECTED, result)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.adapter = adapter

        parentFragmentManager.setFragmentResultListener(
            ResultKeys.RATING_CHANGED, viewLifecycleOwner
        ) { _, result ->
            adapter.notifyRecipeChanged(result.getInt(ResultKeys.ARG_RECIPE_ID))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(MainActivity.TAG, "List: onDestroyView")
    }
}
