package com.example.budgetwiseassignment.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetwiseassignment.db.CategoryDetails
import com.example.budgetwiseassignment.db.CategoryDetailsDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(private val db: CategoryDetailsDB) : ViewModel() {

    private val _categoryList = MutableLiveData<List<CategoryDetails>>()
    val categoryList: LiveData<List<CategoryDetails>> = _categoryList

    private val _updatedCategory = MutableLiveData<CategoryDetails>()
    val updatedCategory: LiveData<CategoryDetails> = _updatedCategory

    private val _categoryData = MutableLiveData<CategoryDetails?>()
    val categoryData: LiveData<CategoryDetails?> = _categoryData

    fun getCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            _categoryList.postValue(db.categoryDetailsDao().getAllCategories())
        }
    }

    fun getCategoryData(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _categoryData.postValue(db.categoryDetailsDao().getCategoryByName(categoryName))
        }
    }

    fun updateCategoryData(categoryDetails: CategoryDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            db.categoryDetailsDao()
                .updateCategoryFields(
                    categoryDetails.categoryName,
                    categoryDetails.spent,
                    categoryDetails.balance
                )
            _updatedCategory.postValue(categoryDetails)
            _categoryData.postValue(null)
        }
    }

    fun insertCategoryList(list: List<CategoryDetails>) {
        viewModelScope.launch(Dispatchers.IO) {
            db.categoryDetailsDao().insertAll(list)
        }
    }

    class TransactionViewModelFactory(private val db: CategoryDetailsDB) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TransactionViewModel(db) as T
        }
    }
}