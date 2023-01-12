package com.example.assignm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.assignm.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val position = intent.getStringExtra("POSITION")!!
        Log.w("1",position)
        viewModel.setPosition(position)

        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController) // up button

        binding.navigationView.setNavigationItemSelectedListener {
            Log.i("test", it.itemId.toString())
            when (it.itemId) {
                R.id.material -> {
                    try{
                        navController.navigate(R.id.action_mainMenuFragment_to_materialFragment)
                    }catch (e : IllegalArgumentException){

                    }
                }
                R.id.employeeDirectory -> {
                    try{
                        navController.navigate(R.id.action_mainMenuFragment_to_employeeDirectoryFragment)
                    }catch (e : IllegalArgumentException){

                    }
                }
                R.id.signUp -> {
                    if(viewModel.getPosition().toString() == "Manager"){
                        try{
                            navController.navigate(R.id.action_mainMenuFragment_to_registerFragment)
                        }catch (e : IllegalArgumentException){

                        }
                    }else{
                        Toast.makeText(this,"Position Not Manager!", Toast.LENGTH_SHORT)
                    }
                }
                R.id.materialReport -> {
                    if(viewModel.getPosition().toString() == "Manager"){
                        try{
                            navController.navigate(R.id.action_mainMenuFragment_to_reportFragment)
                        }catch (e : IllegalArgumentException){

                        }
                    }else{
                        Toast.makeText(this,"Position Not Manager!", Toast.LENGTH_SHORT)
                    }
                }
                R.id.logOut -> startActivity(Intent(this, MainActivity::class.java))
            }
            true
        }

        drawerLayout = binding.drawerLayout
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout) // menu icon
        Log.i("test", "aba")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}