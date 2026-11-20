package com.example.esm.paymenthistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutPaymentHistoryBinding
import com.example.esm.paymenthistory.models.PaymentHistoryModel

class PaymentHistoryAdapter(
    val paymentList: ArrayList<PaymentHistoryModel>,
    private val listener: OnPaymentClickListener,
    val requireContext: Context
)
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


        if (requireContext.packageName.equals("com.fssa.esm")) {
           // holder.binding.itemLayout.weightSum = 2.0f
            // 2. Hide the Fee Receipt column
            holder.binding.feeDepositReceipt.visibility = View.VISIBLE
            holder.binding.feeDepositMonth.visibility = View.GONE
            // Optional: Ensure the layout refreshes
            holder.binding.itemLayout.requestLayout()

        }else if (requireContext.packageName.equals("com.busybees.esm")||requireContext.packageName.equals("com.keystoneacademy.esm")) {
           // holder.binding.itemLayout.weightSum = 2.0f
            // 2. Hide the Fee Receipt column
            holder.binding.feeDepositMonth.visibility = View.VISIBLE
            holder.binding.feeDepositReceipt.visibility = View.GONE
            holder.binding.feeDepositMonth.text = paymentList[position].MonthNames
            // Optional: Ensure the layout refreshes
            holder.binding.itemLayout.requestLayout()

        }else{
            holder.binding.itemLayout.weightSum = 2.0f
            // 2. Hide 3rd coloum column
            holder.binding.cell3.visibility = View.GONE
        }

        holder.binding.feeDepositReceipt.setOnClickListener {
            listener.onDownloadReceiptClick(itemData.FeeDepositId.toString(),itemData.UC_SchoolId)
        }



    }
    class PaymentHistoryViewHolder(val binding: RecyclerviewLayoutPaymentHistoryBinding)
        :RecyclerView.ViewHolder(binding.root)

    interface OnPaymentClickListener {
        fun onDownloadReceiptClick(feeDepositId: String, ucSchoolid: String?)
    }

}