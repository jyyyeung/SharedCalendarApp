package com.example.sharedcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter(context: Context, items: List<Color>) :
    ArrayAdapter<Color>(context, 0, items) {
    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = layoutInflater.inflate(R.layout.blank_layout, null, true)
        return view(view, position)

    }

    private fun view(view: View, position: Int): View {
        val color: Color = getItem(position) ?: return view
        val colorText = view.findViewById<TextView>(R.id.colorText)
        val colorView = view.findViewById<View>(R.id.colorView)
        colorView?.background?.setTint(android.graphics.Color.parseColor(color.code))
        colorText?.text = color.name

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        if (cv == null)
            cv = layoutInflater.inflate(R.layout.spinner_layout, parent, false)
        return view(cv!!, position)
    }
}