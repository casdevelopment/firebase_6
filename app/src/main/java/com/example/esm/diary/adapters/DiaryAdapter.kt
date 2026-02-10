package com.example.esm.diary.adapters

import android.R.id.input
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutDiariesLayoutBinding
import com.example.esm.diary.models.DiaryModel


class DiaryAdapter( val stdDiaryList: ArrayList<DiaryModel> , val mListener: onDownloadClickListener) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding: RecyclerviewLayoutDiariesLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_diaries_layout,
            parent,
            false)
        return DiaryViewHolder(binding)


    }

    override fun getItemCount(): Int {
        return stdDiaryList.size
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.binding.subjectFirstLetter.text = stdDiaryList[position].SubjectNameFirstLetter
        holder.binding.subjectName.text = stdDiaryList[position].SubjectName
        holder.binding.createdBy.text = stdDiaryList[position].CreatedBy
        holder.binding.diaryNotes.text = stdDiaryList[position].DiaryNotes

        //////split name of subjec name
        var subject_name = stdDiaryList[position].SubjectName
        val split_string = subject_name?.toCharArray()
        var first_char = split_string?.get(0)
        holder.binding.subjectFirstLetter.text = first_char.toString()

            Log.d("string","split string is : $first_char")

          //  val char= str?.chars()

//        if (str != null) {
//            Log.d("string","split string is : $char")
//           // Log.d("string","split string is : ${str.charAt[0]}")
//        }
        Log.d("string","split string is : $subject_name")
        //Log.d("string","split string is : $result[1]")
            //////////////////////
//           var arr = input_string?.split("\\s+")
//                   Log.d("string","split string is : $arr[0]")



        //show download if user_file_name is not empty
        if (!stdDiaryList[position].UserFileName.isNullOrEmpty()){
            holder.binding.download.visibility = View.VISIBLE
        }
        else{
            holder.binding.download.visibility = View.GONE

        }
        holder.binding.download.setOnClickListener {
            mListener.onItemClick(position,stdDiaryList[position])

        }

    }

    private fun splitStringIntoCharacters(inputString: String): List<Char> {
            return inputString.toList()
    }

    class DiaryViewHolder( val binding: RecyclerviewLayoutDiariesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
    interface onDownloadClickListener{
        fun onItemClick(position: Int, diaryModel: DiaryModel)

    }

}