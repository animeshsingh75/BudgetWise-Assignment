package com.example.budgetwiseassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.budgetwiseassignment.R
import com.example.budgetwiseassignment.databinding.FragmentAddTransactionBinding
import com.example.budgetwiseassignment.db.CategoryDetailsDB
import com.example.budgetwiseassignment.ui.viewmodel.TransactionViewModel
import com.example.budgetwiseassignment.utils.Utils

class AddTransactionFragment : Fragment() {
    private lateinit var binding: FragmentAddTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private val db by lazy {
        CategoryDetailsDB.getDatabase(requireContext())
    }

    private val factory by lazy {
        TransactionViewModel.TransactionViewModelFactory(db)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), factory)[TransactionViewModel::class.java]
        val categories = resources.getStringArray(R.array.category)
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        ) {
            override fun getCount(): Int {
                // Exclude last item for display as it is the hint
                return super.getCount() - 1
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
        // Set default last item for hint.
        binding.categorySpinner.setSelection(adapter.count)

        binding.submitBtn.setOnClickListener {

            Utils.hideKeyboard(requireActivity(), binding.transactionAmtEdTv)
            val category = binding.categorySpinner.selectedItem.toString()
            val amount = binding.transactionAmtEdTv.text.toString()
            // Validation for input fields before submitting transaction.
            if (amount.isNotEmpty() && amount.isNotBlank() && binding.categorySpinner.selectedItemPosition != adapter.count) {
                viewModel.getCategoryData(category)
            } else {
                if (amount.isEmpty() or amount.isBlank()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_enter_amount), Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_enter_valid_category),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }
        }
        initObserver()
    }

    private fun initObserver() {
        viewModel.categoryData.observe(viewLifecycleOwner) {
            if(it!=null){
                // Transaction amount does not exceed the available balance.
                if (it.balance < binding.transactionAmtEdTv.text.toString().toInt()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.insufficient_balance), Toast.LENGTH_SHORT
                    ).show()
                    return@observe
                }
                // Update category details and navigate back.
                it.spent += binding.transactionAmtEdTv.text.toString().toInt()
                it.balance -= it.spent
                viewModel.updateCategoryData(it)
                parentFragmentManager.popBackStack()
            }
        }
    }
}