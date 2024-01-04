package com.example.passportphotocomparisonthesis.Settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.passportphotocomparisonthesis.R

class SpinnerAdapter(context: Context, private val images: IntArray, private val names: Array<String>)
    : ArrayAdapter<String>(context, R.layout.spinner_item, names) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.image)
        val textView = view.findViewById<TextView>(R.id.text)

        imageView.setImageResource(images[position])
        textView.text = names[position]

        return view
    }
}
