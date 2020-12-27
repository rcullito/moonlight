package com.robertculliton.moonlight.sensor

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.FX_KEYPRESS_INVALID
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.O)
fun motionVibrate(ctx: Context) {
  val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  val effect: VibrationEffect = VibrationEffect.createOneShot(
    1000,
    VibrationEffect.DEFAULT_AMPLITUDE
  )
  vibrator.vibrate(effect)
}

fun motionAudio(ctx: Context) {
  val am = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager
  am.playSoundEffect(FX_KEYPRESS_INVALID)
}
