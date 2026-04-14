package com.example.esm.notice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewNoticeLayoutBinding
import com.example.esm.notice.models.NoticeModel
import java.text.SimpleDateFormat
import java.util.Locale

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

        val item = noticeList[position]
        holder.binding.notificationTitle.text = item.NotificationTitle
        holder.binding.notificationText.text = item.NotificationText

        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            Locale.getDefault()
        )

        val dateObj = try {
            item.CreatedDate?.let { inputFormat.parse(it) }
        } catch (e: Exception) {
            null
        }

        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        holder.binding.notificationDate.text =
            dateObj?.let { dateFormat.format(it) } ?: ""

        holder.binding.notificationTime.text =
            dateObj?.let { timeFormat.format(it) } ?: ""

        holder.binding.viewNotification.setOnClickListener {
            mListener.onItemClick(noticeList[position],position)

        }
    }

    class RecyclerViewHolder( val binding: RecyclerviewNoticeLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    interface onNoticeClickListener{
        fun onItemClick(model: NoticeModel, position: Int)

    }

}