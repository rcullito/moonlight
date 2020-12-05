package com.example.moonlight.sensor

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.moonlight.R
import com.example.moonlight.databinding.FragmentSensorBinding


@SuppressLint("ServiceCast")
@Suppress("DEPRECATION")
fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
  return (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
    .getRunningServices(Integer.MAX_VALUE)
    .any { it -> it.service.className == service.name }
}

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

class SensorFragment : Fragment() {

  private lateinit var binding: FragmentSensorBinding

  fun isSensorServiceRunning(): Boolean? {
    return context?.isServiceRunning(SensorService::class.java)
  }

  fun updateButtons(running: Boolean?, binding: FragmentSensorBinding) {
    if (running!!) {
      binding.cancelServiceButton.visibility = View.VISIBLE
      binding.startServiceButton.visibility = View.INVISIBLE
      binding.radioGroup1.isEnabled = false
    } else {
      binding.cancelServiceButton.visibility = View.INVISIBLE
      binding.startServiceButton.visibility = View.VISIBLE
      binding.radioGroup1.isEnabled = true
    }
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val sharedPref = activity?.getSharedPreferences(
      getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    binding = DataBindingUtil.inflate<FragmentSensorBinding>(inflater,
      R.layout.fragment_sensor, container, false)

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

    var running = isSensorServiceRunning()
    updateButtons(running, binding)

    binding.startServiceButton.setOnClickListener {
      Log.i("SensorFragment", "start service click listener called")

      context?.let { it1 ->
        SensorService.startService(
          it1,
          "Tracking Sleep Movements"
        )
      }

      var running = isSensorServiceRunning()
      updateButtons(running, binding)

    }

    binding.cancelServiceButton.setOnClickListener {
      Log.i("SensorFragment", "cancel service click listener called")
      context?.let { it1 ->
        SensorService.cancelService(
          it1
        )
      }

      var running = isSensorServiceRunning()
      updateButtons(running, binding)
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
    // Inflate the layout for this fragment
    return binding.root
  }

  override fun onResume() {
    super.onResume()
    var running = isSensorServiceRunning()
    updateButtons(running, binding)

  }

  override fun onPause() {
    super.onPause()
    binding.cancelServiceButton.visibility = View.INVISIBLE
    binding.startServiceButton.visibility = View.INVISIBLE
  }
}
