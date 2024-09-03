package com.example.themenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.themenu.databinding.FragmentListBinding
import com.example.themenu.databinding.FragmentRecipeBinding


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener { save(it) }
        binding.deleteButton.setOnClickListener { delete(it) }
        binding.imageView.setOnClickListener { choosePic(it) }

        arguments?.let {
            val info = RecipeFragmentArgs.fromBundle(it).info

            if (info == "new"){
                // yeni bilgi eklenecek
                binding.deleteButton.isEnabled = false
                binding.saveButton.isEnabled = true
            } else {
                binding.deleteButton.isEnabled = true
                binding.saveButton.isEnabled = false
            }
        }
    }

    fun save(view: View) {

    }

    fun delete(view: View) {

    }

    fun choosePic(view: View) {


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}