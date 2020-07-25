package com.example.diceroller

import android.app.Application
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.example.diceroller.database.SleepPosition
import com.example.diceroller.database.SleepPositionDao
import kotlinx.coroutines.*

class ResultsViewModel(val database: SleepPositionDao, application: Application): AndroidViewModel(application) {

  private var viewModelJob = Job()
  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }

  private val positions = database.getAllPositions()

  val positionsString = Transformations.map(positions) {
    formatPosition(it)
  }

  fun formatPosition(positions: List<SleepPosition>): Spanned {
    val sb = StringBuilder()
    sb.apply {
      append("<h3>Here are your recent recorded positions</h3>")
      positions.forEach {
        append("<br>")
        append("<b>Pitch:</b>")
        append(it.pitch.toString())
      }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
      return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
  }



}
