package com.example.esm.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.esm.BuildConfig
import com.example.esm.R

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Objects

object AppUtils {



    fun progressDialog(context: Context): Dialog? {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dilaog_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }
    fun startLoader(activity: Activity) {
        if (!activity.isFinishing) {
            AppConstants.progressLoader = AppUtils.progressDialog(activity)
        }
    }
    fun stopLoader() {
        if (AppConstants.progressLoader != null) {
            AppConstants.progressLoader!!.dismiss()
        }
    }

    fun toRequestBody(value: String?): RequestBody {
        return value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }
    fun checkConnectivity(context:Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }



    fun iszeroCheck(phone: String): Boolean {
        val zeroCheckString = phone[0]
        val arr = zeroCheckString.toString()
        val splited = arr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        return splited[0] == "0"
    }
    fun isEntityCodeValid(code: String): Boolean {
        //return Patterns.EMAIL_ADDRESS.matcher(userName).matches();
        return code.length >= 1
    }
    fun isPasswordValid(code: String): Boolean {
        //return Patterns.EMAIL_ADDRESS.matcher(userName).matches();
        return code.length >= 8
    }

    fun loadImageImage(img: String?, imageView: ImageView, mCtx: Context,placeHolder:Int,errorImg:Int) {
        if (img != null) {
            Log.v("roundImage", "img " + img)
            Glide.with(mCtx)
                .load( img)
                .apply( RequestOptions().placeholder(placeHolder).error(errorImg))
                .into(imageView)
        }

    }


     fun downloadPdfFile(url: String,filename: String,context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                attachmentDownloadCompleteReceive,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                Context.RECEIVER_EXPORTED
            )
        } else {
            context.registerReceiver(
                attachmentDownloadCompleteReceive,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
        }

        val request = DownloadManager.Request(Uri.parse(url.toString()))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(filename)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setAllowedOverMetered(true)
        request.setAllowedOverRoaming(false)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        val dm =context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

    }

    var attachmentDownloadCompleteReceive: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0L)
                Log.d("downloadPdfFile", "ACTION_DOWNLOAD_COMPLETE ")

                Toast.makeText(context, "Downloading Complete ", Toast.LENGTH_LONG).show()

                openDownloadedAttachment(context, downloadId)
                stopLoader()
            }else{
                stopLoader()
            }
        }
    }

    @SuppressLint("Range")
    private fun openDownloadedAttachment(context: Context, downloadId: Long) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            val downloadLocalUri =
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            val downloadMimeType =
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE))
            Log.d("downloadPdfFile", "downloadLocalUri $downloadLocalUri")
            Log.d("downloadPdfFile", "downloadMimeType $downloadMimeType")
            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL && downloadLocalUri != null) {
                openDownloadedAttachment(context, Uri.parse(downloadLocalUri), downloadMimeType)
            }else if(downloadStatus== DownloadManager.STATUS_FAILED){
                Toast.makeText(context, "Downloading Failed ", Toast.LENGTH_LONG).show()
                stopLoader()
            }

        }else{
            stopLoader()
        }
        cursor.close()
    }

    private fun openDownloadedAttachment(
        context: Context,
        attachmentUri: Uri,
        attachmentMimeType: String
    ) {
        var attachmentUri: Uri? = attachmentUri
        if (attachmentUri != null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE == attachmentUri.scheme) {
                // FileUri - Convert it to contentUri.
                val file = File(Objects.requireNonNull(attachmentUri.path))
                attachmentUri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
                )
            }
            Log.v("attachmentMimeType", "attachmentMimeType $attachmentMimeType")
            val openAttachmentIntent = Intent(Intent.ACTION_VIEW)
            openAttachmentIntent.setDataAndType(attachmentUri, attachmentMimeType)
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(openAttachmentIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.unable_to_open_file),
                    Toast.LENGTH_LONG
                ).show()
                stopLoader()
            }
        }
        else{
            stopLoader()
        }
    }
    public fun goToLink(link: String,context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        } catch (ex: Exception) {
            Toast.makeText(
                context,
                "Url $link is invalid or no application found for this url to open",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}