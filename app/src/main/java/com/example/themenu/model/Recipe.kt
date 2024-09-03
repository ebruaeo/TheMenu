package com.example.themenu.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "ingredients")
    var ingredients: String,
    @ColumnInfo(name = "picture" )
    var picture: ByteArray,
    @ColumnInfo(name = "cook")
    var recipe: String
){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
