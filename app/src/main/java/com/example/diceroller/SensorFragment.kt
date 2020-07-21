package com.example.diceroller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.example.diceroller.databinding.FragmentSensorBinding


class SensorFragment : Fragment() {

  private lateinit var binding: FragmentSensorBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val model: SensorViewModel by activityViewModels()

    model.startLogWorker()

    model.sensorWorkInfo.observe(this, workInfoObserver())

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentSensorBinding>(inflater, R.layout.fragment_sensor, container, false)

    binding.cancelWorkButton.setOnClickListener {
      val model: SensorViewModel by activityViewModels()
      model.cancelWork()
    }
    // Inflate the layout for this fragment
    return binding.root
  }

  private fun workInfoObserver(): Observer<WorkInfo> {
    return Observer { workInfo ->

      if (workInfo == null) {
        return@Observer
      }

      Log.i("SensorFragment", "Observer for workInfo being called")
      Log.i("SensorFragment", workInfo.state.toString())

      if (workInfo.state.isFinished) {
        binding.status = "Work Finished"
        // awesome!!! now hide CANCEL button, or at least give a back button via the nav.
      } else {
        binding.status = "Work In Progress"
      }
    }
  }
}
