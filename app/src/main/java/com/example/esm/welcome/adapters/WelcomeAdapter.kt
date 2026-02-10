package com.example.esm.welcome.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.databinding.RecyclerviewLayoutWelcomeScreenBinding
import com.example.esm.utils.AppUtils
import com.example.esm.welcome.models.StudentDataModel

class WelcomeAdapter( val studentList: ArrayList<StudentDataModel>, var mListener: onItemClickListener ,var context: Context)
    : RecyclerView.Adapter<WelcomeAdapter.WelcomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        val binding: RecyclerviewLayoutWelcomeScreenBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_welcome_screen,
            parent,
            false
        )
        return WelcomeViewHolder(binding)


    }

    override fun getItemCount(): Int {
        Log.d("studentList","$studentList")
         return studentList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        Log.d("studentList","$studentList")
        studentList[position].StudentPictureString?.let {
            AppUtils.loadImageImage(it,holder.binding.studenImage,context,R.drawable.avtr,R.drawable.avtr)
        }

        holder.binding.studentName.text = studentList[position].StudentName
        holder.binding.studentFatherName.text = studentList[position].FatherName
        holder.binding.studentID.text= studentList[position].StudentId.toString()
        holder.binding.studentRegistration.text = studentList[position].AdmissionNumber
        holder.binding.studentclass.text = studentList[position].ClassName
        holder.binding.sectionName.text = studentList[position].SectionName
        holder.binding.item.setOnClickListener {
           mListener.onItemClick(studentList[position],position)

        }
        if (context.packageName.equals("com.bag.esm")) {
            holder.binding.item.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.gradient_welcome_screen_bag
                )
            )
        }else  if (context.packageName.equals("com.rubricseducationsystem.esm")) {
            holder.binding.item.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.gradient_welcome_screen_rubrics
                )
            )
        }

    }

    class WelcomeViewHolder(val binding:RecyclerviewLayoutWelcomeScreenBinding)
        : RecyclerView.ViewHolder(binding.root)

    interface onItemClickListener{
        fun onItemClick(model: StudentDataModel, position: Int) {

        }

    }


}