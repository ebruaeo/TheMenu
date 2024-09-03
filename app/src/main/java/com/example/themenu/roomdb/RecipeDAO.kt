package com.example.themenu.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.themenu.model.Recipe
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface RecipeDAO {
    @Query("SELECT * FROM recipe")
    fun getAll(): Flowable<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun findById(id: Int): Flowable<Recipe>

    @Insert
    fun insert(recipe: Recipe): Completable

    @Delete
    fun delete(recipe: Recipe): Completable
}