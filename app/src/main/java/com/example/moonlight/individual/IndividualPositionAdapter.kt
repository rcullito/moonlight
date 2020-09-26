package com.example.moonlight.individual

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moonlight.database.SleepPosition
import com.example.moonlight.databinding.ListItemSleepPositionBinding

/**
 * ViewHolder that holds a single [TextView].
 *
 * A ViewHolder holds a view for the [RecyclerView] as well as providing additional information
 * to the RecyclerView such as where on the screen it was last drawn during scrolling.
 */

class IndividualPositionAdapter(): RecyclerView.Adapter<IndividualPositionAdapter.ViewHolder>() {
  var data = listOf<SleepPosition>()
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

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = data[position]
    holder.bind(item)
  }

  class ViewHolder private constructor(val binding: ListItemSleepPositionBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SleepPosition) {
      binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemSleepPositionBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(
          binding
        )
      }
    }
  }
}



