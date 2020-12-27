package com.robertculliton.moonlight.dates

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.robertculliton.moonlight.database.SleepDate
import com.robertculliton.moonlight.databinding.ListItemSleepNightBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ViewHolder that holds a single [TextView].
 *
 * A ViewHolder holds a view for the [RecyclerView] as well as providing additional information
 * to the RecyclerView such as where on the screen it was last drawn during scrolling.
 */


class SleepNightListener(val clickListener: (date: String) -> Unit) {
  fun onClick(date: SleepDate) = clickListener(date.date)
}

class SleepDateAdapter(val clickListener: SleepNightListener): RecyclerView.Adapter<SleepDateAdapter.ViewHolder>() {
  var data = listOf<SleepDate>()
      set(value) {
        field = value
        notifyDataSetChanged()
      }

  override fun getItemCount(): Int {
    return data.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.from(
      parent
    )
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = data[position]
    holder.bind(clickListener, item)
  }

  class ViewHolder private constructor(val binding: ListItemSleepNightBinding): RecyclerView.ViewHolder(binding.root) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(clickListener: SleepNightListener, item: SleepDate) {

      var date = LocalDate.parse(item.date)
      var formatter = DateTimeFormatter.ofPattern("EEE, MMM dd")

      binding.date = item
      binding.display = date.format(formatter)
      binding.clickListener = clickListener
      binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
      }
    }
  }
}


