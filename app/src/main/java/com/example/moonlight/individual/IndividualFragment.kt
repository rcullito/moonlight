package com.example.moonlight.individual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moonlight.R
import com.example.moonlight.database.SleepDatabase
import com.example.moonlight.databinding.FragmentIndividualBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.*


class IndividualFragment : Fragment() {

  private lateinit var binding: FragmentIndividualBinding

  var hpJob = Job()
  private val hpScope = CoroutineScope(Dispatchers.Default + hpJob)

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

    binding.individualViewModel = individualViewModel

    hpScope.launch {
      withContext(Dispatchers.IO) {
        individualViewModel.getSpecificDate(date)
        val chart: LineChart = binding.chart as LineChart

        var positions = individualViewModel.positions
        var entries: ArrayList<Entry> = ArrayList()

        for (position in positions) {
          // turn your data into Entry objects
          entries.add(Entry(position.sleepPositionTime.toFloat(), position.pitch.toFloat()));
        }

        var dataSet = LineDataSet(entries, "Sleep")
        val lineData = LineData(dataSet)
        chart.data = lineData

      }

    }

    return binding.root

  }

}
