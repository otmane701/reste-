package com.example.restauapp.Activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.restauapp.DB.MealDataBase
import com.example.restauapp.R
import com.example.restauapp.ViewModel.HomeViewModel
import com.example.restauapp.ViewModel.HomeViewModelFactory
import com.example.restauapp.WorkManger.NotificationWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
 val viewModel: HomeViewModel by lazy{
     val mealDataBase = MealDataBase.getInstance(this)
     val homeViewModelFactoryProvider = HomeViewModelFactory(mealDataBase)
     ViewModelProvider(this,homeViewModelFactoryProvider)[HomeViewModel::class.java]
 }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.background = null
//        bottomNavigationView.menu.getItem(2).isEnabled = false
        val navController = Navigation.findNavController(this, R.id.nav_fragment)
        NavigationUI.setupWithNavController(bottomNavigationView,navController)
        val channel =
            NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
        val notificationWorkRequest: WorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(20, TimeUnit.SECONDS)
            .build()
        Log.i("WorkManager",notificationWorkRequest.toString())

        // Schedule the WorkRequest with WorkManager
        WorkManager.getInstance(this).enqueue(notificationWorkRequest)


    }
}