package com.example.diceroller

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.example.diceroller.databinding.ActivitySensorBinding

class SensorActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySensorBinding

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_sensor)

    Log.i("SensorActivity", "onCreate Called yipee")

    val model: SensorViewModel by viewModels()

    model.startLogWorker()

    model.sensorWorkInfo.observe(this, workInfoObserver())

  }

  fun cancelWork(view: View) {
    val model: SensorViewModel by viewModels()
    model.cancelWork()
  }

  // Add this functions
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
