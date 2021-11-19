package com.uv.expressit

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment (val listener: (day:Int, month:Int, year:Int) -> Unit): DialogFragment(), DatePickerDialog.OnDateSetListener{

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth, month, year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendario = Calendar.getInstance()
        val diaActual = calendario.get(Calendar.DAY_OF_MONTH)
        val mesActual = calendario.get(Calendar.MONTH)
        val anioActual = calendario.get(Calendar.YEAR)

        val picker = DatePickerDialog(activity as Context, this, anioActual, mesActual, diaActual)
        picker.datePicker.maxDate = calendario.timeInMillis
        return picker

    }
}