package com.example.budgetwiseassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDetailsDao {

    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<CategoryDetails>

    @Query("SELECT * FROM category WHERE categoryName = :categoryName")
    suspend fun getCategoryByName(categoryName: String): CategoryDetails

    @Query("UPDATE category SET spent = :spent, balance = :balance WHERE categoryName = :categoryName")
    suspend fun updateCategoryFields(categoryName: String, spent: Int, balance: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryDetails>)
}