package com.example.moonlight

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moonlight.database.SleepDate
import com.example.moonlight.databinding.ListItemSleepNightBinding

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
    return ViewHolder.from(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = data[position]
    holder.bind(clickListener, item)
  }

  class ViewHolder private constructor(val binding: ListItemSleepNightBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: SleepNightListener, item: SleepDate) {
      binding.date = item
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


