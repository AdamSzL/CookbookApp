package com.example.catalog.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.catalog.R
import com.example.catalog.core.ResultKeys
import com.example.catalog.core.starsOf
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RatingDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val current = requireArguments().getInt(ResultKeys.ARG_RATING)
        val options = (1..5).map { starsOf(it) }.toTypedArray()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.rate_title)
            .setSingleChoiceItems(options, current - 1) { _, which ->
                val result = Bundle().apply {
                    putInt(ResultKeys.ARG_RATING, which + 1)
                }
                parentFragmentManager.setFragmentResult(ResultKeys.RATING_PICKED, result)
                dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    companion object {
        const val TAG = "rating_dialog"

        fun newInstance(currentRating: Int) = RatingDialogFragment().apply {
            arguments = Bundle().apply {
                putInt(ResultKeys.ARG_RATING, currentRating)
            }
        }
    }
}
