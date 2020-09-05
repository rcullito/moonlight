package com.example.moonlight.sensor

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.O)
fun motionVibrate(ctx: Context) {
  val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  val effect: VibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
  vibrator.vibrate(effect)
}

