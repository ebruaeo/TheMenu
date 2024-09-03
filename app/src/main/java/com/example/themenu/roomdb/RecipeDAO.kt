package com.example.themenu.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.themenu.model.Recipe

@Dao
interface RecipeDAO {
    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun findById(id: Int): Recipe

    @Insert
    fun insert(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)
}