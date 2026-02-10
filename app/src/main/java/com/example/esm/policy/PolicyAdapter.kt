package com.example.esm.policy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.dashboardfragment.adapter.DashboardAdapter.onItemClickListener
import com.example.esm.databinding.RecyclerviewLayoutPaymentHistoryBinding
import com.example.esm.databinding.RecyclerviewLayoutPolicyBinding


class PolicyAdapter(val paymentList: ArrayList<PolicyListData>,val mListener:onItemClickListener,)
    :RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyViewHolder {
        val binding: RecyclerviewLayoutPolicyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_policy,
            parent,
            false)
        return PolicyViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    override fun onBindViewHolder(holder: PolicyViewHolder, position: Int) {
        val itemData : PolicyListData = paymentList[position]
        holder.binding.policyText.text =itemData.PolicyText
        holder.binding.cardView.setOnClickListener {
            mListener.onItemClick(position)
        }


    }
    class PolicyViewHolder(val binding: RecyclerviewLayoutPolicyBinding)
        :RecyclerView.ViewHolder(binding.root)

    interface onItemClickListener{
        fun onItemClick(position: Int)

    }

}