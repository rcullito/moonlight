package com.example.diceroller.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class LogWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
  override fun doWork(): Result {
    TODO("Not yet implemented")
  }
}
