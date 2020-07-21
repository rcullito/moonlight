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

  fun cancelWork(view: View) {
    val model: SensorViewModel by activityViewModels()
    model.cancelWork()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentSensorBinding>(inflater, R.layout.fragment_sensor, container, false)
    // Inflate the layout for this fragment
    return binding.root
  }

  private fun workInfoObserver(): Observer<WorkInfo> {
    return Observer { workInfo ->

      // Note that these next few lines grab a single WorkInfo if it exists
      // This code could be in a Transformation in the ViewModel; they are included here
      // so that the entire process of displaying a WorkInfo is in one location.

      // If there are no matching work info, do nothing
      if (workInfo == null) {
        return@Observer
      }

      Log.i("SensorActivity", "Observer for workInfo being called")
      Log.i("SensorActivity", workInfo.state.toString())

      if (workInfo.state.isFinished) {
        binding.status = "Work Finished Yo"
        // awesome!!! now hide CANCEL button, or at least give a back button via the nav.
      } else {
        binding.status = "Work In Progress"
      }
    }
  }
}
