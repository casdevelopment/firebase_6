package com.example.esm.studenttimetable.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.databinding.ItemTimetableBinding
import com.example.esm.studenttimetable.models.TimeTableModel

class StudentTimeTableAdapter(
    private val timeTableList: List<TimeTableModel>
) : RecyclerView.Adapter<StudentTimeTableAdapter.TimeTableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableViewHolder {
        val binding = ItemTimetableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeTableViewHolder(binding)
    }

    override fun getItemCount(): Int = timeTableList.size

    override fun onBindViewHolder(holder: TimeTableViewHolder, position: Int) {
        holder.bind(timeTableList[position])
    }

    class TimeTableViewHolder(private val binding: ItemTimetableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: TimeTableModel) {
            binding.timetable = model
            binding.executePendingBindings()
        }
    }
}