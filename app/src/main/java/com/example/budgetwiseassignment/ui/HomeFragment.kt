package com.example.budgetwiseassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetwiseassignment.R
import com.example.budgetwiseassignment.databinding.FragmentHomeBinding
import com.example.budgetwiseassignment.db.CategoryDetails
import com.example.budgetwiseassignment.db.CategoryDetailsDB
import com.example.budgetwiseassignment.ui.adapter.CategoryListAdapter
import com.example.budgetwiseassignment.ui.viewmodel.TransactionViewModel
import com.example.budgetwiseassignment.utils.Utils
import com.example.budgetwiseassignment.utils.Utils.formatWithCommas


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: CategoryListAdapter
    private lateinit var list: MutableList<CategoryDetails>

    private var totalBudget = 0
    private var totalSpent = 0

    private val factory by lazy {
        TransactionViewModel.TransactionViewModelFactory(db)
    }
    private val db by lazy {
        CategoryDetailsDB.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), factory)[TransactionViewModel::class.java]
        viewModel.getCategoryList()
        binding.fab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(
                    R.id.fragmentContainerView,
                    AddTransactionFragment()
                )
                .addToBackStack(null)
                .commit()
        }
        initObserver()
    }

    private fun initObserver() {
        viewModel.categoryList.observe(viewLifecycleOwner) {
            adapter = if (it.isEmpty()) {
                list = Utils.getDefaultList(requireContext()).toMutableList()
                viewModel.insertCategoryList(Utils.getDefaultList(requireContext()))
                CategoryListAdapter(list)
            } else {
                list = it.toMutableList()
                CategoryListAdapter(list)
            }
            // Update total budget and total spent.
            list.forEach { category ->
                totalBudget += category.budget
                totalSpent += category.spent
            }

            //UI Update
            binding.apply {
                balanceTv.text = "${(totalBudget - totalSpent).formatWithCommas()}"
                budgetNoTv.text = "${totalBudget.formatWithCommas()}"
                spentNoTv.text = "${totalSpent.formatWithCommas()}"
                progressBar.progress =
                    ((totalSpent.toFloat() / totalBudget.toFloat()) * 100).toInt()
                categoryRv.layoutManager = LinearLayoutManager(requireContext())
                categoryRv.adapter = adapter
            }
        }
        viewModel.updatedCategory.observe(viewLifecycleOwner) { categoryDetails ->
            // Find the index of the updated category and update UI of category item and totals
            val index = list.indexOfFirst { it.categoryName == categoryDetails.categoryName }
            list[index] = categoryDetails
            adapter.updateProgressBar(index)
            totalSpent += categoryDetails.spent
            val newProgress = ((totalSpent.toFloat() / totalBudget.toFloat()) * 100).toInt()
            Utils.animateProgressBar(
                binding.progressBar,
                binding.progressBar.progress,
                newProgress,
                1000
            )
            binding.balanceTv.text = "${(totalBudget - totalSpent).formatWithCommas()}"
            binding.spentNoTv.text = "${totalSpent.formatWithCommas()}"

        }
    }
}