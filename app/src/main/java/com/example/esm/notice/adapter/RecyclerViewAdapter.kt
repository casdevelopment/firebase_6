package com.example.esm.notice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewNoticeLayoutBinding
import com.example.esm.notice.models.NoticeModel

class RecyclerViewAdapter( val noticeList: ArrayList<NoticeModel>, var mListener: onNoticeClickListener) :RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding :RecyclerviewNoticeLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_notice_layout,
            parent
            ,false
        )
        return RecyclerViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.binding.notificationTitle.text = noticeList[position].NotificationTitle
        holder.binding.notificationText.text = noticeList[position].NotificationText
        holder.binding.viewNotification.setOnClickListener {
            mListener.onItemClick(noticeList[position],position)

        }
    }

    class RecyclerViewHolder( val binding: RecyclerviewNoticeLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    interface onNoticeClickListener{
        fun onItemClick(model: NoticeModel, position: Int)

    }

}