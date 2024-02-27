package com.example.restauapp.DB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restauapp.Models.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal)


//    @Update
//    fun updateFavorite(meal:Meal)
//
    @Query("SELECT * FROM mealInformation ")
    fun getAllSavedMeals(): LiveData<List<Meal>>
//
//    @Query("SELECT * FROM mealInformation WHERE mealId =:id")
//    fun getMealById(id:String):Meal
//
//    @Query("DELETE FROM mealInformation WHERE mealId =:id")
//    fun deleteMealById(id:String)
//
    @Query("DELETE FROM mealInformation WHERE idMeal = :mealId")
    suspend fun deleteMeal(mealId:String)
}