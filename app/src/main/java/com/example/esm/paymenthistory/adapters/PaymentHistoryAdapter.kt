package com.example.esm.paymenthistory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutPaymentHistoryBinding
import com.example.esm.paymenthistory.models.PaymentHistoryModel

class PaymentHistoryAdapter( val paymentList: ArrayList<PaymentHistoryModel>)
    :RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        val binding:RecyclerviewLayoutPaymentHistoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_payment_history,
            parent,
            false)
        return PaymentHistoryViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        val itemData : PaymentHistoryModel = paymentList[position]
        holder.binding.paymenthistorydate.text =itemData.Date
        holder.binding.paymenthistoryamount.text = paymentList[position].Amount
       // holder.binding.view.visibility = View.VISIBLE

    }
    class PaymentHistoryViewHolder(val binding: RecyclerviewLayoutPaymentHistoryBinding)
        :RecyclerView.ViewHolder(binding.root)

}