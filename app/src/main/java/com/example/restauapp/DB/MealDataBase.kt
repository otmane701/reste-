package com.example.restauapp.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.restauapp.Models.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(MealTypeConvertor::class)
abstract class MealDataBase : RoomDatabase() {
    abstract fun mealdao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: MealDataBase? = null

//        @Synchronized
//        fun getInstance(context: Context): MealDataBase {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    MealDataBase::class.java,
//                    "meal_database"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
        @Synchronized
        fun getInstance(context: Context): MealDataBase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    MealDataBase::class.java,
                    "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
                return INSTANCE as MealDataBase
            }
    }

}

