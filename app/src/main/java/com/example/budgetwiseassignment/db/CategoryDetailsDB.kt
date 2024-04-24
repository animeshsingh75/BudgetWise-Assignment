package com.example.budgetwiseassignment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CategoryDetails::class], version = 1)
abstract class CategoryDetailsDB : RoomDatabase() {
    abstract fun categoryDetailsDao(): CategoryDetailsDao

    companion object {
        @Volatile
        private var INSTANCE: CategoryDetailsDB? = null
        fun getDatabase(context: Context): CategoryDetailsDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CategoryDetailsDB::class.java,
                    "category_details"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}