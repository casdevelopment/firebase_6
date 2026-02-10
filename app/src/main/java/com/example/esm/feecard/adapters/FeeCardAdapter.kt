package com.example.esm.feecard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutFeecardBinding
import com.example.esm.feecard.models.FeeCardDetailsModel

class FeeCardAdapter( val feeCardlist: ArrayList<FeeCardDetailsModel>) : RecyclerView.Adapter<FeeCardAdapter.FeecardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeecardViewHolder {
        val binding: RecyclerviewLayoutFeecardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_feecard,
            parent,
            false)
        return FeecardViewHolder(binding)

    }
    override fun getItemCount(): Int {
        return feeCardlist.size
    }


    override fun onBindViewHolder(holder: FeecardViewHolder, position: Int) {
        holder.binding.TFeeHeadName.text = feeCardlist[position].TFeeHeadName
        holder.binding.TPrevBalance.text = feeCardlist[position].TPrevBalance.toString()
        holder.binding.TFeeHeadActualAmount.text = feeCardlist[position].TFeeHeadActualAmount.toString()
        holder.binding.TRemaining.text = feeCardlist[position].TRemaining.toString()
    }



class FeecardViewHolder( val binding: RecyclerviewLayoutFeecardBinding)
    :RecyclerView.ViewHolder(binding.root)

}