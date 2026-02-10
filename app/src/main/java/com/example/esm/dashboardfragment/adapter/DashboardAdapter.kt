package com.example.esm.dashboardfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.dashboardfragment.models.DashboardItemModel
import com.example.esm.databinding.ItemDashboardBinding

class DashboardAdapter(val list: ArrayList<DashboardItemModel>, val context: Context,val mListener:onItemClickListener)
    :RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {
    private var dashboardDetailList: ArrayList<DashboardItemModel> = ArrayList()
    init {
        dashboardDetailList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        var binding:ItemDashboardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_dashboard,
            parent,
            false
        )
        return DashboardViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return dashboardDetailList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.binding.textView.text=dashboardDetailList[position].name
        holder.binding.itemImage.setImageResource(list[position].image)
        holder.binding.llMain.setOnClickListener {
            mListener.onItemClick(position)
        }
    }
    class DashboardViewHolder( val binding:ItemDashboardBinding):RecyclerView.ViewHolder(binding.root)
    interface onItemClickListener{
        fun onItemClick(position: Int)

    }

}