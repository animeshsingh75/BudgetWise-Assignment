package com.example.budgetwiseassignment.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryDetails(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryName: String,
    val budget: Int,
    var spent: Int,
    var balance: Int,
    val categoryColor:Int
)