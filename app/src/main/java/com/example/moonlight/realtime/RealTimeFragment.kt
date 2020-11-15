package com.example.moonlight.realtime


import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Display
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment


class RealTimeFragment: Fragment(), SensorEventListener {
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

  // TextViews to display current sensor values.
  private var mTextSensorAzimuth: TextView? = null
  private var mTextSensorPitch: TextView? = null
  private var mTextSensorRoll: TextView? = null

  // ImageView drawables to display spots.


  // System display. Need this for determining rotation.
  private var mDisplay: Display? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    // Get accelerometer and magnetometer sensors from the sensor manager.
    // The getDefaultSensor() method returns null if the sensor
    // is not available on the device.
    mSensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

    mSensorAccelerometer = mSensorManager!!.getDefaultSensor(
      Sensor.TYPE_ACCELEROMETER
    )
    mSensorMagnetometer = mSensorManager!!.getDefaultSensor(
      Sensor.TYPE_MAGNETIC_FIELD
    )

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

  override fun onStop() {
    super.onStop()

    // Unregister all sensor listeners in this callback so they don't
    // continue to use resources when the app is stopped.
    mSensorManager!!.unregisterListener(this)
  }

  override fun onSensorChanged(sensorEvent: SensorEvent) {

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
