package com.example.budgetwiseassignment.utils

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import com.example.budgetwiseassignment.R
import com.example.budgetwiseassignment.db.CategoryDetails
import java.text.NumberFormat
import java.util.Locale

object Utils {

    fun getDefaultList(context: Context): List<CategoryDetails> {
        return listOf(
            CategoryDetails(
                categoryName = context.getString(R.string.food),
                budget = 500,
                spent = 0,
                balance = 500,
                categoryColor = R.color.food
            ),
            CategoryDetails(
                categoryName = context.getString(R.string.shopping),
                budget = 10000,
                spent = 0,
                balance = 10000,
                categoryColor = R.color.shopping
            ),
            CategoryDetails(
                categoryName = context.getString(R.string.transportation),
                budget = 2500,
                spent = 0,
                balance = 2500,
                categoryColor = R.color.transportation
            ),
            CategoryDetails(
                categoryName = context.getString(R.string.education),
                budget = 20000,
                spent = 0,
                balance = 20000,
                categoryColor = R.color.education
            ),
            CategoryDetails(
                categoryName = context.getString(R.string.groceries),
                budget = 2000,
                spent = 0,
                balance = 2000,
                categoryColor = R.color.groceries
            ),
            CategoryDetails(
                categoryName = context.getString(R.string.housing),
                budget = 1000,
                spent = 0,
                balance = 1000,
                categoryColor = R.color.housing
            ),
        )
    }

    fun animateProgressBar(progressBar: ProgressBar, from: Int, to: Int, duration: Long) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", from, to)
        animator.duration = duration
        animator.start()
    }

    fun getCategoryIcon(context: Context, categoryName: String): Int {
        return when (categoryName) {
            context.getString(R.string.food) -> R.drawable.ic_food
            context.getString(R.string.shopping) -> R.drawable.ic_shopping
            context.getString(R.string.transportation) -> R.drawable.ic_transportation
            context.getString(R.string.education) -> R.drawable.ic_education
            context.getString(R.string.groceries) -> R.drawable.ic_groceries
            context.getString(R.string.housing) -> R.drawable.ic_housing
            else -> R.drawable.ic_launcher_background
        }
    }

    fun Int.formatWithCommas(): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        return "$${formatter.format(this)}"
    }

    fun hideKeyboard(activity: Activity, view: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
