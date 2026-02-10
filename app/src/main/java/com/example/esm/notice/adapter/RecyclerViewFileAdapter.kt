package com.example.esm.notice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutNoticefilesLayoutBinding
import com.example.esm.notice.models.NoticeFilesModel

class RecyclerViewFileAdapter( val noticeFilesList: ArrayList<NoticeFilesModel>, val mListener: onItemClickListener) : RecyclerView.Adapter<RecyclerViewFileAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding: RecyclerviewLayoutNoticefilesLayoutBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.recyclerview_layout_noticefiles_layout,
            parent
        ,false
        )
        return FileViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return noticeFilesList.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.binding.openFileLink.text = noticeFilesList[position].FileName
        holder.binding.openFileLink.setOnClickListener {
            mListener.onItemClick(noticeFilesList[position],position)


        }

    }

    class FileViewHolder( val binding: RecyclerviewLayoutNoticefilesLayoutBinding) : RecyclerView.ViewHolder(binding.root)
    interface onItemClickListener{
        fun onItemClick(model: NoticeFilesModel, position: Int)

    }



}