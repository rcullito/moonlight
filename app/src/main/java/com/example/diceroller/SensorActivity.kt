package com.example.diceroller

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.WorkInfo

class SensorActivity : AppCompatActivity() {

  private var mTextWorkStatus: TextView? = null

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sensor)
    Log.i("SensorActivity", "onCreate Called yipee")

    mTextWorkStatus= findViewById(R.id.workStatus) as TextView

    val model: SensorViewModel by viewModels()

    model.startLogWorker()

    model.sensorWorkInfo.observe(this, workInfosObserver())

  }

  fun cancelWork(view: View) {
    val model: SensorViewModel by viewModels()
    model.cancelWork()
  }

  // Add this functions
  private fun workInfosObserver(): Observer<WorkInfo> {
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
        mTextWorkStatus?.setText("Work Finished")
        // awesome!!! now hide CANCEL button, or at least give a back button via the nav.
      } else {
        mTextWorkStatus?.setText("Work In Progress")
      }
    }
  }

}
