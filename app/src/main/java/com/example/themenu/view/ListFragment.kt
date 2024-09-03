package com.example.themenu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.themenu.adapter.RecipeAdapter
import com.example.themenu.databinding.FragmentListBinding
import com.example.themenu.model.Recipe
import com.example.themenu.roomdb.RecipeDAO
import com.example.themenu.roomdb.RecipeDataBase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: RecipeDataBase
    private lateinit var recipeDAO: RecipeDAO

    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(), RecipeDataBase::class.java, "Recipes").build()
        recipeDAO = db.recipeDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener { addNew(it) }
        binding.recipeRV.layoutManager = LinearLayoutManager(requireContext())

        getDatas()

    }

    private fun getDatas(){
        mDisposable.add(
            recipeDAO.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }
    private fun handleResponse(recipes: List<Recipe>){
        val adapter = RecipeAdapter(recipes)
        binding.recipeRV.adapter = adapter
    }

    fun addNew(view: View){
        val action = ListFragmentDirections.actionListFragmentToRecipeFragment(info = "new", id = 0)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }


}
