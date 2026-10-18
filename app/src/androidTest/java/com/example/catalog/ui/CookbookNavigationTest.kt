package com.example.catalog.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.catalog.R
import com.example.catalog.core.starsOf
import com.example.catalog.data.RecipeRepository
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CookbookNavigationTest {
    private val recipe = RecipeRepository.getAll().first()

    @Test
    fun selectionUsesTheCorrectContainerAndBackBehavior() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            openFirstRecipe()
            onView(withId(R.id.detailName)).check(matches(withText(recipe.name)))

            scenario.onActivity { activity ->
                val manager = activity.supportFragmentManager
                val twoPane = activity.resources.getBoolean(R.bool.two_pane)
                val container = if (twoPane) R.id.detail_container else R.id.list_container
                assertTrue(manager.findFragmentById(container) is RecipeDetailsFragment)
                assertEquals(if (twoPane) 0 else 1, manager.backStackEntryCount)

                if (!twoPane) activity.onBackPressedDispatcher.onBackPressed()
            }

            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
            scenario.onActivity { activity ->
                assertTrue(activity.supportFragmentManager.findFragmentById(R.id.list_container) is RecipeListFragment)
                assertEquals(0, activity.supportFragmentManager.backStackEntryCount)
            }
        }
    }

    @Test
    fun selectedRecipeAndChildTabSurviveActivityRecreation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            openFirstRecipe()
            onView(withId(R.id.tabSteps)).perform(scrollTo(), click())

            scenario.recreate()

            onView(withId(R.id.detailName)).check(matches(withText(recipe.name)))
            onView(withId(R.id.tabText)).check(matches(withText(
                recipe.steps.mapIndexed { index, step -> "${index + 1}.  $step" }.joinToString("\n\n")
            )))
            scenario.onActivity { activity ->
                val details = activity.supportFragmentManager.fragments
                    .filterIsInstance<RecipeDetailsFragment>().single()
                assertEquals(recipe.id, details.recipeId)
                assertTrue(details.childFragmentManager.findFragmentById(R.id.child_container) is StepsTabFragment)
            }
        }
    }

    @Test
    fun restoredRatingDialogUpdatesDetailsAndTheList() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            openFirstRecipe()
            onView(withId(R.id.rateButton)).perform(scrollTo(), click())
            scenario.recreate()
            onView(withText(R.string.rate_title)).check(matches(isDisplayed()))
            onView(withText(starsOf(4))).perform(click())
            onView(withId(R.id.detailRating)).check(matches(withText(starsOf(4))))

            scenario.onActivity { activity ->
                val details = activity.supportFragmentManager.fragments
                    .filterIsInstance<RecipeDetailsFragment>().single()
                assertEquals(4, RecipeRepository.getRating(recipe.id))
                assertFalse(details.childFragmentManager.fragments.any { it is RatingDialogFragment })
                if (!activity.resources.getBoolean(R.bool.two_pane)) activity.onSupportNavigateUp()
            }

            onView(allOf(
                withId(R.id.recipeRating),
                isDescendantOfA(withId(R.id.recyclerView)),
                withText(starsOf(4))
            )).check(matches(isDisplayed()))
        }
    }

    private fun openFirstRecipe() {
        onView(allOf(withId(R.id.recipeName), withText(recipe.name))).perform(click())
    }
}
