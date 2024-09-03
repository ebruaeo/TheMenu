package com.example.themenu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.themenu.databinding.RecyclerRowBinding
import com.example.themenu.model.Recipe
import com.example.themenu.view.ListFragmentDirections

class RecipeAdapter(val recipeList: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {

    class RecipeHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val recyclerRowBinding =
            RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
      holder.binding.RecyclerViewTextView.text = recipeList[position].name
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToRecipeFragment(info = "eski", id = recipeList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }
}