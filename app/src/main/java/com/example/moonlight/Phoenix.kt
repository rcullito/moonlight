package com.example.moonlight

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.moonlight.databinding.FragmentPhoenixBinding

class Phoenix: Fragment() {

  private lateinit var binding: FragmentPhoenixBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val sharedPref = activity?.getSharedPreferences(
      getString(R.string.preference_file_key), Context.MODE_PRIVATE)


    binding = DataBindingUtil.inflate<FragmentPhoenixBinding>(inflater,
      R.layout.fragment_phoenix, container, false)

    binding.setLifecycleOwner(this)

    binding.radioGroup1.setOnCheckedChangeListener { group, checkedId ->
      // Responds to child RadioButton checked/unchecked

      Log.i("interfere", checkedId.toString())

      with (sharedPref?.edit()) {
        this?.putInt(getString(R.string.interfere), checkedId)
        this?.apply()
      }
    }







    return binding.root

  }


}



