package com.example.restauapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restauapp.DB.MealDataBase
import com.example.restauapp.Models.Category
import com.example.restauapp.Models.CategoryList
import com.example.restauapp.Models.MealsByCategoryList
import com.example.restauapp.Models.MealsByCategory
import com.example.restauapp.Models.Meal
import com.example.restauapp.Models.MealList
import com.example.restauapp.Retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDataBase: MealDataBase): ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var  CategoriesLiveData = MutableLiveData<List<Category>>()
    private var FavoritesMealsLiveData = mealDataBase.mealdao().getAllSavedMeals()
    fun getRandomMeal(){
        RetrofitInstance.mealApi.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() !=null){
                    val randomMeal: Meal = response.body()!!.meals[0]

                    randomMealLiveData.value = randomMeal

                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d( "HomeFragment",t.message.toString())
            }


        })
    }
    fun getPopularItems(category:String){
        RetrofitInstance.mealApi.getPopularItems(category).enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null){
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d( "HomeFragment",t.message.toString())
            }

        })
    }
    fun getCategories(){
        RetrofitInstance.mealApi.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
//                if (response.body() != null){
//                    CategoriesLiveData.value = response.body()!!.categories
//                }
                response.body()?.let {categoryList ->
                    CategoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d( "HomeFragment",t.message.toString())
            }

        })
    }
    fun deleteMeal(mealId:String){
        viewModelScope.launch {
            mealDataBase.mealdao().deleteMeal(mealId)
        }
    }
    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDataBase.mealdao().upsert(meal)
        }
    }

    fun observeRandomMealLivedata():LiveData<Meal>{
        return randomMealLiveData
    }
    fun observerPopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }
    fun observerCategoriesLiveData():LiveData<List<Category>>{
        return CategoriesLiveData
    }
    fun observerFavoritesMealsLiveData():LiveData<List<Meal>>{
        return FavoritesMealsLiveData
    }
}