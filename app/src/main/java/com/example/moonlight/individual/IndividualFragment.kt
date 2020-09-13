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
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


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
      var chart: LineChart = binding.chart as LineChart
      chart.isScaleYEnabled = false

      chart.axisRight.isEnabled = false
      val xAxis = chart.xAxis
      xAxis.setDrawGridLines(false)

      val leftAxis = chart.axisLeft
      leftAxis.setDrawGridLines(false)

      var entries: ArrayList<Entry> = ArrayList()

      for (position in it) {
        entries.add(Entry(position.sleepPositionTime.toFloat(), position.pitch.toFloat()));
      }

      var dataSet = LineDataSet(entries, "Sleep")
      val lineData = LineData(dataSet)
      chart.data = lineData
    })

    return binding.root

  }

}
