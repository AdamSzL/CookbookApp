package com.example.catalog.data

import com.example.catalog.core.Recipe

/**
 * Źródło danych — na Lab 1 przepisy są zapisane w kodzie.
 * W kolejnych labach można je podmienić na Room / API bez zmian w UI.
 */
object RecipeRepository {

    private val recipes = listOf(
        Recipe(
            1, "Naleśniki", "🥞", "Śniadanie", 30, 4, "Łatwe",
            listOf("250 g mąki pszennej", "2 jajka", "500 ml mleka", "250 ml wody gazowanej", "szczypta soli", "2 łyżki oleju"),
            listOf(
                "Zmiksuj wszystkie składniki na gładkie ciasto.",
                "Odstaw ciasto na 15 minut.",
                "Rozgrzej patelnię i posmaruj ją cienko olejem.",
                "Smaż cienkie placki ok. 1 minuty z każdej strony."
            )
        ),
        Recipe(
            2, "Jajecznica na boczku", "🍳", "Śniadanie", 15, 2, "Łatwe",
            listOf("4 jajka", "80 g boczku", "1 łyżka masła", "szczypiorek", "sól, pieprz"),
            listOf(
                "Pokrój boczek w kostkę i podsmaż na maśle.",
                "Wbij jajka na patelnię, dopraw solą i pieprzem.",
                "Mieszaj na małym ogniu, aż jajka się zetną.",
                "Posyp szczypiorkiem i podawaj z pieczywem."
            )
        ),
        Recipe(
            3, "Pierogi ruskie", "🥟", "Obiad", 90, 4, "Średnie",
            listOf("500 g mąki", "250 ml ciepłej wody", "2 łyżki oleju", "500 g ziemniaków", "250 g twarogu", "2 cebule"),
            listOf(
                "Ugotuj ziemniaki i przeciśnij je przez praskę.",
                "Wymieszaj z twarogiem i zeszkloną cebulą, dopraw.",
                "Zagnieć ciasto z mąki, wody i oleju.",
                "Rozwałkuj, wykrawaj krążki, nakładaj farsz i sklejaj.",
                "Gotuj partiami 2 minuty od wypłynięcia."
            )
        ),
        Recipe(
            4, "Spaghetti bolognese", "🍝", "Obiad", 45, 4, "Łatwe",
            listOf("400 g spaghetti", "500 g mięsa mielonego", "500 g passaty", "1 cebula", "2 ząbki czosnku", "1 marchewka", "oregano, bazylia, sól, pieprz", "parmezan"),
            listOf(
                "Podsmaż posiekaną cebulę, czosnek i startą marchewkę.",
                "Dodaj mięso mielone i smaż, aż się zrumieni.",
                "Wlej passatę, dopraw ziołami i duś 20 minut.",
                "Ugotuj makaron al dente w osolonej wodzie.",
                "Podawaj makaron z sosem, posyp parmezanem."
            )
        ),
        Recipe(
            5, "Zupa pomidorowa", "🍲", "Obiad", 45, 4, "Łatwe",
            listOf("1,5 l bulionu", "500 g passaty", "100 ml śmietany 18%", "1 marchewka", "1 pietruszka", "ryż lub makaron"),
            listOf(
                "Ugotuj warzywa w bulionie do miękkości.",
                "Dodaj passatę i gotuj 10 minut.",
                "Zahartuj śmietanę gorącą zupą i wlej do garnka.",
                "Podawaj z ryżem lub makaronem."
            )
        ),
        Recipe(
            6, "Kurczak curry z ryżem", "🍛", "Obiad", 35, 4, "Łatwe",
            listOf("500 g piersi z kurczaka", "400 ml mleka kokosowego", "1 cebula", "2 ząbki czosnku", "2 łyżki przyprawy curry", "200 g ryżu", "sól"),
            listOf(
                "Ugotuj ryż według instrukcji na opakowaniu.",
                "Pokrój kurczaka w kostkę i podsmaż z cebulą i czosnkiem.",
                "Dodaj przyprawę curry i smaż minutę.",
                "Wlej mleko kokosowe i gotuj 15 minut. Podawaj z ryżem."
            )
        ),
        Recipe(
            7, "Szarlotka", "🍎", "Deser", 80, 8, "Średnie",
            listOf("300 g mąki", "200 g zimnego masła", "100 g cukru", "2 żółtka", "1,5 kg jabłek", "cynamon"),
            listOf(
                "Posiekaj masło z mąką, dodaj cukier i żółtka, zagnieć ciasto.",
                "Schłodź ciasto 30 minut w lodówce.",
                "Jabłka zetrzyj i podduś z cynamonem.",
                "Wyłóż połowę ciasta, jabłka, zetrzyj resztę ciasta na wierzch.",
                "Piecz 50 minut w 180°C."
            )
        ),
        Recipe(
            8, "Ciasteczka owsiane", "🍪", "Deser", 25, 20, "Łatwe",
            listOf("200 g płatków owsianych", "100 g masła", "80 g cukru", "1 jajko", "100 g mąki", "garść rodzynek"),
            listOf(
                "Utrzyj miękkie masło z cukrem i jajkiem.",
                "Dodaj płatki, mąkę i rodzynki, wymieszaj.",
                "Formuj małe placuszki na blasze z papierem.",
                "Piecz 12–15 minut w 180°C."
            )
        )
    )

    fun getAll(): List<Recipe> = recipes

    fun getById(id: Int): Recipe? = recipes.find { it.id == id }

    // Oceny użytkownika (1–5). Tylko w pamięci — znikną po zamknięciu aplikacji.
    private val ratings = mutableMapOf<Int, Int>()

    fun getRating(recipeId: Int): Int? = ratings[recipeId]

    fun setRating(recipeId: Int, rating: Int) {
        ratings[recipeId] = rating
    }
}
