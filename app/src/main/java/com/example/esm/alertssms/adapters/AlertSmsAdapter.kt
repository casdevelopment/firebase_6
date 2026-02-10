package com.example.esm.alertssms.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.alertssms.AlertSMSFragment
import com.example.esm.alertssms.models.AlertSmsModel
import com.example.esm.databinding.RecyclerviewLayoutSmsLayoutBinding

class AlertSmsAdapter(val smsList: ArrayList<AlertSmsModel>, val mListener: onSmsItemClickListener) :RecyclerView.Adapter<AlertSmsAdapter.smsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): smsViewHolder {
        val binding:RecyclerviewLayoutSmsLayoutBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_sms_layout,
            parent,
            false)
        return smsViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return smsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: smsViewHolder, position: Int) {

        holder.binding.tvSmsMask.text= smsList[position].SmsMask
       // holder.binding.msg.text= smsList[position].Message
        holder.binding.smsdate.text= smsList[position].Date
        //show 4 words of msg
        var message= smsList[position].Message
        //split the string into sub_string
        // this is a cat: input
        // "this","is","a","cat"
        val array2 = message.split("\\s+".toRegex())
        Log.v("arr","array: ${array2}")
        if (array2.size > 4){
            //Splits words & assign to the arr[]  ex : arr[0] -> Copying ,arr[1] -> first
            val N = 4 // NUMBER OF WORDS THAT YOU NEED
            var nWords = ""
            // concatenating number of words that you required
            for (i in 0 until N) {
                nWords = nWords + " " + array2[i]
            }
            holder.binding.msg.text= nWords + "..."

        }
        holder.binding.smsItem.setOnClickListener {
            mListener.onItemClick(position,smsList[position])

        }
    }

    class smsViewHolder( val binding: RecyclerviewLayoutSmsLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)
    interface onSmsItemClickListener{
        fun onItemClick(position: Int, alertSmsModel: AlertSmsModel)

    }





}