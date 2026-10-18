package com.example.catalog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catalog.R
import com.example.catalog.core.Recipe
import com.example.catalog.core.starsOf
import com.example.catalog.databinding.ItemRecipeBinding

class RecipeAdapter(
    private val items: List<Recipe>,
    private val ratingOf: (Int) -> Int?,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder =
        RecipeViewHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = items[position]
        with(holder.binding) {
            recipeEmoji.text = recipe.emoji
            recipeName.text = recipe.name
            recipeMeta.text = root.context.getString(R.string.fmt_list_meta, recipe.category, recipe.timeMinutes)
            recipeRating.text = starsOf(ratingOf(recipe.id))
            root.setOnClickListener { onClick(recipe) }
        }
    }

    override fun getItemCount(): Int = items.size

    /** Odświeża tylko jeden wiersz, zamiast całej listy. */
    fun notifyRecipeChanged(recipeId: Int) {
        val index = items.indexOfFirst { it.id == recipeId }
        if (index != -1) notifyItemChanged(index)
    }
}
