package com.example.themenu.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.themenu.model.Recipe

@Database(entities = [Recipe::class], version = 2)
abstract class RecipeDataBase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDAO
}