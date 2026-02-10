package com.example.esm.results.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerViewResultChildLayoutBinding
import com.example.esm.results.models.ResultDetailsModel

class ResultChildAdapter(val evaluationDetailsList: List<ResultDetailsModel>) :
    RecyclerView.Adapter<ResultChildAdapter.ResultViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder2 {
        val binding: RecyclerViewResultChildLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_result_child_layout,
            parent,
            false
        )
        return ResultViewHolder2(binding)

    }

    override fun getItemCount(): Int {
        return evaluationDetailsList.size
    }

    override fun onBindViewHolder(holder: ResultViewHolder2, position: Int) {
        holder.binding.dateResult.text = evaluationDetailsList[position].EvaluationDateString
        holder.binding.tvSubject.text = evaluationDetailsList[position].Subject
        holder.binding.tvTotalMarks.text = evaluationDetailsList[position].TotalMarks.toString()
        holder.binding.tvObtMarks.text = evaluationDetailsList[position].ObtainedMarks.toString()
        holder.binding.tvPercentage.text = evaluationDetailsList[position].Percentage.toString()
    }

    class ResultViewHolder2(val binding: RecyclerViewResultChildLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}