package com.example.diceroller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.example.diceroller.databinding.FragmentSensorBinding


class SensorFragment : Fragment() {

  private lateinit var binding: FragmentSensorBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentSensorBinding>(inflater, R.layout.fragment_sensor, container, false)
    // TODO split this into its own fn
    binding.startWorkButton.setOnClickListener {
      Log.i("SensorFragment", "onclickListener Being Called")
      val model: SensorViewModel by activityViewModels()

      // starting work will have to be the default
      model.startSensorWorker()
      model.sensorWorkInfo.observe(viewLifecycleOwner, workInfoObserver())
    }
    // Inflate the layout for this fragment
    return binding.root
  }

  private fun workInfoObserver(): Observer<List<WorkInfo>> {
    return Observer { listOfWorkInfo ->

      if (listOfWorkInfo.isNullOrEmpty()) {
        return@Observer
      }

      val workInfo = listOfWorkInfo[0]

      Log.i("SensorFragment", "Observer for workInfo being called")
      Log.i("SensorFragment", workInfo.state.toString())

      if (workInfo.state.isFinished) {
        binding.status = "Work Finished"
        // awesome!!! now hide CANCEL button, or at least give a back button via the nav.
        //      model.cancelWork()
      } else {
        binding.status = "Work In Progress"
        binding.startWorkButton.visibility = View.GONE
      }
    }
  }
}
