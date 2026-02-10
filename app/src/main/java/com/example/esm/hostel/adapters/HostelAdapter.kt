package com.example.esm.hostel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.ItemHostelBinding
import com.example.esm.hostel.models.HostelModel

class HostelAdapter(val hostelList: ArrayList<HostelModel>) :
    RecyclerView.Adapter<HostelAdapter.HostelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostelViewHolder {
        var binding: ItemHostelBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_hostel,
            parent,
            false
        )
        return HostelViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return hostelList.size
    }

    override fun onBindViewHolder(holder: HostelViewHolder, position: Int) {
        holder.binding.studentName.text = hostelList[position].StudentName
        holder.binding.fatherName.text = hostelList[position].FatherName
        holder.binding.description.text = hostelList[position].Description
        holder.binding.locationName.text = hostelList[position].LocationName
        holder.binding.floorName.text = hostelList[position].FloorName
        holder.binding.roomName.text = hostelList[position].RoomName

    }

    class HostelViewHolder(val binding: ItemHostelBinding) : RecyclerView.ViewHolder(binding.root)

}