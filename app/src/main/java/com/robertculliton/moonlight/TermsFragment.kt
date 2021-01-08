package com.robertculliton.moonlight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.robertculliton.moonlight.databinding.FragmentTermsBinding

class TermsFragment : Fragment() {

  private lateinit var drawerLayout: DrawerLayout

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val binding = DataBindingUtil.inflate<FragmentTermsBinding>(inflater, R.layout.fragment_terms, container, false)


    val sharedPref = activity?.getSharedPreferences(
      getString(R.string.preference_file_key), Context.MODE_PRIVATE)



    setHasOptionsMenu(true)
    return binding.root
  }

}
