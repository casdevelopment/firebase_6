package com.example.esm.complaint.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.complaint.models.ComplaintModel
import com.example.esm.complaint.models.ComplaintResponseModel
import com.example.esm.databinding.ItemChatBinding

class ComplaintAdapterResponse (private val items: List<ComplaintResponseModel>, var mCtx: Context)
    : RecyclerView.Adapter<ComplaintAdapterResponse.ViewHolder>() {

    inner class ViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChatBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_chat,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData : ComplaintResponseModel = items[position]
        if(itemData.ComplaintResponseType.equals("Complaint Resolver",true)){
            holder.binding.leftchat.text = itemData.Response
            holder.binding.leftchat.visibility = View.VISIBLE
            holder.binding.rightchat.visibility = View.GONE
        } else {
            holder.binding.rightchat.text = itemData.Response
            holder.binding.leftchat.visibility = View.GONE
            holder.binding.rightchat.visibility = View.VISIBLE
        }
    }
}