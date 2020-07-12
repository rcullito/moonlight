package com.example.diceroller.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class LogWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
  override fun doWork(): Result {

    Log.i("LogWorker", "Shut the door Alec, there's a draft")

    return Result.success()
  }
}
