package com.example.esm.notice.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.esm.R
import com.example.esm.databinding.FragmentNoticeViewBinding
import com.example.esm.notice.adapter.RecyclerViewFileAdapter
import com.example.esm.notice.models.NoticeFilesModel


class NoticeViewFragment : Fragment() , RecyclerViewFileAdapter.onItemClickListener{
    lateinit var binding: FragmentNoticeViewBinding
    private val args: NoticeViewFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_notice_view,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        binding.notificationText.setText(args.noticeModel.NotificationText)

    }

    private fun setRecyclerView() {
       if(args.noticeModel.NoticeFiles != null && args.noticeModel.NoticeFiles.isNotEmpty()) {
           binding.filesRecyclerView.adapter = RecyclerViewFileAdapter(args.noticeModel.NoticeFiles,this)
           binding.noItems.visibility=GONE
       } else {
           binding.noItems.visibility= VISIBLE

       }
      /*  if(args.noticeModel.NoticeFiles.size>0){
            binding.noItems.visibility=GONE
        }
        binding.filesRecyclerView.adapter = RecyclerViewFileAdapter(args.noticeModel.NoticeFiles,this)*/

    }

    override fun onItemClick(model: NoticeFilesModel, position: Int) {
        //val url = "WWW.GOOGLE.COM"
       // val url = "https://chat.openai.com/c/3ea13d6f-457a-4be8-bd75-1c5c8e49860d"
        try {
            Log.d("url","url is :${model.FileURL}")
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(model.FileURL)))
        }catch (exc:Exception){
            Log.d("url","url is :${exc}")
        }
    }
}