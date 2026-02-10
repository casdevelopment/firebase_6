package com.example.esm.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentReportBinding
import com.example.esm.network.Status
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.SharedPrefsHelper
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ReportFragment : Fragment() {
    lateinit var fields: HashMap<String, String>
    var gson = Gson()
    private lateinit var binding: FragmentReportBinding
    val args : ReportFragmentArgs by navArgs()
    private var fromDateText = ""
    private var toDateText = ""
    private var examDateText = ""
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: ReportViewModel by viewModel()
    private  var evaluationTypeID: String="0"
    private  var middleEvaluationTypeID ="0"
    private  var oaLevelEvaluationTypeID ="0"
    private  var middleEvaluationID ="0"
    private  var oaLevelEvaluationID ="0"
    private  var eovEvaluationTypeID: String="0"
    private  var myeEvaluationTypeID: String="0"
    private  var examEvaluationID: String="0"
    private  var termEvaluationID: String="0"
    private lateinit var evaluationTypeList: ArrayList<EvaluationTypeListData>
    private lateinit var middleOALevelEvaluationTypeList: ArrayList<EvaluationTypeListData>

    private var adapterMiddleEvaluationType: ArrayAdapter<EvaluationTypeListData>? = null
    private var adapterOALevelEvaluationType: ArrayAdapter<EvaluationTypeListData>? = null
    private var adapterEvaluationType: ArrayAdapter<EvaluationTypeListData>? = null
    private var adapterEOVEvaluationType: ArrayAdapter<EvaluationTypeListData>? = null
    private var adapterMYEEvaluationType: ArrayAdapter<EvaluationTypeListData>? = null

    private lateinit var evaluationList: ArrayList<EvaluationListData>
    private var adapterEvaluationExam: ArrayAdapter<EvaluationListData>? = null
    private var adapterOALevelEvaluation: ArrayAdapter<EvaluationListData>? = null
    private var adapterEvaluationTerm: ArrayAdapter<EvaluationListData>? = null
    private var adapterMiddleEvaluation: ArrayAdapter<EvaluationListData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReportBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedToday = sdf.format(today)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.dashboardFragment, false)
                }
            })
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()

        with(binding){

            if(args.eoy){
                evaluationTypeSpinner.visibility=GONE
                evaluationTypeSpinnerText.visibility=GONE
            }else if (args.mye){
                myeEvaluationTypeSpinner.visibility=GONE
                eoyEvaluationTypeSpinner.visibility=GONE
                myeEvaluationTypeSpinnerText.visibility=GONE
                eoyEvaluationTypeSpinnerText.visibility=GONE
            }else if (args.middle){

                middleEvaluationTypeSpinner.visibility= VISIBLE
                middleEvaluationTypeSpinnerText.visibility= VISIBLE
                middleEvaluationSpinner.visibility= VISIBLE
                middleEvaluationSpinnerText.visibility= VISIBLE


                evaluationTypeSpinner.visibility=GONE
                evaluationTypeSpinner.visibility=GONE
                evaluationTypeSpinnerText.visibility=GONE
                myeEvaluationTypeSpinner.visibility=GONE
                eoyEvaluationTypeSpinner.visibility=GONE
                myeEvaluationTypeSpinnerText.visibility=GONE
                eoyEvaluationTypeSpinnerText.visibility=GONE
                selectExamSpinnerText.visibility=GONE
                selectExamSpinner.visibility=GONE
                selectTermSpinnerText.visibility=GONE
                selectTermSpinner.visibility=GONE
                fromDate.visibility=GONE
                toDate.visibility=GONE
                toDate.visibility=GONE
                examDate.visibility=GONE

                callMiddleEvaluationTypeAPi()

            }else if (args.olevel){

                olevelEvaluationTypeSpinnerText.visibility= VISIBLE
                olevelEvaluationTypeSpinner.visibility= VISIBLE

                olevelEvaluationSpinner.visibility= VISIBLE
                olevelEvaluationSpinnerText.visibility= VISIBLE

                evaluationTypeSpinner.visibility=GONE
                evaluationTypeSpinnerText.visibility=GONE
                myeEvaluationTypeSpinner.visibility=GONE
                eoyEvaluationTypeSpinner.visibility=GONE
                myeEvaluationTypeSpinnerText.visibility=GONE
                eoyEvaluationTypeSpinnerText.visibility=GONE
                selectExamSpinnerText.visibility=GONE
                selectExamSpinner.visibility=GONE
                selectTermSpinnerText.visibility=GONE
                selectTermSpinner.visibility=GONE
                fromDate.visibility=GONE
                toDate.visibility=GONE
                toDate.visibility=GONE
                examDate.visibility=GONE


                callOALevelEvaluationTypeAPi()

            }

            fromDate.setOnClickListener {
                showDatePicker { selectedDate ->
                    fromDateText = selectedDate
                    binding.fromDate.setText(selectedDate)
                }
            }

            toDate.setOnClickListener {
                showDatePicker { selectedDate ->
                    toDateText = selectedDate
                    binding.toDate.setText(selectedDate)
                }
            }

            examDate.setOnClickListener {
                showDatePicker { selectedDate ->
                    examDateText = selectedDate
                    binding.examDate.setText(selectedDate)
                }
            }


            evaluationTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    evaluationTypeID= evaluationTypeList[position].EvaluationTypeId.toString()
                    callEvaluationAPi(evaluationTypeID)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            myeEvaluationTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    myeEvaluationTypeID= evaluationTypeList[position].EvaluationTypeId.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            eoyEvaluationTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    eovEvaluationTypeID= evaluationTypeList[position].EvaluationTypeId.toString()
                    callEvaluationAPi(eovEvaluationTypeID)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            selectExamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    examEvaluationID= evaluationTypeList[position].EvaluationTypeId.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            selectTermSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    termEvaluationID= evaluationTypeList[position].EvaluationTypeId.toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            middleEvaluationTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.v("callMiddleEvaluationTypeAPi","setMiddleSpinnerListener evaluationTypeList size "+middleOALevelEvaluationTypeList.size)

                    middleEvaluationTypeID= middleOALevelEvaluationTypeList[position].EvaluationTypeId.toString()
                    callMiddleEvaluationAPi(middleEvaluationTypeID)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            middleEvaluationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.v("callMiddleEvaluationTypeAPi","setMiddleSpinnerListener evaluationTypeList size "+middleOALevelEvaluationTypeList.size)

                    middleEvaluationID= evaluationList[position].EvaluationId.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    middleEvaluationID="0"
                }
            }

            olevelEvaluationTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    oaLevelEvaluationTypeID= middleOALevelEvaluationTypeList[position].EvaluationTypeId.toString()
                    callOALevelEvaluationAPi(oaLevelEvaluationTypeID)


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            olevelEvaluationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.v("callMiddleEvaluationTypeAPi","setMiddleSpinnerListener evaluationTypeList size "+middleOALevelEvaluationTypeList.size)

                    oaLevelEvaluationID= evaluationList[position].EvaluationId.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    oaLevelEvaluationID="0"
                }
            }

            showButton.setOnClickListener {
                Log.v("evaluation","evaluationTypeID "+ evaluationTypeID)
                Log.v("evaluation","eovEvaluationTypeID "+ eovEvaluationTypeID)
                Log.v("evaluation","myeEvaluationTypeID "+ myeEvaluationTypeID)
                Log.v("evaluation","examEvaluationID "+ examEvaluationID)
                Log.v("evaluation","termEvaluationID "+ termEvaluationID)

                if(args.eoy){
                    callEOYReportAPi()
                }else if (args.mye){
                    callMYEReportAPi()
                }else if (args.middle){
                    callMiddleReportAPi()
                } else if (args.olevel) {
                    callOALevelReportAPi()
                }

            }

        }

        callEvaluationTypeAPi()


    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // month is 0-based, so add +1
                val pickedDate = Calendar.getInstance()
                pickedDate.set(year, month, dayOfMonth)

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = sdf.format(pickedDate.time)

                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)


        )

        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun callMiddleEvaluationTypeAPi(){
        Log.v("callMiddleEvaluationTypeAPi","callMiddleEvaluationTypeAPi")
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["MobileCode"] = AppConstants.MOBILE_CODE
        viewModel.getMiddleEvaluationType(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        middleOALevelEvaluationTypeList = serverResponse.data.body()!!.EvaluationTypeList!!
                        Log.v("callMiddleEvaluationTypeAPi","evaluationTypeList "+middleOALevelEvaluationTypeList.size)


                        adapterMiddleEvaluationType=setMiddleOALevelEvaluationTypeSpinnerAdapter()
                        binding.middleEvaluationTypeSpinner.adapter = adapterMiddleEvaluationType




                    } else {
                        Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }



    private fun callOALevelEvaluationTypeAPi(){
        Log.v("callOALevelEvaluationTypeAPi","callMiddleEvaluationTypeAPi")
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["MobileCode"] = AppConstants.MOBILE_CODE
        viewModel.getOALevelEvaluationType(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        middleOALevelEvaluationTypeList = serverResponse.data.body()!!.EvaluationTypeList!!
                        Log.v("callOALevelEvaluationTypeAPi","evaluationList "+middleOALevelEvaluationTypeList.size)


                        adapterOALevelEvaluationType=setMiddleOALevelEvaluationTypeSpinnerAdapter()
                        binding.olevelEvaluationTypeSpinner.adapter = adapterOALevelEvaluationType


                    } else {
                        Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }


    private fun callEvaluationTypeAPi(){
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        viewModel.getEvaluationType(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        evaluationTypeList = serverResponse.data.body()!!.EvaluationTypeList!!
                        /* adapterEvaluationType = ArrayAdapter<EvaluationTypeListData>(
                             requireContext(),
                             R.layout.custom_spinner_textview,
                             evaluationTypeList
                         )
                         adapterEvaluationType!!.setDropDownViewResource(R.layout.custom_spinner_textview)
                         binding.evaluationTypeSpinner.adapter = adapterEvaluationType*/
                        /* customResultDialogLayoutBinding.spinnerEvaluationGroup.adapter =
                             adapterEvaluationGroup*/

                        adapterEvaluationType=setEvaluationTypeSpinnerAdapter()
                        adapterEOVEvaluationType=setEvaluationTypeSpinnerAdapter()
                        adapterMYEEvaluationType=setEvaluationTypeSpinnerAdapter()
                        binding.evaluationTypeSpinner.adapter = adapterEvaluationType
                        binding.myeEvaluationTypeSpinner.adapter = adapterMYEEvaluationType
                        binding.eoyEvaluationTypeSpinner.adapter = adapterEOVEvaluationType

                    } else {
                        Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    fun setEvaluationTypeSpinnerAdapter(): ArrayAdapter<EvaluationTypeListData>  {
        val adapter = ArrayAdapter<EvaluationTypeListData>(
            requireContext(),
            R.layout.report_custom_spinner_textview,
            evaluationTypeList
        )

        adapter.setDropDownViewResource(R.layout.report_custom_spinner_textview)
        return adapter
    }

    fun setMiddleOALevelEvaluationTypeSpinnerAdapter(): ArrayAdapter<EvaluationTypeListData>  {
        val adapter = ArrayAdapter<EvaluationTypeListData>(
            requireContext(),
            R.layout.report_custom_spinner_textview,
            middleOALevelEvaluationTypeList
        )

        adapter.setDropDownViewResource(R.layout.report_custom_spinner_textview)
        return adapter
    }

    private fun callEvaluationAPi(evaluationTypeID: String) {
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["EvaluationTypeId"] = evaluationTypeID
        viewModel.getEvaluation(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        evaluationList = serverResponse.data.body()!!.EvaluationList!!


                        adapterEvaluationExam=setEvaluationSpinnerAdapter()
                        adapterEvaluationTerm=setEvaluationSpinnerAdapter()


                        binding.selectExamSpinner.adapter = adapterEvaluationExam
                        binding.selectTermSpinner.adapter = adapterEvaluationTerm


                    } else {
                        Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    private fun callMiddleEvaluationAPi(evaluationTypeID: String) {
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["EvaluationTypeId"] = evaluationTypeID
        viewModel.getMiddleEvaluation(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        evaluationList = serverResponse.data.body()!!.EvaluationList!!


                        adapterMiddleEvaluation=setEvaluationSpinnerAdapter()



                        binding.middleEvaluationSpinner.adapter = adapterMiddleEvaluation



                    } else {
                        Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    private fun callOALevelEvaluationAPi(evaluationTypeID: String) {
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["EvaluationTypeId"] = evaluationTypeID
        viewModel.getOALevelEvaluation(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        evaluationList = serverResponse.data.body()!!.EvaluationList!!


                        adapterOALevelEvaluation=setEvaluationSpinnerAdapter()



                        binding.olevelEvaluationSpinner.adapter = adapterOALevelEvaluation



                    } else {
                        Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    fun setEvaluationSpinnerAdapter(): ArrayAdapter<EvaluationListData>  {
        val adapter = ArrayAdapter<EvaluationListData>(
            requireContext(),
            R.layout.report_custom_spinner_textview,
            evaluationList
        )

        adapter.setDropDownViewResource(R.layout.report_custom_spinner_textview)
        return adapter
    }

    private fun callMYEReportAPi() {
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["EvaluationTypeId"] = evaluationTypeID
        fields["ExamId"] = examEvaluationID
        fields["TermId"] = termEvaluationID
        fields["FromDate"] = fromDateText
        fields["ToDate"] = toDateText
        fields["ExamDate"] = examDateText
        viewModel.getMYEReport(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        if(serverResponse.data.body()!!.toString().isNotEmpty()){
                            (activity as DashboardActivity).goToLink(serverResponse.data.body()!!.toString())
                        } else {
                            Toast.makeText(requireContext(), "No File available to download", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "No File available to download", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    private fun callEOYReportAPi() {
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["EvaluationTypeId_MYE"] = myeEvaluationTypeID
        fields["EvaluationTypeId_EOY"] = eovEvaluationTypeID
        fields["ExamId"] = examEvaluationID
        fields["TermId"] = termEvaluationID
        fields["FromDate"] = fromDateText
        fields["ToDate"] = toDateText
        fields["ExamDate"] = examDateText
        viewModel.getEOYReport(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        if(serverResponse.data.body()!!.toString().isNotEmpty()){
                            (activity as DashboardActivity).goToLink(serverResponse.data.body()!!.toString())
                        } else {
                            Toast.makeText(requireContext(), "No File available to download", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "No File available to download", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    private fun callMiddleReportAPi() {
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["EvaluationTypeId"] = middleEvaluationTypeID
        fields["EvaluationIDFk"] = middleEvaluationID

        viewModel.getMiddleReport(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        if(serverResponse.data.body()!!.toString().isNotEmpty()){
                            (activity as DashboardActivity).goToLink(serverResponse.data.body()!!.toString())
                        } else {
                            Toast.makeText(requireContext(), "No File available to download", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "No File available to download", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    private fun callOALevelReportAPi() {
        val fields: HashMap<String, Any> = HashMap()
        fields["UserIdentity"] = sharedPrefsHelper.getUserId().toString()
        fields["StudentId"] = AppConstants.STUDENT_ID
        fields["EvaluationTypeId"] = oaLevelEvaluationTypeID
        fields["EvaluationIDFk"] = oaLevelEvaluationID

        viewModel.getOALevelReport(fields).observe(viewLifecycleOwner) { serverResponse ->
            when (serverResponse.status) {
                Status.LOADING -> {
                    AppConstants.startLoader(requireActivity())
                }

                Status.SUCCESS -> {
                    AppConstants.stopLoader()
                    if (serverResponse.data != null) {
                        if(serverResponse.data.body()!!.toString().isNotEmpty()){
                            (activity as DashboardActivity).goToLink(serverResponse.data.body()!!.toString())
                        } else {
                            Toast.makeText(requireContext(), "No File available to download", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "No File available to download", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.ERROR -> {
                    AppConstants.stopLoader()
                    Toast.makeText(requireContext(), serverResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

}