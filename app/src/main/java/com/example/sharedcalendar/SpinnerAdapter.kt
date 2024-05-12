package com.example.sharedcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Adapter for the spinner that displays a list of colors.
 *
 * @param context The context of the application.
 * @param items The list of colors to be displayed in the spinner.
 */
class SpinnerAdapter(context: Context, items: List<Color>) :
    ArrayAdapter<Color>(context, 0, items) {
    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View {
        val view: View = layoutInflater.inflate(R.layout.blank_layout, null, true)
        return view(view, position)
    }

    /**
     * Represents a private function that handles the creation of a view.
     */
    private fun view(
        view: View,
        position: Int,
    ): View {
        val color: Color = getItem(position) ?: return view
        val colorText = view.findViewById<TextView>(R.id.colorText)
        val colorView = view.findViewById<View>(R.id.colorView)
        colorView?.background?.setTint(android.graphics.Color.parseColor(color.code))
        colorText?.text = color.name

        return view
    }

    /**
     * Returns the view that is displayed when the spinner is expanded and the dropdown list is shown.
     *
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return The view corresponding to the specified position in the data set.
     */
    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View {
        var cv = convertView
        if (cv == null) {
            cv = layoutInflater.inflate(R.layout.spinner_layout, parent, false)
        }
        return view(cv!!, position)
    }
}
