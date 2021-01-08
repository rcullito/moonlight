package com.robertculliton.moonlight

import android.content.Context
import  android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.robertculliton.moonlight.databinding.ActivityMainBinding
import com.robertculliton.moonlight.databinding.FragmentTermsBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var termsBinding: FragmentTermsBinding
  private lateinit var drawerLayout: DrawerLayout

  override fun onCreate(savedInstanceState: Bundle?) {

    Log.i("MainActivity", "onCreate Called yipee")
    super.onCreate(savedInstanceState)


    val sharedPref = this.getSharedPreferences(
      getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    val viewedTerms = sharedPref?.getString("viewedTerms", "no")

    if (viewedTerms == "no") {
      termsBinding = DataBindingUtil.setContentView(this, R.layout.fragment_terms)
    } else {
      binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

      drawerLayout = binding.drawerLayout
      val navController = this.findNavController(R.id.myNavHostFragment)

      NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
      NavigationUI.setupWithNavController(binding.navView, navController)
    }


  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = this.findNavController(R.id.myNavHostFragment)
    return NavigationUI.navigateUp(navController, drawerLayout)
  }
}
