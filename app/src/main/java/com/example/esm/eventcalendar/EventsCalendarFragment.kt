package com.example.esm.eventcalendar


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.cyberisol.calendarview.EventDay
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentEventsCalendarBinding
import com.example.esm.eventcalendar.adapters.EventCalendarAdapter
import com.example.esm.eventcalendar.models.EventsModel
import com.example.esm.eventcalendar.viewmodels.EventsViewModel
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import com.varunbarad.highlightable_calendar_view.DayDecorator
import com.varunbarad.highlightable_calendar_view.OnDateSelectListener
import com.varunbarad.highlightable_calendar_view.OnMonthChangeListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.Locale


class EventsCalendarFragment : Fragment() {
    lateinit var binding: FragmentEventsCalendarBinding
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: EventsViewModel by viewModel()
    private val eventList: ArrayList<EventsModel> = ArrayList()
    private val eventList2: ArrayList<EventsModel> = ArrayList()


    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    // var calendarDays: ArrayList<CalendarDay> = ArrayList()
    var eventdays: ArrayList<DayDecorator> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_events_calendar,
            container,
            false
        )
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
        setCalendardata()
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
            binding.eventsRecyclerView.visibility = View.GONE
            binding.noItem.visibility = View.VISIBLE
        } else {
            setDataForApi()
        }


        initView()
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()


    }


    private fun initView() {

        binding.calendarView2.onDateSelectListener = OnDateSelectListener { selectedDate ->
            day = selectedDate.get(Calendar.DAY_OF_MONTH)
            month = selectedDate.get(Calendar.MONTH) + 1
            year = selectedDate.get(Calendar.YEAR)

            //  Toast.makeText(requireContext(), "$day $month , $year", Toast.LENGTH_SHORT).show()
            if (eventList.size > 0) {
                addToEvent(eventList)
            }
        }


        binding.calendarView2.onMonthChangeListener = OnMonthChangeListener { oldMonth, newMonth ->
            val oldMonthDisplay = oldMonth.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.getDefault()
            )
            val newMonthDisplay = newMonth.getDisplayName(
                Calendar.MONTH,
                Calendar.LONG,
                Locale.getDefault()
            )

            day = newMonth.get(Calendar.MONTH)
            month = newMonth.get(Calendar.MONTH) + 1
            year = newMonth.get(Calendar.YEAR)
            if (!sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)) {
                setDataForApi()
            }
        }

    }


    private fun addToEvent(eventsModelList: ArrayList<EventsModel>) {
        if (eventList2.size > 0) {
            eventList2.clear()
        }
        if (eventdays.size > 0) {
            eventdays.clear()
        }

        for (i in 0 until eventsModelList.size) {

            if (eventsModelList[i].Description.isNotEmpty()) {
                val arr: Array<String> = eventsModelList[i].DayString.split(" ").toTypedArray()
                val a = arr[0]
                val b = a.toInt()
                eventdays.add(
                    DayDecorator(
                        Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, b) },
                        ContextCompat.getColor(requireContext(), R.color.greenStatus),
                        ContextCompat.getColor(requireContext(), R.color.colorWhite)
                    )
                )

                val calendar = Calendar.getInstance()
                if (b < day) {
                    calendar.add(Calendar.DAY_OF_MONTH, b - day)
                } else if (b > day) {
                    calendar.add(Calendar.DAY_OF_MONTH, b - day)
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, 0)
                    eventList2.add(eventsModelList[i])
                }
            }
        }
        binding.calendarView2.dayDecorators = eventdays
        Log.v("eventdays", "eventdays ")
        setRecyclerView(eventList2)
    }


    private fun setDataForApi() {
        Log.v("calendar", "----year :$year :month:$month :day is:$day")
        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        identityModel.Month = month
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        identityModel.Year = year
        callStudentCalendarApi(identityModel)

    }

    private fun setCalendardata() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)

    }

    private fun callStudentCalendarApi(model: IdentityModel) {
        viewModel.calendarList(model).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    AppUtils.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data != null) {
                        if (apiResponse.data.isSuccessful) {

                            val response = apiResponse.data.body()
                            if (response != null) {
                                Log.d("response", "$response")
                                Log.d("response", "Size " + response.CalendarList.size)
                                if (response.CalendarList.isNotEmpty()) {
                                    response.CalendarList.let {
                                        if (eventList.size > 0) {
                                            eventList.clear()
                                        }
                                        eventList.addAll(it)
                                    }

                                } else {
                                    if (eventList.size > 0) {
                                        eventList.clear()
                                    }
                                }
                                addToEvent(eventList)
                                Log.d("response", "$eventList")
                                /*  setRecyclerView(eventList)*/


                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    " api response is null",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), " not successful", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }


                }

                Status.ERROR -> {
                    AppUtils.stopLoader()
                    Toast.makeText(requireContext(), apiResponse.message, Toast.LENGTH_SHORT).show()
                    Log.d("response", "error:${apiResponse.message}")

                }
            }

        }

    }

    private fun setRecyclerView(list: ArrayList<EventsModel>) {
        if (list.isNotEmpty()) {
            binding.eventsRecyclerView.adapter = EventCalendarAdapter(list)
            binding.eventsRecyclerView.visibility = View.VISIBLE
            binding.noItem.visibility = View.GONE

        } else {
            binding.eventsRecyclerView.visibility = View.GONE
            binding.noItem.visibility = View.VISIBLE

        }
    }

}
