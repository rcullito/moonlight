package com.example.diceroller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ListAdapter
import com.example.diceroller.databinding.FragmentDateBinding

class DateFragment : Fragment() {

  private lateinit var binding: FragmentDateBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentDateBinding>(inflater, R.layout.fragment_date, container, false)
    // Inflate the layout for this fragment
    val dateViewModel: DateViewModel by activityViewModels()
    binding.setLifecycleOwner(this)
    binding.dateViewModel = dateViewModel
    return binding.root
  }
}
