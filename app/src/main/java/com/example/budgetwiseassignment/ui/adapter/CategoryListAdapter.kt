package com.example.budgetwiseassignment.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetwiseassignment.databinding.ItemCategoryLayoutBinding
import com.example.budgetwiseassignment.db.CategoryDetails
import com.example.budgetwiseassignment.utils.Utils
import com.example.budgetwiseassignment.utils.Utils.formatWithCommas


class CategoryListAdapter(
    private val data: MutableList<CategoryDetails>,
) :
    RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>() {
    // Last updated item in the list to animate progress bar changes.
    var lastUpdatedPosition: Int = -1
    private lateinit var binding: ItemCategoryLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        binding =
            ItemCategoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CategoryListViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateProgressBar(position: Int) {
        // Notify the adapter of item changes and animate progress bar.
        lastUpdatedPosition = position
        notifyItemChanged(position)
    }


    inner class CategoryListViewHolder(
        private val context: Context,
        private val binding: ItemCategoryLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryDetails) {
            binding.apply {
                categoryTv.text = category.categoryName
                categoryIv.setImageResource(Utils.getCategoryIcon(context, category.categoryName))
                spentTv.text = "${category.spent.formatWithCommas()}"
                totalTv.text = "of ${category.budget.formatWithCommas()}"
                balanceTv.text = "${category.balance.formatWithCommas()} left"
                progressBar.setProgressTintList(
                    ContextCompat.getColorStateList(
                        context,
                        category.categoryColor
                    )
                )
                // Animate progress bar changes
                if (lastUpdatedPosition == adapterPosition) {
                    val newProgress =
                        ((category.spent.toFloat() / category.budget.toFloat()) * 100).toInt()
                    Utils.animateProgressBar(progressBar, progressBar.progress, newProgress, 1000)
                    lastUpdatedPosition = -1
                } else {
                    progressBar.progress =
                        ((category.spent.toFloat() / category.budget.toFloat()) * 100).toInt()
                }
            }

        }
    }
}
