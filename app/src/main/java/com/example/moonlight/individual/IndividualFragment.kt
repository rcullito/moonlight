package com.example.moonlight.individual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.moonlight.*
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.databinding.FragmentIndividualBinding


class IndividualFragment : Fragment() {

  private lateinit var binding: FragmentIndividualBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentIndividualBinding>(inflater,
      R.layout.fragment_individual, container, false)
    val application = requireNotNull(this.activity).application
    val dataSource = SleepDatabase.getInstance(application).sleepPositionDao

    val arguments =
      IndividualFragmentArgs.fromBundle(
        requireArguments()
      )
    val date = arguments.date

    val viewModelFactory =
      IndividualViewModelFactory(
        dataSource,
        application
      )
    val individualViewModel: IndividualViewModel by activityViewModels({ viewModelFactory })
    binding.setLifecycleOwner(this)

    individualViewModel.getSpecificDate(date)

    binding.individualViewModel = individualViewModel

    val adapter = IndividualPositionAdapter()
    binding.individualList.adapter = adapter
    individualViewModel.positions.observe(viewLifecycleOwner, Observer {
      it?.let {
        adapter.data = it
      }
    })

    return binding.root
  }

}
