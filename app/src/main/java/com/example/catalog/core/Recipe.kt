package com.example.catalog.core

/**
 * Model domenowy — jeden przepis.
 * Między fragmentami przekazujemy tylko [id], nie cały obiekt.
 */
data class Recipe(
    val id: Int,
    val name: String,
    val emoji: String,
    val category: String,
    val timeMinutes: Int,
    val servings: Int,
    val difficulty: String,
    val ingredients: List<String>,
    val steps: List<String>
)
