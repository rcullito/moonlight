package com.example.moonlight.individual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.moonlight.R
import com.example.moonlight.convertLongToTimeString
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter


class MyXAxisFormatter : ValueFormatter() {
  override fun getAxisLabel(value: Float, axis: AxisBase?): String {
    return convertLongToTimeString(value.toLong(), "HH:mm:ss")
  }
}
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
    individualViewModel.getSpecificDate(date)
    binding.setLifecycleOwner(this)

    individualViewModel.positions.observe(viewLifecycleOwner, Observer {
      buildChart(binding, it)
    })

    return binding.root

  }

}
