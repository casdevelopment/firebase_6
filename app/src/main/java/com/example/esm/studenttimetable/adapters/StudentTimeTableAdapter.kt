package com.example.esm.studenttimetable.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.ItemTimetableBinding
import com.example.esm.studenttimetable.models.TimeTableModel

class StudentTimeTableAdapter( val timeTableList: ArrayList<TimeTableModel>) : RecyclerView.Adapter<StudentTimeTableAdapter.TimeTableViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableViewHolder {
        val binding : ItemTimetableBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_timetable,
            parent
        ,false
        )
        return TimeTableViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return timeTableList.size
    }

    override fun onBindViewHolder(holder: TimeTableViewHolder, position: Int) {
        holder.binding.subjectName.text = timeTableList[position].SubjectName
        holder.binding.startDate.text = timeTableList[position].StartDate
        holder.binding.endDate.text = timeTableList[position].EndDate
        holder.binding.startTime.text = timeTableList[position].StartTime
        holder.binding.endTime.text = timeTableList[position].EndTime

    }
    class TimeTableViewHolder(val binding: ItemTimetableBinding) : RecyclerView.ViewHolder(binding.root)

}