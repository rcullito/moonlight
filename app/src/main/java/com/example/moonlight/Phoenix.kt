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


fun checkedIdToInterfere(checkedId: Int): String {
  return when (checkedId) {
    2131231065 -> "vibrate"
    2131231066 -> "chime"
    2131231067 -> "both"
    else -> "none"
  }
}

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

    binding.radioGroup1.setOnCheckedChangeListener { _, checkedId ->

      with (sharedPref?.edit()) {
        this?.putString(getString(R.string.interfere), checkedIdToInterfere(checkedId))
        this?.apply()
      }
    }

    binding.radioGroup2.setOnCheckedChangeListener { group, checkedId ->

      with (sharedPref?.edit()) {
        this?.putInt(getString(R.string.bound), checkedId)
        this?.apply()
      }
    }







    return binding.root

  }


}



