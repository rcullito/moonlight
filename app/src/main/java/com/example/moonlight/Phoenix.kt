package com.example.moonlight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.example.moonlight.databinding.FragmentPhoenixBinding
import com.example.moonlight.individual.IndividualFragmentArgs
import com.example.moonlight.individual.IndividualViewModel
import com.example.moonlight.individual.IndividualViewModelFactory
import com.example.moonlight.individual.buildChart

class Phoenix: Fragment() {

  private lateinit var binding: FragmentPhoenixBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentPhoenixBinding>(inflater,
      R.layout.fragment_phoenix, container, false)

    binding.setLifecycleOwner(this)


    return binding.root

  }


}



