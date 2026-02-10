package com.example.esm.studentconsultancy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.ItemStudentConsultancyBinding
import com.example.esm.studentconsultancy.models.StudentConsultancyModel

class StudentConsultancyAdapter( val consultancyList: ArrayList<StudentConsultancyModel>):
    RecyclerView.Adapter<StudentConsultancyAdapter.ConsultancyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultancyViewHolder {
        val binding: ItemStudentConsultancyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_student_consultancy,
            parent,
            false
        )
        return ConsultancyViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return consultancyList.size
    }

    override fun onBindViewHolder(holder: ConsultancyViewHolder, position: Int) {
        holder.binding.universityName.text= consultancyList[position].UniversityName
        holder.binding.program.text = consultancyList[position].Program
        holder.binding.totalSemester.text = consultancyList[position].TotalSemester.toString()
        holder.binding.feePerSemester.text= consultancyList[position].FeePerSemester.toString()
        holder.binding.totalProgramFee.text= consultancyList[position].TotalProgramFee.toString()
        holder.binding.duration.text= consultancyList[position].Duration

    }

    class ConsultancyViewHolder( val binding: ItemStudentConsultancyBinding) : RecyclerView.ViewHolder(binding.root)

}