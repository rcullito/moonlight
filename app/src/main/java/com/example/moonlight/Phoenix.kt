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
    R.id.radio_button_1 -> "vibrate"
    R.id.radio_button_2 -> "chime"
    R.id.radio_button_3 -> "both"
    else -> "none"
  }
}

fun checkedToBound(checkedId: Int): String {
  return when (checkedId) {
    R.id.radio_button_5 -> "10-2"
    R.id.radio_button_6 -> "9-3"
    else -> "8-4"
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

    val interfere = sharedPref?.getString("interfere", "none")
    val boundary = sharedPref?.getString("boundary", "8-4")

    when (interfere) {
      "vibrate" -> binding.radioButton1.isChecked = true
      "chime" -> binding.radioButton2.isChecked = true
      "both" -> binding.radioButton3.isChecked = true
      else -> binding.radioButton4.isChecked = true
    }

    when (boundary) {
      "10-2" -> binding.radioButton5.isChecked = true
      "9-3" -> binding.radioButton6.isChecked = true
      else -> binding.radioButton7.isChecked = true
    }

    binding.radioGroup1.setOnCheckedChangeListener { _, checkedId ->

      var interfereChoice = checkedIdToInterfere(checkedId)

      with (sharedPref?.edit()) {
        this?.putString("interfere", interfereChoice)
        this?.apply()
      }

    }

    binding.radioGroup2.setOnCheckedChangeListener { _, checkedId ->

      with (sharedPref?.edit()) {

        var boundChoice = checkedToBound(checkedId)

        this?.putString("boundary", boundChoice)
        this?.apply()
      }
    }

    return binding.root

  }


}
