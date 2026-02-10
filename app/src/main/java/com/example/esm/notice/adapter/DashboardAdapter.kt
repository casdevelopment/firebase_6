package com.example.esm.notice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.NoticeListItemBinding
import com.example.esm.notice.models.DashboardModel

class DashboardAdapter( val list: ArrayList<DashboardModel> , val mListener:onItemClickListener) : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        var binding: NoticeListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.notice_list_item,
            parent,
            false
        )
        return DashboardViewHolder(binding)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.binding.textView.text = list[position].name
        holder.binding.imageView.setImageResource(list[position].image)
        holder.binding.llmain.setOnClickListener {
            mListener.onItemClick(list[position],position)

        }



    }
    class DashboardViewHolder( val binding: NoticeListItemBinding) : RecyclerView.ViewHolder(binding.root)
    interface onItemClickListener{
        fun onItemClick(dashboardModel: DashboardModel, position: Int)
    }

}