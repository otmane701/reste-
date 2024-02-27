package com.example.restauapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.restauapp.Activities.MainActivity
import com.example.restauapp.Adapters.FavoriteMealAdapter
import com.example.restauapp.Models.Meal
import com.example.restauapp.R
import com.example.restauapp.ViewModel.HomeViewModel
import com.example.restauapp.databinding.CategoryMealCardBinding
import com.example.restauapp.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class FavoritesFragment : Fragment() {
    private lateinit var favoriteMealAdapter: FavoriteMealAdapter
    private lateinit var mvvm: HomeViewModel
    private lateinit var binding: FragmentFavoritesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvvm = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareFavoritesRecyclerView()
        observeFavorites()
        getSwipe()

    }

    fun getSwipe() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                 mvvm.deleteMeal(favoriteMealAdapter.differ.currentList[position].idMeal)
                Snackbar.make(requireView(),"Meal Deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        mvvm.insertMeal(favoriteMealAdapter.differ.currentList[position])
                    }).show()

            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favRecView)

    }

    private fun prepareFavoritesRecyclerView() {
        favoriteMealAdapter = FavoriteMealAdapter()
        binding.favRecView.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoriteMealAdapter
        }

    }

    private fun observeFavorites() {
        mvvm.observerFavoritesMealsLiveData().observe(viewLifecycleOwner, Observer { meals ->
            meals.forEach { meal ->
                Log.d("test", meal.idMeal)
            }
            favoriteMealAdapter.differ.submitList(meals)

        })
    }


}