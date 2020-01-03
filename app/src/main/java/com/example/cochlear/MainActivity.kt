package com.example.cochlear

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.patient_control, R.id.clinical_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //instantiating Shared Preferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        edit = sharedPreferences.edit()
    }

    //this method will save boolean values in Shared Preferences
    fun saveBoolean(key: String, value: Boolean) {
        edit.putBoolean(key, value)
        edit.commit()
    }

    //this method will save Integer values in Shared Preferences
    fun saveInt(key: String, value: Int) {
        edit.putInt(key, value)
        edit.commit()
    }

    //this method is to retrieve Integer values from Shared Preferences
    fun getIntValue(key: String): Int {
        return sharedPreferences.getInt(key, 5)
    }

    //this method is to retrieve Boolean values from Shared Preferences
    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

}
