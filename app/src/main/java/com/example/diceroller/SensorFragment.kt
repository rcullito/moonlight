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
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import com.example.diceroller.database.SleepDatabase
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


    val model: SensorViewModel by activityViewModels()

    // if the sensor has already been initialized
    // then let the current state of the job determine the button state
    // otherwise set start to visible assuming this is the
    // first interaction with the app
    if (model.isSensorWorkInfoInitialised()) {
      model.sensorWorkInfo.observe(viewLifecycleOwner, workInfoObserver())
    } else {
      binding.startServiceButton.visibility = View.VISIBLE
    }


    binding.startServiceButton.setOnClickListener {
      Log.i("SensorFragment", "first click listener called")
      val model: SensorViewModel by activityViewModels()
      model.startService()

      // just double checking. but it looks like by the time we add an observer here
      // it will always be the first of its kind :)
      // Log.i("RightBeforeObserver", model.sensorWorkInfo.hasObservers().toString())
      // model.sensorWorkInfo.observe(viewLifecycleOwner, workInfoObserver())
    }

    binding.cancelServiceButton.setOnClickListener {
      Log.i("SensorFragment", "cancel work onClickListener Being Called")
      val model: SensorViewModel by activityViewModels()
      model.cancelService()
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
        binding.cancelServiceButton.visibility = View.GONE
        binding.startServiceButton.visibility = View.VISIBLE

      } else {
        binding.status = "Work In Progress"
        binding.startServiceButton.visibility = View.GONE
        binding.cancelServiceButton.visibility = View.VISIBLE
      }
    }
  }
}
