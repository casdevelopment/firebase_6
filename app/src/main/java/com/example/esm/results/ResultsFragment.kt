package com.example.esm.results

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.esm.BuildConfig
import com.example.esm.R
import com.example.esm.dashboardactivity.DashboardActivity
import com.example.esm.databinding.FragmentResultsBinding
import com.example.esm.network.Status
import com.example.esm.results.adapters.ResultsAdapter
import com.example.esm.results.models.KeyValueModel
import com.example.esm.results.models.ResultMasterModel
import com.example.esm.results.viewmodels.ResultViewModel
import com.example.esm.utils.AppConstants
import com.example.esm.utils.AppUtils
import com.example.esm.utils.KEY_DUMMY_LOGIN
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.models.IdentityModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.text.DecimalFormat
import java.util.Calendar


class ResultsFragment : Fragment() {
    lateinit var binding: FragmentResultsBinding

    var permissionCode = 101
    private var pdfDocument: PdfDocument? = null
    private var canvas: Canvas? = null
    private var myPageInfo: PdfDocument.PageInfo? = null
    private lateinit var myPage: PdfDocument.Page
    private val pageHeight = 1120
    private val pageWidth = 792
    private var scaledBmp: Bitmap? = null
    private lateinit var pdfData: List<ResultMasterModel>
    var monthrange :Int = 0
    val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val viewModel: ResultViewModel by viewModel()
    private lateinit var evaluationDetail: ArrayList<ResultMasterModel>

    var descendingMonth: ArrayList<KeyValueModel> = ArrayList()
    //var descendingMonth: MutableList<KeyValueModel> = mutableListOf()
    var descendingYear: ArrayList<KeyValueModel> = ArrayList()

    var currentMonth: Int = 0
    var currentYear: Int = 0
    var startYear: Int = 0
    var modelYear: Int = 0
    var modelMonth: Int = 0
    var monthList: ArrayList<KeyValueModel> = ArrayList()

    private val yearSpinnerModel: KeyValueModel? = null
    private val monthSpinnerModel: KeyValueModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_results,
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
        val calendar = Calendar.getInstance()
        currentMonth = calendar.get(Calendar.MONTH) + 1 // beware of month indexing from zero
        Log.d("currentMonth", "$currentMonth")
        currentYear = calendar.get(Calendar.YEAR)
        startYear = 2019
        populateYear()
       // populateMonth(modelYear != currentYear)
        initView()
        (activity as DashboardActivity).backArrowVisible()
        (activity as DashboardActivity).drawIconInvisible()



    }

    private fun initView() {
        if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
            binding.resultRecyclerView.visibility= View.GONE
            binding.noItem.visibility= View.VISIBLE
        } else {
            setDataForApi()
        }
        binding.showResult.setOnClickListener {
            if (sharedPrefsHelper.getBoolean(KEY_DUMMY_LOGIN, false)){
                binding.resultRecyclerView.visibility= View.GONE
                binding.noItem.visibility= View.VISIBLE
                Toast.makeText(requireContext(), "No result is available", Toast.LENGTH_SHORT).show()
            } else {
                setDataForApi()
            }
        }
        binding.getPdf.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                generatePDF()

            }else{
                checkPermission()

            }

        }
        binding.monthSpinner.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                modelMonth = if (position!= 0){
                    descendingMonth[position].Id.toInt()

                }else{
                    currentMonth
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        binding.yearSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                modelYear = if (position!= 0){
                    descendingYear[position].Item.toInt()
                }else{
                    currentYear
                }
                populateMonth(modelYear!= currentYear)


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        if (requireContext().packageName.equals("com.bass.esm") ||
            requireContext().packageName.equals("com.jaschoolsystem.esm")){
            binding.downloadResult.visibility= VISIBLE
            binding.downloadResult.setOnClickListener {
                downloadResultApi()
            }
        }
        if (requireContext().packageName.equals("com.jaschoolsystem.esm")){
            binding.downloadWeeklyResult.visibility= VISIBLE
            binding.downloadWeeklyResult.setOnClickListener {
                downloadWeeklyResultApi()
            }
        }



    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE
        )!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE),permissionCode)
        }else{
            generatePDF()

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF()
                //setDataForDownloadVoucher()
                //callDownloadFeeVoucherApi()

            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), permissionCode
                )

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun generatePDF() {
        pdfDocument = PdfDocument()
        setPdfPage()
        drawBitmap()
        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        val title = Paint()
        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15f

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(requireContext(), R.color.black)


        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        var x = 100
        var y = 100
        canvas!!.drawText("Student ID", x.toFloat(), y.toFloat(), title)
        x = x + 10
        canvas!!.drawText(" :", x.toFloat(), y.toFloat(), title)
        x = x + 100
        canvas!!.drawText(
           AppConstants.STUDENT_ID.toString(),
            x.toFloat(),
            y.toFloat(),
            title
        )
        x = 100
        y = y + 20
        canvas!!.drawText("Name", x.toFloat(), y.toFloat(), title)
        x = x + 10
        canvas!!.drawText(" :", x.toFloat(), y.toFloat(), title)
        x = x + 100
        canvas!!.drawText(
           AppConstants.STUDENT_NAME,
            x.toFloat(),
            y.toFloat(),
            title
        )

        x = 100
        y = y + 20
        canvas!!.drawText("Class", x.toFloat(), y.toFloat(), title)
        x = x + 10
        canvas!!.drawText(" :", x.toFloat(), y.toFloat(), title)
        x = x + 100
        canvas!!.drawText(
            AppConstants.CLASS_NAME,
            x.toFloat(),
            y.toFloat(),
            title
        )
        x = 100
        y = y + 20
        canvas!!.drawText("School", x.toFloat(), y.toFloat(), title)
        x = x + 10
        canvas!!.drawText(" :", x.toFloat(), y.toFloat(), title)
        x = x + 100
        canvas!!.drawText(
            AppConstants.SCHOOL_NAME,
            x.toFloat(),
            y.toFloat(),
            title
        )
        if (pdfData!!.size > 0) {
            for (i in pdfData!!.indices) {
                var totalMarksSum = 0.0
                var obtainedMarksSum = 0.00
                val precision = DecimalFormat("0.0")
                var totalPercentage = 0.00
                x = 100
                y = y + 50
                if (checkPageHeight(y)) {
                    y = 100
                }
                canvas!!.drawText("Evaluation :", x.toFloat(), y.toFloat(), title)
                x = x + 100
                canvas!!.drawText(
                    pdfData[i].EvaluationName,
                    x.toFloat(),
                    y.toFloat(),
                    title
                )
                x = 100
                y = y + 30
                canvas!!.drawText("Date", x.toFloat(), y.toFloat(), title)
                canvas!!.drawText("Subject", (x + 100).toFloat(), y.toFloat(), title)
                canvas!!.drawText("Total Marks", (x + 220).toFloat(), y.toFloat(), title)
                canvas!!.drawText("Obt Marks", (x + 340).toFloat(), y.toFloat(), title)
                canvas!!.drawText("Percentage", (x + 440).toFloat(), y.toFloat(), title)
                title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
                for (j in 0 until pdfData[i].EvaluationDetails.size) {
                    y = y + 25
                    if (checkPageHeight(y)) {
                        y = 100
                    }
                    val date: String =
                        pdfData[i].EvaluationDetails[j].EvaluationDate
                    val separated = date.split("T".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    canvas!!.drawText(separated[0], x.toFloat(), y.toFloat(), title)
                    canvas!!.drawText(
                        pdfData[i].EvaluationDetails[j].Subject,
                        (x + 100).toFloat(),
                        y.toFloat(),
                        title
                    )
                    canvas!!.drawText(
                        pdfData[i].EvaluationDetails[j].TotalMarks.toString(),
                        (x + 250).toFloat(),
                        y.toFloat(),
                        title
                    )
                    canvas!!.drawText(
                        pdfData[i].EvaluationDetails[j].ObtainedMarks.toString(),
                        (x + 350).toFloat(),
                        y.toFloat(),
                        title
                    )
                    canvas!!.drawText(
                        pdfData[i].EvaluationDetails[j].Percentage.toString()  + "%",
                        (x + 450).toFloat(),
                        y.toFloat(),
                        title
                    )
                    totalMarksSum =
                        totalMarksSum + pdfData[i].EvaluationDetails[j].TotalMarks
                    obtainedMarksSum =
                        obtainedMarksSum + pdfData[i].EvaluationDetails[j].ObtainedMarks
                }
                title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
                y = y + 30
                if (checkPageHeight(y)) {
                    y = 100
                }
                canvas!!.drawText("Total", x.toFloat(), y.toFloat(), title)
                canvas!!.drawText(" ", x.toFloat(), y.toFloat(), title)
                canvas!!.drawText(
                    precision.format(totalMarksSum).toString(),
                    (x + 250).toFloat(),
                    y.toFloat(),
                    title
                )
                canvas!!.drawText(
                    precision.format(obtainedMarksSum).toString(),
                    (x + 350).toFloat(),
                    y.toFloat(),
                    title
                )
                if (totalMarksSum > 0.0) {
                    totalPercentage = obtainedMarksSum / totalMarksSum * 100
                    canvas!!.drawText(
                        precision.format(totalPercentage).toString() + "%",
                        (x + 450).toFloat(),
                        y.toFloat(),
                        title
                    )
                }
            }
        }


        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument!!.finishPage(myPage)

            // below line is used to set the name of
            // our PDF file and its path.
            var path :String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/"
        val dir: File = File(path)
        if (!dir.exists()) dir.mkdirs()
        val fileName =
            requireContext().getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".pdf"
        //   File filePath = new File(dir,"result.pdf");
        //   File filePath = new File(dir,"result.pdf");
        val filePath = File(dir, fileName)
        try {
            /*  File file;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                file= new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + " GFG.pdf");
            }
            else
            {
                file= new File(Environment.getExternalStorageDirectory().toString() + "/" + "GFG.pdf");
            }*/

            // after creating a file name we will
            // write our PDF file to that location.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pdfDocument!!.writeTo(Files.newOutputStream(filePath.toPath()))
            } else {
                pdfDocument!!.writeTo(FileOutputStream(filePath))
            }
            val intent = Intent(Intent.ACTION_VIEW) //
                .setDataAndType(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
                        requireContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        filePath
                    ) else Uri.fromFile(filePath),
                    "*/*"
                ).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            requireContext().startActivity(intent)
            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(requireContext(), "PDF File Generated Successfully.", Toast.LENGTH_SHORT)
                .show()
        /*    Toast.makeText(
                requireContext(),
                "PDF File Save in " + Environment.DIRECTORY_DOWNLOADS,
                Toast.LENGTH_SHORT
            ).show()*/
        } catch (e: IOException) {
            // below line is used
            // to handle error
            Toast.makeText(
                requireContext(),
                "Error Occur During Creating PDF File .",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument!!.close()


    }

    private fun checkPageHeight(y: Int): Boolean {
        val sum = pageHeight - y
        if (sum <= 135) {
            pdfDocument!!.finishPage(myPage)
            setPdfPage()
            return true
        }
        return false
    }

    private fun drawBitmap() {
        scaledBmp = getBitMApImage(AppConstants.STUDENT_SCHOOL_LOGO)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true

        scaledBmp = getResizedBitmap(scaledBmp!!, 70, 70)
        val x = 600f
        val y = 70f
        canvas!!.drawBitmap(scaledBmp!!, x, y, paint)


    }

    private fun getBitMApImage(image: String): Bitmap? {
        val pureBase64Encoded: String
        val decodedBytes: ByteArray
        pureBase64Encoded = image.substring(image.indexOf(',') + 1)
        decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

    private fun setPdfPage() {
        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        myPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        // below line is used for setting
        // start page for our PDF file.
        myPage = pdfDocument!!.startPage(myPageInfo)
        // creating a variable for canvas
        // from our page of PDF.
        canvas = myPage.canvas


    }

    private fun setDataForApi() {
        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
       // identityModel.Month = 3
        identityModel.Month = modelMonth
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        //identityModel.Year = 2023
        identityModel.Year = modelYear
        callStudentMonthlyExamHistory(identityModel)

    }

    private fun callStudentMonthlyExamHistory(model: IdentityModel) {
        viewModel.stdMonthlyExamHistory(model).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    AppUtils.startLoader(requireActivity())
                    Log.d("response", "loading")
                }

                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data != null) {
                        evaluationDetail = ArrayList()
                        if (apiResponse.data.isSuccessful) {
                            val response = apiResponse.data.body()
                            if (response!= null){
                                response.Evaluations?.let { it1 -> evaluationDetail.addAll(it1) }
                                setRecyclerView(evaluationDetail)
                                binding.resultRecyclerView.adapter = ResultsAdapter(evaluationDetail)
                            }else{
                                Toast.makeText(requireContext(), " api response is null", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            Log.d("response", "not successful")
                            Toast.makeText(requireContext(), " not successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                Status.ERROR -> {
                    Log.d("response", "error")
                    AppUtils.stopLoader()
                }
            }


        }

    }

    private fun downloadResultApi() {

        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        // identityModel.Month = 3
        identityModel.Month = modelMonth
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        //identityModel.Year = 2023
        identityModel.Year = modelYear


        viewModel.downloadResultApi(identityModel).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    AppUtils.startLoader(requireActivity())
                    Log.d("response", "loading")
                }

                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data != null) {
                        evaluationDetail = ArrayList()
                        if (apiResponse.data.isSuccessful) {
                            val response = apiResponse.data.body()
                            if (response!= null){
                                goToLink(response)
                                /*response.Evaluations?.let { it1 -> evaluationDetail.addAll(it1) }
                                setRecyclerView(evaluationDetail)
                                binding.resultRecyclerView.adapter = ResultsAdapter(evaluationDetail)*/
                            }else{
                               // Toast.makeText(requireContext(), " api response is null", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            Log.d("response", "not successful")
                            Toast.makeText(requireContext(), " not successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                Status.ERROR -> {
                    Log.d("response", "error")
                    AppUtils.stopLoader()
                }
            }


        }

    }

    private fun downloadWeeklyResultApi() {

        val identityModel = IdentityModel()
        identityModel.UserIdentity = sharedPrefsHelper.getUserId().toString()
        identityModel.MobileCode = sharedPrefsHelper.getUserMobileCode()
        // identityModel.Month = 3
        identityModel.Month = modelMonth
        identityModel.NotificationTypeId = 0
        identityModel.StudentId = AppConstants.STUDENT_ID
        //identityModel.Year = 2023
        identityModel.Year = modelYear


        viewModel.downloadWeeklyResultApi(identityModel).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse.status) {
                Status.LOADING -> {
                    AppUtils.startLoader(requireActivity())
                    Log.d("response", "loading")
                }

                Status.SUCCESS -> {
                    AppUtils.stopLoader()
                    if (apiResponse.data != null) {
                        evaluationDetail = ArrayList()
                        if (apiResponse.data.isSuccessful) {
                            val response = apiResponse.data.body()
                            if (response!= null){
                                goToLink(response)
                                /*response.Evaluations?.let { it1 -> evaluationDetail.addAll(it1) }
                                setRecyclerView(evaluationDetail)
                                binding.resultRecyclerView.adapter = ResultsAdapter(evaluationDetail)*/
                            }else{
                               // Toast.makeText(requireContext(), " api response is null", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            Log.d("response", "not successful")
                            Toast.makeText(requireContext(), " not successful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                Status.ERROR -> {
                    Log.d("response", "error")
                    AppUtils.stopLoader()
                }
            }


        }

    }

    private fun setRecyclerView(list: ArrayList<ResultMasterModel>) {
        if (!list.isEmpty()) {
            pdfData = list
            binding.noItem.visibility = View.GONE
            binding.resultRecyclerView.visibility = View.VISIBLE
            binding.getPdf.visibility = View.VISIBLE
        } else {
            binding.noItem.visibility = View.VISIBLE
            binding.resultRecyclerView.visibility = View.GONE
            binding.getPdf.visibility = View.GONE
        }

    }

    private fun populateYear() {
        var yearList: ArrayList<KeyValueModel> = ArrayList()
        for (i in startYear..currentYear) {
            yearList.add(KeyValueModel(i.toString(), i.toString()))
            Log.d("populateYear", "$yearList")
        }
            // Log.d("populateYear", "sorted array: ${yearList.reversed()}")
            // reversed() is used to arranage array in descending order
             descendingYear = yearList.reversed() as ArrayList<KeyValueModel>
            Log.d("populateYear", "sorted array: ${yearList.reversed()}")
            binding.yearSpinner.adapter = ArrayAdapter(
                requireActivity(),
                R.layout.custom_spinner_textview, descendingYear
            )
            (binding.yearSpinner.adapter as ArrayAdapter<*>).setDropDownViewResource(R.layout.custom_spinner_textview)




    }

    private fun populateMonth(bool: Boolean) {
        if (monthList.size>0){
            monthList.clear()
        }
       // var monthList: MutableList<KeyValueModel> = mutableListOf()
        if (bool){
            monthrange = 12
        }else{
            monthrange = currentMonth
        }
//        monthrange = currentMonth
        Log.d("monthRange", " $monthrange")
        for (i in 1..monthrange) {
            if (i == 1) {
                monthList.add(KeyValueModel(i.toString(), "January"))
            } else if (i == 2) {
                monthList.add(KeyValueModel(i.toString(), "February"))
            } else if (i == 3) {
                monthList.add(KeyValueModel(i.toString(), "March"))
            } else if (i == 4) {
                monthList.add(KeyValueModel(i.toString(), "April"))
            } else if (i == 5) {
                monthList.add(KeyValueModel(i.toString(), "May"))
            } else if (i == 6) {
                monthList.add(KeyValueModel(i.toString(), "June"))
            } else if (i == 7) {
                monthList.add(KeyValueModel(i.toString(), "July"))
            } else if (i == 8) {
                monthList.add(KeyValueModel(i.toString(), "August"))
            } else if (i == 9) {
                monthList.add(KeyValueModel(i.toString(), "September"))
            } else if (i == 10) {
                monthList.add(KeyValueModel(i.toString(), "October"))
            } else if (i == 11) {
                monthList.add(KeyValueModel(i.toString(), "Novermber"))
            } else {
                monthList.add(KeyValueModel(i.toString(), "December"))
            }
            Log.d("monthRange", " month range: $monthList")
            Log.v("monthRange", " month range: $monthList")

        }


        //if (!all) {
        // = monthList as ArrayList<KeyValueModel>
         //descendingMonth = monthList.reverse() as ArrayList<KeyValueModel>
      //  descendingMonth = monthList.reversed() as ArrayList<KeyValueModel>
//        monthList.sortByDescending {
//            it.Id
//        }
        if (monthList.size==1){
            descendingMonth = monthList
        }else{
            descendingMonth = monthList.reversed() as ArrayList<KeyValueModel>
        }

        Log.d("populateYear", "sorted array: ${monthList.reversed()}")
        binding.monthSpinner.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.custom_spinner_textview,
            descendingMonth
        )
        (binding.monthSpinner.adapter as ArrayAdapter<*>).setDropDownViewResource(R.layout.custom_spinner_textview)


    }
    private fun goToLink(link: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        } catch (ex: Exception) {
            Toast.makeText(
                requireContext(),
                "Url $link is invalid or no application found for this url to open",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

