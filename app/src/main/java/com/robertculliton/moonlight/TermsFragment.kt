package com.robertculliton.moonlight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.robertculliton.moonlight.databinding.FragmentTermsBinding

class TermsFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val binding = DataBindingUtil.inflate<FragmentTermsBinding>(inflater, R.layout.fragment_terms, container, false)

    setHasOptionsMenu(true)
    return binding.root
  }

}
