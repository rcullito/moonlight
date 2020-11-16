package com.example.moonlight.realtime


import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.moonlight.R
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.example.moonlight.databinding.FragmentRealTimeBinding
import com.example.moonlight.sensor.orientationAngles
import com.example.moonlight.sensor.rotationMatrix


class RealTimeFragment: Fragment(), SensorEventListener {

  private lateinit var binding: FragmentRealTimeBinding
  // System sensor manager instance.
  private var mSensorManager: SensorManager? = null

  // Accelerometer and magnetometer sensors, as retrieved from the
  // sensor manager.
  private var mSensorAccelerometer: Sensor? = null
  private var mSensorMagnetometer: Sensor? = null

  // Current data from accelerometer & magnetometer.  The arrays hold values
  // for X, Y, and Z.
  private var mAccelerometerData = FloatArray(3)
  private var mMagnetometerData = FloatArray(3)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    // Get accelerometer and magnetometer sensors from the sensor manager.
    // The getDefaultSensor() method returns null if the sensor
    // is not available on the device.
    mSensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

    mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).also { accelerometer ->
      Log.i("SensorService", "registering sensor listener")
      mSensorManager!!.registerListener(
        this,
        accelerometer,
        SensorManager.SENSOR_DELAY_NORMAL,
        // When maxReportLatencyUs is 0 it requires the events to be delivered as soon as possible
        0
      )
    }
    mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).also { magneticField ->
      mSensorManager!!.registerListener(
        this,
        magneticField,
        SensorManager.SENSOR_DELAY_NORMAL,
        0
      )
    }

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate<FragmentRealTimeBinding>(inflater,
      R.layout.fragment_real_time, container, false)

    binding.setLifecycleOwner(this)

    return binding.root
  }

  /**
   * Listeners for the sensors are registered in this callback so that
   * they can be unregistered in onStop().
   */
  override fun onStart() {
    super.onStart()

    // Listeners for the sensors are registered in this callback and
    // can be unregistered in onStop().
    //
    // Check to ensure sensors are available before registering listeners.
    // Both listeners are registered with a "normal" amount of delay
    // (SENSOR_DELAY_NORMAL).
    if (mSensorAccelerometer != null) {
      mSensorManager!!.registerListener(
        this, mSensorAccelerometer,
        SensorManager.SENSOR_DELAY_NORMAL
      )
    }
    if (mSensorMagnetometer != null) {
      mSensorManager!!.registerListener(
        this, mSensorMagnetometer,
        SensorManager.SENSOR_DELAY_NORMAL
      )
    }
  }

  override fun onPause() {
    super.onPause()
    // Unregister all sensor listeners in this callback so they don't
    // continue to use resources when the app is stopped.
    mSensorManager!!.unregisterListener(this)
  }

  override fun onStop() {
    super.onStop()
  }

  override fun onSensorChanged(event: SensorEvent) {
    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
      System.arraycopy(event.values, 0, mAccelerometerData, 0, mAccelerometerData.size)
    } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
      System.arraycopy(event.values, 0, mMagnetometerData, 0, mMagnetometerData.size)
    }

    SensorManager.getRotationMatrix(rotationMatrix, null, mAccelerometerData, mMagnetometerData)
    SensorManager.getOrientation(rotationMatrix, orientationAngles)


    var roll = orientationAngles.get(2)
    binding.roll = roll.toString()
  }

  /**
   * Must be implemented to satisfy the SensorEventListener interface;
   * unused in this app.
   */
  override fun onAccuracyChanged(sensor: Sensor, i: Int) {}

  companion object {
    // Very small values for the accelerometer (on all three axes) should
    // be interpreted as 0. This value is the amount of acceptable
    // non-zero drift.
    private const val VALUE_DRIFT = 0.05f
  }
}
