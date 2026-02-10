package com.example.esm.results.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutResultLayoutBinding
import com.example.esm.results.models.ResultMasterModel
import java.text.DecimalFormat

class ResultsAdapter( val list: ArrayList<ResultMasterModel>) :RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding :RecyclerviewLayoutResultLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_result_layout,
            parent,
            false
        )
        return ResultViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        var totalMarks= 0
        var obtMarks= 0.0f
        holder.binding.evaluationName.text= list[position].EvaluationName
        for (i in 0 until list[position].EvaluationDetails.size){
            totalMarks= totalMarks.plus(list[position].EvaluationDetails[i].TotalMarks)
            obtMarks= obtMarks.plus(list[position].EvaluationDetails[i].ObtainedMarks)
        }
        val percentage= (obtMarks*100)/totalMarks
        holder.binding.tvTotalMarksSum.text= totalMarks.toString()
        //holder.binding.tvObtainedMarksSum.text=  DecimalFormat("##.##".format(obtMarks)).toString()
        holder.binding.tvObtainedMarksSum.text= obtMarks.toString()
        holder.binding.tvPercentage.text= "$percentage %"
        holder.binding.childResultRecyclerView.adapter= ResultChildAdapter(list[position].EvaluationDetails)

    }

    class ResultViewHolder(val binding: RecyclerviewLayoutResultLayoutBinding):
        RecyclerView.ViewHolder(binding.root)

}