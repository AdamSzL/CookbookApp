package com.example.catalog.core

/**
 * Klucze dla Fragment Result API i argumentów fragmentów.
 * Trzymamy je w jednym miejscu, żeby nadawca i odbiorca używały tej samej nazwy.
 */
object ResultKeys {
    /** Lista -> Aktywność: "użytkownik wybrał przepis". */
    const val RECIPE_SELECTED = "recipe_selected"

    /** Dialog oceny -> Szczegóły: "użytkownik wybrał liczbę gwiazdek". */
    const val RATING_PICKED = "rating_picked"

    /** Szczegóły -> Lista: "ocena przepisu się zmieniła, odśwież wiersz". */
    const val RATING_CHANGED = "rating_changed"

    const val ARG_RECIPE_ID = "recipe_id"
    const val ARG_RATING = "rating"
}

/** 3 -> "★★★☆☆", brak oceny -> "—" */
fun starsOf(rating: Int?): String =
    if (rating == null) "—" else "★".repeat(rating) + "☆".repeat(5 - rating)
