package com.example.passportphotocomparisonthesis.View

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.passportphotocomparisonthesis.databinding.FragmentModelTestBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


/**
 * A simple [Fragment] subclass.
 * Use the [ModelTestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModelTestFragment : Fragment() {

    lateinit var binding: FragmentModelTestBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentModelTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiatePlots()
    }


    fun intiatePlots(){

        val charts = arrayOf(binding.chartTP, binding.chartTN, binding.chartFP, binding.chartFN)

        val groupSpace = 0.04f
        val barSpace = 0.02f
        val barWidth = 0.45f

// (barSpace + barWidth) * 2 + groupSpace = 1
        val groupCount = 3

// Data for FaceNet and MobileFaceNet for each chart
        val faceNetValues = arrayOf(
            floatArrayOf(47932f, 94017f, 130493f),
            floatArrayOf(57633526f, 57633169f, 57630107f),
            floatArrayOf(50f, 407f, 3469f),
            floatArrayOf(105117f, 59032f, 22556f)
        )

        val mobileFaceNetValues = arrayOf(
            floatArrayOf(5000f, 14959f, 32473f),
            floatArrayOf(57632148f, 57609007f, 57419033f),
            floatArrayOf(1428f, 24569f, 214543f),
            floatArrayOf(148049f, 138090f, 120576f)
        )

        for (chartIndex in charts.indices) {
            val yVals1: ArrayList<BarEntry> = ArrayList()
            val yVals2: ArrayList<BarEntry> = ArrayList()

            for (i in 0 until groupCount) {
                yVals1.add(BarEntry(i.toFloat(), faceNetValues[chartIndex][i]))
                yVals2.add(BarEntry(i.toFloat(), mobileFaceNetValues[chartIndex][i]))
            }

            val set1 = BarDataSet(yVals1, "FaceNet")
            set1.color = Color.parseColor("#C58103")
            val set2 = BarDataSet(yVals2, "MobileFaceNet")
            set2.color = Color.parseColor("#003f5c")

            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set1)
            dataSets.add(set2)

            val data = BarData(dataSets)
            data.barWidth = barWidth
            charts[chartIndex].data = data

            // barData.getGroupWidth(...) will calculate the width each group (set of bars) will have.
            charts[chartIndex].xAxis.axisMaximum = charts[chartIndex].barData.getGroupWidth(groupSpace, barSpace) * groupCount

            charts[chartIndex].groupBars(0f, groupSpace, barSpace)

            // Set the granularity of the y-axis to reduce crowding
            charts[chartIndex].axisLeft.granularity = 1f

            // Set the x-axis labels
            val xAxisLabels = arrayOf("0.6", "0.7", "0.8")
            charts[chartIndex].xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
            charts[chartIndex].xAxis.position = XAxis.XAxisPosition.BOTTOM

            // Remove grid lines
            charts[chartIndex].xAxis.setDrawGridLines(false)
            charts[chartIndex].axisLeft.setDrawGridLines(false)
            charts[chartIndex].axisRight.setDrawGridLines(false)

            charts[chartIndex].invalidate()
        }

    }

}