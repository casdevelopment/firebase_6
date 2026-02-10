package com.example.esm.notice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentNoticeBinding
import com.example.esm.databinding.FragmentNoticePracticeBinding
import com.example.esm.notice.adapter.DashboardAdapter
import com.example.esm.notice.models.DashboardModel
import com.example.esm.utils.AppConstants


class NoticeFragment : Fragment() , DashboardAdapter.onItemClickListener {
   // lateinit var binding: FragmentNoticeBinding
   lateinit var binding: FragmentNoticePracticeBinding
    var arrayList:ArrayList<DashboardModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_notice_practice,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.dashboardFragment, false)
                }
            })
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()

        Log.v("remoteMessage", "POSITION 1: ${AppConstants.POSITION}")


        AppConstants.POSITION?.let {
            if (it.isNotEmpty() && it != "null") {
                AppConstants.POSITION = "null"
                    if (it.toInt() != 0) {
                        navigateToFragment(it.toInt() - 1)
                    } else {
                        navigateToFragment(it.toInt())

                    }

        }
        }
        initView()
    }

    private fun initView() {
        arrayList.clear()
       // arrayList.add(0, DashboardModel(R.drawable.payment_history_48,"Datesheet"))
        arrayList.add(0, DashboardModel(R.drawable.datesheet,"Datesheet"))
        arrayList.add(1, DashboardModel(R.drawable.syllabus,"Syllabus"))
        arrayList.add(2, DashboardModel(R.drawable.holiday,"Holiday"))
        arrayList.add(3, DashboardModel(R.drawable.general,"General"))
        arrayList.add(4, DashboardModel(R.drawable.misconduct,"Misconduct"))
        arrayList.add(5, DashboardModel(R.drawable.sports,"Sports"))
        arrayList.add(6, DashboardModel(R.drawable.ptm,"Parent Teacher Meeting"))
        binding.noticeRecyclerview.adapter = DashboardAdapter(arrayList,this)
    }

    override fun onItemClick(model: DashboardModel, position: Int) {
        if (position == 0){
            val action = NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(1)
           findNavController().navigate(action)

        }else if (position == 1){
            val action = NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(2)
            findNavController().navigate(action)

        }else if (position == 2){
            val action = NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(3)
            findNavController().navigate(action)

        }else if (position == 3){
            val action = NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(4)
            findNavController().navigate(action)

        }else if (position == 4){
            val action = NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(5)
            findNavController().navigate(action)

        }else if (position == 5){
            val action = NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(6)
            findNavController().navigate(action)

        }else if (position == 6){
            val action = NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(7)
            findNavController().navigate(action)

        }

    }

    private fun navigateToFragment(position: Int) {
        val action = when(position) {
            0 -> NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(1)
            1 -> NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(2)
            2 -> NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(3)
            3 -> NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(4)
            4 -> NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(5)
            5 -> NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(6)
            6 -> NoticeFragmentDirections.actionNoticeFragmentToCommonApiFragment(7)
            else -> null
        }

        action?.let { findNavController().navigate(it) }
    }


}