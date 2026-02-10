package com.example.esm.attendance.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.attendance.models.AttendanceModel
import com.example.esm.databinding.RecyclerviewLayoutAttendanceHistoryBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AttendanceAdapter( val attendanceList: ArrayList<AttendanceModel>, val context: Context) :RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding: RecyclerviewLayoutAttendanceHistoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_attendance_history,
            parent,
            false
        )
        return AttendanceViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
//        var formattedDate=""
      //  holder.binding.attendanceDate.text = (attendanceList[position].AttendanceDateString)
        var attendanceDateString = convertDateFormat2(attendanceList[position].AttendanceDateString)
        holder.binding.attendanceDate.text= attendanceDateString

       // holder.binding.timeIn.text =  attendanceList[position].TimeIn
        if (attendanceList[position].Status.equals("Present")) {
            holder.binding.statusAttendance.setTextColor(Color.parseColor("#05CA03"))
        }
        else{
            holder.binding.statusAttendance.setTextColor(Color.parseColor("#FB3B3B"))

        }
        holder.binding.statusAttendance.text= attendanceList[position].Status
        ////date show just time
       // Log.v("string"," original date: $originalDate")
        if (context.packageName.equals("com.bag.esm")) {
            holder.binding.timeIn.visibility= View.GONE

            val param = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.5f
            )
            param.gravity=Gravity.CENTER
            holder.binding.statusAttendance.setLayoutParams(param)
            holder.binding.attendanceDate.setLayoutParams(param)


        }
        if (attendanceList[position].TimeIn != null) {
            val originalDate= attendanceList[position].TimeIn.toString()
            var formattedDate = convertDateFormat(originalDate)
            holder.binding.timeIn.text = formattedDate

        }else{
            holder.binding.timeIn.text = null
        }

        if (context.packageName.equals("com.bass.esm")) {
            holder.binding.mainLayout.weightSum = 4f
            if (attendanceList[position].TimeOut != null) {
                val originalDate= attendanceList[position].TimeOut  .toString()
                val formattedDate = convertDateFormat(originalDate)
                 holder.binding.timeOut.text = formattedDate

            }else{
                  holder.binding.timeOut.text = null
            }
        }


    }

    private fun convertDateFormat2(date: String?): CharSequence? {
        //15 december 2022 input
        // 15 dec 2022 out
        val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date: Date = inputFormat.parse(date)
        return outputFormat.format(date)

    }

    private fun convertDateFormat(originalDate: String): String {
        /// "2022-12-22T00:00:00" input
        // "10:51 am" out
        val inputFormat =
            android.icu.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = android.icu.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(originalDate)
        return outputFormat.format(date)

    }

    class AttendanceViewHolder(val binding: RecyclerviewLayoutAttendanceHistoryBinding)
        :RecyclerView.ViewHolder(binding.root)


}