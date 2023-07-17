package com.example.templateforapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.templateforapp.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class CrimeDetailFragment : Fragment() {

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val args: CrimeDetailFragmentArgs by navArgs()
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }
    private val callback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            findNavController().navigate(R.id.crimeListFragment)
            Toast.makeText(
                requireContext(),
                "Пожалуйста, заполните поле тайтла",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }


            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let {
                        updateUi(it)
                    }
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.updateCrime {
                it.copy(date = newDate)
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(crime: Crime) {

        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            val dateFormat = DateFormat.getDateInstance(DateFormat.LONG).format(crime.date)
            crimeDate.text = dateFormat
            crimeDate.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections
                        .actionCrimeDetailFragmentToDatePickerFragment(crime.date)
                )
            }
            crimeSolved.isChecked = crime.isSolved

            if (crimeTitle.text.toString().isEmpty()) {
                callback.isEnabled = true
            }
        }

    }

}


