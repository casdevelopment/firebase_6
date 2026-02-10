package com.example.esm.complaint.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.esm.R
import com.example.esm.complaint.ComplaintFragmentDirections
import com.example.esm.complaint.models.ComplaintModel
import com.example.esm.databinding.DialogComplaintDetailsBinding
import com.example.esm.databinding.RecyclerviewLayoutComplaintsHistoryBinding
import com.example.esm.utils.AppConstants
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class ComplaintAdapter( val complaintList: ArrayList<ComplaintModel>,
                        var mCtx: Context)
    : RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding:RecyclerviewLayoutComplaintsHistoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_layout_complaints_history,
            parent,
            false
        )
        return ComplaintViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return complaintList.size
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        holder.binding.attendanceDate.text = "#"+ " " + complaintList[position].ComplaintId.toString()
        holder.binding.complaintTitle.text = complaintList[position].ComplaintTitle
        holder.binding.complaintStatus.text = complaintList[position].ComplaintText
        holder.binding.viewComplaint.setOnClickListener {
            val dialogBinding = DialogComplaintDetailsBinding.inflate(LayoutInflater.from(mCtx))
            val dialogBuilder = AlertDialog.Builder(mCtx)
                .setView(dialogBinding.root)
                .create()
            dialogBinding.titleComplaint.setText( complaintList[position].ComplaintTitle)
            dialogBinding.complaintText.setText(complaintList[position].ComplaintText)
            dialogBinding.submitComplaint.setOnClickListener {
                dialogBuilder.dismiss()
            }
            dialogBuilder.show()
        }

        holder.binding.viewResponse.setOnClickListener(View.OnClickListener {
            val complaintId = complaintList[position].ComplaintId ?: return@OnClickListener
            val userId = AppConstants.USER_IDENTITY
         //   val userId = complaintList[position].UserIdentity ?: return@OnClickListener

            if (mCtx is FragmentActivity) {
                val navController = (mCtx as FragmentActivity).findNavController(R.id.nav_host_fragment)
                navController.navigate(ComplaintFragmentDirections.actionComplaintFragmentToResponseChatFragment(complaintId,userId))
            }
        })

    }

    class ComplaintViewHolder(val binding: RecyclerviewLayoutComplaintsHistoryBinding)
        : RecyclerView.ViewHolder(binding.root)



}