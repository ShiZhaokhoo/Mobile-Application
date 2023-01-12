package com.example.assignm.fragments

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.assignm.Database
import com.example.assignm.R
import com.example.assignm.databinding.FragmentReportBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.lang.Float.parseFloat

data class DailyReceive(var part:String,var qty:Int)
data class MonthlyReceive(var part:String,var qty:Int)
data class YearlyReceive(var part:String,var qty:Int)
data class DailyIssued(var part:String,var qty:Int)
data class MonthlyIssued(var part:String,var qty:Int)
data class YearlyIssued(var part:String,var qty:Int)

class ReportFragment : Fragment() {

    private lateinit var binding:FragmentReportBinding
    lateinit var barList:ArrayList<Entry>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_report,container,false)

        val dailyIssuedList: MutableList<DailyIssued> = mutableListOf()
        val monthlyIssuedList: MutableList<MonthlyIssued> = mutableListOf()
        val yearlyIssuedList: MutableList<YearlyIssued> = mutableListOf()
        val dailyReceiveList: MutableList<DailyReceive> = mutableListOf()
        val monthlyReceiveList: MutableList<MonthlyReceive> = mutableListOf()
        val yearlyReceiveList: MutableList<YearlyReceive> = mutableListOf()

        for(material in Database.materials){
            dailyReceiveList.add(DailyReceive(material.getPartNumber(),material.getDailyReceive()))
            monthlyReceiveList.add(MonthlyReceive(material.getPartNumber(),material.getMonthlyReceive()))
            yearlyReceiveList.add(YearlyReceive(material.getPartNumber(),material.getYearlyReceive()))
            dailyIssuedList.add(DailyIssued(material.getPartNumber(),material.getDailyIssued()))
            monthlyIssuedList.add(MonthlyIssued(material.getPartNumber(),material.getMonthlyIssued()))
            yearlyIssuedList.add(YearlyIssued(material.getPartNumber(),material.getYearlyIssued()))
            Log.w(TAG, material.getMonthlyReceive().toString())
            Log.w(TAG, material.getYearlyReceive().toString())
        }

        binding.issuedBtn.setOnClickListener{
            binding.dailyReceiveSet.visibility = View.GONE
            binding.monthlyReceiveSet.visibility = View.GONE
            binding.yearlyReceiveSet.visibility = View.GONE
            binding.dailyIssuedSet.visibility = View.VISIBLE
            binding.monthlyIssuedSet.visibility = View.VISIBLE
            binding.yearlyIssuedSet.visibility = View.VISIBLE
        }

        binding.receiveBtn.setOnClickListener{
            binding.dailyReceiveSet.visibility = View.VISIBLE
            binding.monthlyReceiveSet.visibility = View.VISIBLE
            binding.yearlyReceiveSet.visibility = View.VISIBLE
            binding.dailyIssuedSet.visibility = View.GONE
            binding.monthlyIssuedSet.visibility = View.GONE
            binding.yearlyIssuedSet.visibility = View.GONE
        }

        dailyReceiveList.sortByDescending {dailyReceiveList -> dailyReceiveList.qty}
        monthlyReceiveList.sortByDescending { monthlyReceiveList -> monthlyReceiveList.qty }
        yearlyReceiveList.sortByDescending { yearlyReceiveList -> yearlyReceiveList.qty }
        dailyIssuedList.sortByDescending { dailyIssuedList -> dailyIssuedList.qty }
        monthlyIssuedList.sortByDescending { monthlyIssuedList -> monthlyIssuedList.qty }
        yearlyIssuedList.sortByDescending { yearlyIssuedList -> yearlyIssuedList.qty }

        val ReceiveEntries = arrayListOf(
            BarEntry(1f, parseFloat(dailyReceiveList[0].qty.toString()))
        )
        val ReceiveEntries2 = arrayListOf(
            BarEntry(2f, parseFloat(dailyReceiveList[1].qty.toString()))
        )
        val ReceiveEntries3 = arrayListOf(
            BarEntry(3f, parseFloat(dailyReceiveList[2].qty.toString()))
        )
        val ReceiveEntries4 = arrayListOf(
            BarEntry(4f, parseFloat(dailyReceiveList[3].qty.toString()))
        )
        val ReceiveEntries5 = arrayListOf(
            BarEntry(5f, parseFloat(dailyReceiveList[4].qty.toString()))
        )
        //binding.receiveBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(receiveLabels)
        binding.receiveBarChartDaily.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.receiveBarChartDaily.axisRight.isEnabled = false
        binding.receiveBarChartDaily.setDrawGridBackground(false)

        val bars: MutableList<IBarDataSet> = ArrayList()
        val receiveDataset1 = BarDataSet(ReceiveEntries, dailyReceiveList[0].part)
        receiveDataset1.color = Color.RED
        bars.add(receiveDataset1)
        val receiveDataset2 = BarDataSet(ReceiveEntries2, dailyReceiveList[1].part)
        receiveDataset2.color = Color.BLUE
        bars.add(receiveDataset2)
        val receiveDataset3 = BarDataSet(ReceiveEntries3, dailyReceiveList[2].part)
        receiveDataset3.color = Color.GREEN
        bars.add(receiveDataset3)
        val receiveDataset4 = BarDataSet(ReceiveEntries4, dailyReceiveList[3].part)
        receiveDataset4.color = Color.GRAY
        bars.add(receiveDataset4)
        val receiveDataset5 = BarDataSet(ReceiveEntries5, dailyReceiveList[4].part)
        receiveDataset5.color = Color.BLACK
        bars.add(receiveDataset5)
        val data = BarData(bars)
        binding.receiveBarChartDaily.setData(data)

        //second set
        val ReceiveEntries11 = arrayListOf(
            BarEntry(1f, parseFloat(monthlyReceiveList[0].qty.toString()))
        )
        val ReceiveEntries12 = arrayListOf(
            BarEntry(2f, parseFloat(monthlyReceiveList[1].qty.toString()))
        )
        val ReceiveEntries13 = arrayListOf(
            BarEntry(3f, parseFloat(monthlyReceiveList[2].qty.toString()))
        )
        val ReceiveEntries14 = arrayListOf(
            BarEntry(4f, parseFloat(monthlyReceiveList[3].qty.toString()))
        )
        val ReceiveEntries15 = arrayListOf(
            BarEntry(5f, parseFloat(monthlyReceiveList[4].qty.toString()))
        )
        //binding.receiveBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(receiveLabels)
        binding.receiveBarChartMonthly.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.receiveBarChartMonthly.axisRight.isEnabled = false
        binding.receiveBarChartMonthly.setDrawGridBackground(false)

        val bars3: MutableList<IBarDataSet> = ArrayList()
        val receiveDataset11 = BarDataSet(ReceiveEntries11, monthlyReceiveList[0].part)
        receiveDataset11.color = Color.RED
        bars3.add(receiveDataset11)
        val receiveDataset12 = BarDataSet(ReceiveEntries12, monthlyReceiveList[1].part)
        receiveDataset12.color = Color.BLUE
        bars3.add(receiveDataset12)
        val receiveDataset13 = BarDataSet(ReceiveEntries13, monthlyReceiveList[2].part)
        receiveDataset13.color = Color.GREEN
        bars3.add(receiveDataset13)
        val receiveDataset14 = BarDataSet(ReceiveEntries14, monthlyReceiveList[3].part)
        receiveDataset14.color = Color.GRAY
        bars3.add(receiveDataset14)
        val receiveDataset15 = BarDataSet(ReceiveEntries15, monthlyReceiveList[4].part)
        receiveDataset15.color = Color.BLACK
        bars3.add(receiveDataset15)
        val data3 = BarData(bars3)
        binding.receiveBarChartMonthly.setData(data3)

        //third set
        val ReceiveEntries21 = arrayListOf(
            BarEntry(1f, parseFloat(yearlyReceiveList[0].qty.toString()))
        )
        val ReceiveEntries22 = arrayListOf(
            BarEntry(2f, parseFloat(yearlyReceiveList[1].qty.toString()))
        )
        val ReceiveEntries23 = arrayListOf(
            BarEntry(3f, parseFloat(yearlyReceiveList[2].qty.toString()))
        )
        val ReceiveEntries24 = arrayListOf(
            BarEntry(4f, parseFloat(yearlyReceiveList[3].qty.toString()))
        )
        val ReceiveEntries25 = arrayListOf(
            BarEntry(5f, parseFloat(yearlyReceiveList[4].qty.toString()))
        )
        //binding.receiveBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(receiveLabels)
        binding.receiveBarChartYearly.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.receiveBarChartYearly.axisRight.isEnabled = false
        binding.receiveBarChartYearly.setDrawGridBackground(false)

        val bars5: MutableList<IBarDataSet> = ArrayList()
        val receiveDataset21 = BarDataSet(ReceiveEntries21, yearlyReceiveList[0].part)
        receiveDataset21.color = Color.RED
        bars5.add(receiveDataset21)
        val receiveDataset22 = BarDataSet(ReceiveEntries22, yearlyReceiveList[1].part)
        receiveDataset22.color = Color.BLUE
        bars5.add(receiveDataset22)
        val receiveDataset23 = BarDataSet(ReceiveEntries23, yearlyReceiveList[2].part)
        receiveDataset23.color = Color.GREEN
        bars5.add(receiveDataset23)
        val receiveDataset24 = BarDataSet(ReceiveEntries24, yearlyReceiveList[3].part)
        receiveDataset24.color = Color.GRAY
        bars5.add(receiveDataset24)
        val receiveDataset25 = BarDataSet(ReceiveEntries25, yearlyReceiveList[4].part)
        receiveDataset25.color = Color.BLACK
        bars5.add(receiveDataset25)
        val data5 = BarData(bars5)
        binding.receiveBarChartYearly.setData(data5)

        //for issued daily chart
        val IssuedEntries = arrayListOf(
            BarEntry(1f, parseFloat(dailyIssuedList[0].qty.toString()))
        )
        val IssuedEntries2 = arrayListOf(
            BarEntry(2f, parseFloat(dailyIssuedList[1].qty.toString()))
        )
        val IssuedEntries3 = arrayListOf(
            BarEntry(3f, parseFloat(dailyIssuedList[2].qty.toString()))
        )
        val IssuedEntries4 = arrayListOf(
            BarEntry(4f, parseFloat(dailyIssuedList[3].qty.toString()))
        )
        val IssuedEntries5 = arrayListOf(
            BarEntry(5f, parseFloat(dailyIssuedList[4].qty.toString()))
        )
        //binding.receiveBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(receiveLabels)
        binding.issuedBarChartDaily.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.issuedBarChartDaily.axisRight.isEnabled = false
        binding.issuedBarChartDaily.setDrawGridBackground(false)

        val bars2: MutableList<IBarDataSet> = ArrayList()
        val issuedDataset1 = BarDataSet(IssuedEntries, dailyIssuedList[0].part)
        issuedDataset1.color = Color.RED
        bars2.add(issuedDataset1)
        val issuedDataset2 = BarDataSet(IssuedEntries2, dailyIssuedList[1].part)
        issuedDataset2.color = Color.BLUE
        bars2.add(issuedDataset2)
        val issuedDataset3 = BarDataSet(IssuedEntries3, dailyIssuedList[2].part)
        issuedDataset3.color = Color.GREEN
        bars2.add(issuedDataset3)
        val issuedDataset4 = BarDataSet(IssuedEntries4, dailyIssuedList[3].part)
        issuedDataset4.color = Color.GRAY
        bars2.add(issuedDataset4)
        val issuedDataset5 = BarDataSet(IssuedEntries5, dailyIssuedList[4].part)
        issuedDataset5.color = Color.BLACK
        bars2.add(issuedDataset5)

        val data2 = BarData(bars2)
        binding.issuedBarChartDaily.setData(data2)

        //monthly
        //for issued daily chart
        val IssuedEntries6 = arrayListOf(
            BarEntry(1f, parseFloat(monthlyIssuedList[0].qty.toString()))
        )
        val IssuedEntries7 = arrayListOf(
            BarEntry(2f, parseFloat(monthlyIssuedList[1].qty.toString()))
        )
        val IssuedEntries8 = arrayListOf(
            BarEntry(3f, parseFloat(monthlyIssuedList[2].qty.toString()))
        )
        val IssuedEntries9 = arrayListOf(
            BarEntry(4f, parseFloat(monthlyIssuedList[3].qty.toString()))
        )
        val IssuedEntries10 = arrayListOf(
            BarEntry(5f, parseFloat(monthlyIssuedList[4].qty.toString()))
        )
        //binding.receiveBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(receiveLabels)
        binding.issuedBarChartMonthly.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.issuedBarChartMonthly.axisRight.isEnabled = false
        binding.issuedBarChartMonthly.setDrawGridBackground(false)

        val bars4: MutableList<IBarDataSet> = ArrayList()
        val issuedDataset6 = BarDataSet(IssuedEntries6, monthlyIssuedList[0].part)
        issuedDataset6.color = Color.RED
        bars4.add(issuedDataset6)
        val issuedDataset7 = BarDataSet(IssuedEntries7, monthlyIssuedList[1].part)
        issuedDataset7.color = Color.BLUE
        bars4.add(issuedDataset7)
        val issuedDataset8 = BarDataSet(IssuedEntries8, monthlyIssuedList[2].part)
        issuedDataset8.color = Color.GREEN
        bars4.add(issuedDataset8)
        val issuedDataset9 = BarDataSet(IssuedEntries9, monthlyIssuedList[3].part)
        issuedDataset9.color = Color.GRAY
        bars4.add(issuedDataset9)
        val issuedDataset10 = BarDataSet(IssuedEntries10, monthlyIssuedList[4].part)
        issuedDataset10.color = Color.BLACK
        bars4.add(issuedDataset10)

        val data4 = BarData(bars4)
        binding.issuedBarChartMonthly.setData(data4)

        //yearly
        val IssuedEntries11 = arrayListOf(
            BarEntry(1f, parseFloat(yearlyIssuedList[0].qty.toString()))
        )
        val IssuedEntries12 = arrayListOf(
            BarEntry(2f, parseFloat(yearlyIssuedList[1].qty.toString()))
        )
        val IssuedEntries13 = arrayListOf(
            BarEntry(3f, parseFloat(yearlyIssuedList[2].qty.toString()))
        )
        val IssuedEntries14 = arrayListOf(
            BarEntry(4f, parseFloat(yearlyIssuedList[3].qty.toString()))
        )
        val IssuedEntries15 = arrayListOf(
            BarEntry(5f, parseFloat(yearlyIssuedList[4].qty.toString()))
        )
        //binding.receiveBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(receiveLabels)
        binding.issuedBarChartYearly.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.issuedBarChartYearly.axisRight.isEnabled = false
        binding.issuedBarChartYearly.setDrawGridBackground(false)

        val bars6: MutableList<IBarDataSet> = ArrayList()
        val issuedDataset11 = BarDataSet(IssuedEntries11, yearlyIssuedList[0].part)
        issuedDataset11.color = Color.RED
        bars6.add(issuedDataset11)
        val issuedDataset12 = BarDataSet(IssuedEntries12, yearlyIssuedList[1].part)
        issuedDataset12.color = Color.BLUE
        bars6.add(issuedDataset12)
        val issuedDataset13 = BarDataSet(IssuedEntries13, yearlyIssuedList[2].part)
        issuedDataset13.color = Color.GREEN
        bars6.add(issuedDataset13)
        val issuedDataset14 = BarDataSet(IssuedEntries14, yearlyIssuedList[3].part)
        issuedDataset14.color = Color.GRAY
        bars6.add(issuedDataset14)
        val issuedDataset15 = BarDataSet(IssuedEntries15, yearlyIssuedList[4].part)
        issuedDataset15.color = Color.BLACK
        bars6.add(issuedDataset15)

        val data6= BarData(bars6)
        binding.issuedBarChartYearly.setData(data6)

        return binding.root
    }
}