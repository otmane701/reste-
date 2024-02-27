package com.example.restauapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.restauapp.Activities.CategoriesMealActivity
import com.example.restauapp.Activities.MainActivity
import com.example.restauapp.Activities.MealActivity
import com.example.restauapp.Adapters.CategoriesAdapter
import com.example.restauapp.Adapters.MostPopularAdapter
import com.example.restauapp.Models.MealsByCategory
import com.example.restauapp.Models.Meal
import com.example.restauapp.ViewModel.HomeViewModel
import com.example.restauapp.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var Mvvm: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.restauapp.Fragment.idMeal"
        const val MEAL_NAME = "com.example.restauapp.Fragment.nameMeal"
        const val MEAL_THUMB = "com.example.restauapp.Fragment.thumbMeal"
        const val CATEGORY_NAME ="com.example.restauapp.Fragment.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        homeMvvm = ViewModelProviders.of(this)[HomeViewModel::class.java]
        Mvvm = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePupularRecyclerView()
        Mvvm.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        Mvvm.getPopularItems("Seafood")
        observePupalarItemsLiveData()
        onPupalarItemsClick()

        prepareCategoriesRecyclerView()
        Mvvm.getCategories()
        observeCategoriesLiveData()
        onCategoriesClick()
    }

    private fun onCategoriesClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoriesMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory )
//            Toast.makeText(context,"${category.strCategory}${random * random+1+5} DH ",Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        Mvvm.observerCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)

//            categories.forEach{ category ->
//                Log.d("test", category.strCategory)
//            }


        })
    }

    private fun onPupalarItemsClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun preparePupularRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePupalarItemsLiveData() {
        Mvvm.observerPopularItemsLiveData().observe(viewLifecycleOwner, Observer { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        })
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        Mvvm.observeRandomMealLivedata().observe(viewLifecycleOwner, { meal ->

            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal

        })
    }


}