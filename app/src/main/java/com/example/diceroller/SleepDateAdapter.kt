package com.example.diceroller

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * ViewHolder that holds a single [TextView].
 *
 * A ViewHolder holds a view for the [RecyclerView] as well as providing additional information
 * to the RecyclerView such as where on the screen it was last drawn during scrolling.
 */
class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

class SleepDateAdapter: RecyclerView.Adapter<TextItemViewHolder>() {
  var data = listOf<String>()

  override fun getItemCount(): Int {
    data.size
  }

  override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
    val item = data[position]
    holder.textView.text = item.toString()
  }
}
