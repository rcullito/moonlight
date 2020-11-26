package com.example.moonlight.sensor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.moonlight.R
import com.example.moonlight.databinding.FragmentSensorBinding


class SensorFragment : Fragment() {

  private lateinit var binding: FragmentSensorBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentSensorBinding>(inflater,
      R.layout.fragment_sensor, container, false)

    binding.startServiceButton.setOnClickListener {
      Log.i("SensorFragment", "start service click listener called")

      context?.let { it1 ->
        SensorService.startService(it1)
      }
    }

    binding.cancelServiceButton.setOnClickListener {
      Log.i("SensorFragment", "cancel service click listener called")
      context?.let { it1 ->
        SensorService.cancelService(it1)
      }

    }
    // Inflate the layout for this fragment
    return binding.root
  }
}
