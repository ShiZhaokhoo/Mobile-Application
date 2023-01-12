package com.example.assignm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.assignm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.w("MainActivity", "Start Get Data")
        Database.getDataFromFirestore()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}