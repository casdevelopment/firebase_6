package com.example.esm.eventcalendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutEventsLayoutBinding
import com.example.esm.eventcalendar.models.EventsModel

class EventCalendarAdapter( val list: ArrayList<EventsModel>):RecyclerView.Adapter<EventCalendarAdapter.EventViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding: RecyclerviewLayoutEventsLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_events_layout,
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.binding.tvDescription.text = list[position].Description
    }
    class EventViewHolder( val binding: RecyclerviewLayoutEventsLayoutBinding) :RecyclerView.ViewHolder(binding.root)



}