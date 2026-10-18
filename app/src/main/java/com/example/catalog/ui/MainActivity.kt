package com.example.catalog.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.example.catalog.R
import com.example.catalog.core.ResultKeys
import com.example.catalog.data.RecipeRepository
import com.example.catalog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val isTwoPane by lazy { resources.getBoolean(R.bool.two_pane) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        supportFragmentManager.setFragmentResultListener(
            ResultKeys.RECIPE_SELECTED, this
        ) { _, result ->
            showDetails(result.getInt(ResultKeys.ARG_RECIPE_ID))
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.list_container, RecipeListFragment(), TAG_LIST)
            }
        } else if (savedInstanceState.getBoolean(KEY_TWO_PANE, isTwoPane) != isTwoPane) {
            restoreForWindowSize()
        }

        supportFragmentManager.addOnBackStackChangedListener { updateToolbar() }
        updateToolbar()
    }

    private fun showDetails(recipeId: Int, restoredState: Fragment.SavedState? = null) {
        if (RecipeRepository.getById(recipeId) == null) return

        val details = RecipeDetailsFragment.newInstance(recipeId)
        if (restoredState != null) details.setInitialSavedState(restoredState)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            if (isTwoPane) {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                replace(R.id.detail_container, details, TAG_DETAILS)
            } else {
                setCustomAnimations(
                    R.anim.slide_in_right, R.anim.fade_out,
                    R.anim.fade_in, R.anim.slide_out_right
                )
                replace(R.id.list_container, details, TAG_DETAILS)
                addToBackStack(TAG_DETAILS)
            }
        }
    }

    // Containers change with window width. Keep the current details and child state.
    private fun restoreForWindowSize() {
        val manager = supportFragmentManager
        val previousDetails = manager.fragments.filterIsInstance<RecipeDetailsFragment>().firstOrNull()
        val recipeId = previousDetails?.recipeId
        val detailsState = previousDetails?.let { manager.saveFragmentInstanceState(it) }

        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val detailsToRemove = manager.fragments.filterIsInstance<RecipeDetailsFragment>()
        manager.commitNow {
            setReorderingAllowed(true)
            detailsToRemove.forEach { remove(it) }
            if (manager.findFragmentById(R.id.list_container) !is RecipeListFragment) {
                replace(R.id.list_container, RecipeListFragment(), TAG_LIST)
            }
        }
        if (recipeId != null) showDetails(recipeId, detailsState)
    }

    private fun updateToolbar() {
        val details = supportFragmentManager.findFragmentById(R.id.list_container) as? RecipeDetailsFragment
        val showUp = !isTwoPane && details != null
        supportActionBar?.setDisplayHomeAsUpEnabled(showUp)
        title = if (showUp) {
            details?.let { RecipeRepository.getById(it.recipeId)?.name }
                ?: getString(R.string.title_list)
        } else {
            getString(R.string.title_list)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_TWO_PANE, isTwoPane)
    }

    companion object {
        const val TAG = "Lifecycle"
        private const val KEY_TWO_PANE = "two_pane"
        private const val TAG_LIST = "recipe_list"
        private const val TAG_DETAILS = "recipe_details"
    }
}
