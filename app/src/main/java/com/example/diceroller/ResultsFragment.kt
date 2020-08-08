package com.example.diceroller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.diceroller.database.SleepDatabase
import com.example.diceroller.databinding.FragmentResultsBinding


class ResultsFragment : Fragment() {

  private lateinit var binding: FragmentResultsBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate<FragmentResultsBinding>(inflater, R.layout.fragment_results, container, false)
    val application = requireNotNull(this.activity).application
    val dataSource = SleepDatabase.getInstance(application).sleepPositionDao

    val viewModelFactory = ResultsViewModelFactory(dataSource, application)
    val resultsViewModel: ResultsViewModel by activityViewModels({ viewModelFactory })
    binding.setLifecycleOwner(this)
    binding.resultsViewModel = resultsViewModel

    val adapter = SleepDateAdapter()
    binding.sleepList.adapter = adapter
    resultsViewModel.nights.observe(viewLifecycleOwner, Observer {
      it?.let {
        adapter.data = it
      }
    })



    return binding.root
  }

}
