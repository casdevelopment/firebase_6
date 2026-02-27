package com.example.esm.diary.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutDiariesLayoutBinding
import com.example.esm.diary.models.DiaryModel


class DiaryAdapter(
    val stdDiaryList: ArrayList<DiaryModel>,
    val mListener: onDownloadClickListener,
    val requireContext: Context
) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

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

        // --- DAY HIGHLIGHTING LOGIC ---
        highlightDay(stdDiaryList[position].DateFromString,  holder.binding)
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
        if (requireContext.packageName.equals("com.rha.esm")) {
            holder.binding.s.visibility=GONE
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

    private fun highlightDay(dateString: String?, binding: RecyclerviewLayoutDiariesLayoutBinding) {
        if (dateString.isNullOrEmpty()) return

        try {
            Log.d("highlightDay","dateString: "+dateString)
            // 'd' for day, 'MMM' for short month name, 'yyyy' for year
            val sdf = java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale.getDefault())
            val date = sdf.parse(dateString)
            val calendar = java.util.Calendar.getInstance()
            calendar.time = date ?: return

            // Reset all colors to default (gray/inactive) first
            val defaultColor = androidx.core.content.ContextCompat.getColor(
                binding.root.context,
                R.color.login_attributes_color
            )
            val highlightColor = androidx.core.content.ContextCompat.getColor(
                binding.root.context,
                R.color.colorPrimary
            ) // Or any bright color

            val days = listOf(binding.m, binding.t, binding.w, binding.th, binding.f, binding.s)
            days.forEach {
                it.setTextColor(defaultColor); it.setTypeface(
                null,
                android.graphics.Typeface.NORMAL
            )
            }

            // Highlight the specific day
            // Calendar.SUNDAY = 1, MONDAY = 2...
            when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
                java.util.Calendar.MONDAY -> {
                    binding.m.setTextColor(highlightColor); binding.m.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )
                }

                java.util.Calendar.TUESDAY -> {
                    binding.t.setTextColor(highlightColor); binding.t.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )
                }

                java.util.Calendar.WEDNESDAY -> {
                    binding.w.setTextColor(highlightColor); binding.w.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )
                }

                java.util.Calendar.THURSDAY -> {
                    binding.th.setTextColor(highlightColor); binding.th.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )
                }

                java.util.Calendar.FRIDAY -> {
                    binding.f.setTextColor(highlightColor); binding.f.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )
                }

                java.util.Calendar.SATURDAY -> {
                    binding.s.setTextColor(highlightColor); binding.s.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("DateError", "Error parsing date: $dateString", e)
        }
    }

}