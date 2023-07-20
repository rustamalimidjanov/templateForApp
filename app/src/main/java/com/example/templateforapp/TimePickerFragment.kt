package com.example.templateforapp

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.*

class TimePickerFragment : DialogFragment() {
    private val args: TimePickerFragmentArgs by navArgs()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val timeListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
                val resultTime = getTimeFromHourAndMinute(hourOfDay, minute)
                setFragmentResult(REQUEST_KEY_TIME, bundleOf(BUNDLE_KEY_TIME to resultTime))
            }

        val c = Calendar.getInstance()
        c.time = args.crimeTime
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(
            requireContext(),
            timeListener,
            hour,
            minute,
            true
        )
    }

    private fun getTimeFromHourAndMinute(hour: Int, minute: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        return calendar.time
    }



    companion object {
        const val REQUEST_KEY_TIME = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_TIME = "BUNDLE_KEY_DATE"
    }
}