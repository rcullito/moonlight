package com.robertculliton.moonlight.dates

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robertculliton.moonlight.*
import com.robertculliton.moonlight.database.SleepDatabase
import com.robertculliton.moonlight.databinding.FragmentResultsBinding


class ResultsFragment : Fragment() {

  private lateinit var binding: FragmentResultsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentResultsBinding>(inflater,
      R.layout.fragment_results, container, false)
    val application = requireNotNull(this.activity).application
    val dataSource = SleepDatabase.getInstance(application).sleepPositionDao

    val viewModelFactory =
      ResultsViewModelFactory(
        dataSource,
        application
      )
    val resultsViewModel: ResultsViewModel by activityViewModels({ viewModelFactory })
    binding.setLifecycleOwner(this)
    binding.resultsViewModel = resultsViewModel

    val adapter =
      SleepDateAdapter(SleepNightListener { date ->
        this.findNavController().navigate(
          ResultsFragmentDirections.actionResultsFragment2ToIndividualFragment(
            date
          )
        )
      })

    binding.sleepList.adapter = adapter
    resultsViewModel.dates.observe(viewLifecycleOwner, Observer {
      Log.i("ResultsFragment", "in the results fragment observer")
      it?.let {
        adapter.data = it
      }
    })

    return binding.root
  }

}
