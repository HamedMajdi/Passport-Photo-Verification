package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.SpinnerData

class SpinnerAdapter(context: Context, private val options: List<SpinnerData>) :
    ArrayAdapter<SpinnerData>(context, 0, options) {

    override fun isEnabled(position: Int): Boolean {
        // Disable the first item from Spinner
        // First item will be used for hint
        return position != 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)

    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val view = recycledView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item, parent, false)

        val option = getItem(position)

        val imageView = view.findViewById<ImageView>(R.id.image)
        val textView = view.findViewById<TextView>(R.id.text)

        if (option != null) {
            imageView.setImageResource(option.imageResId)
            textView.text = option.text
        }
        return view
    }
}
