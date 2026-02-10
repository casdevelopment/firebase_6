package com.example.esm.network.koin_module




import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import com.example.esm.alertssms.viewmodels.AlertSmsViewModel
import com.example.esm.attendance.viewmodels.AttendanceViewModel
import com.example.esm.complaint.viewmodels.ComplaintViewModel
import com.example.esm.dashboardfragment.viewmodels.DashboardFragmentViewModel
import com.example.esm.diary.viewmodels.DiaryViewModel
import com.example.esm.eventcalendar.viewmodels.EventsViewModel
import com.example.esm.feecard.viewmodels.FeeCardViewModel
import com.example.esm.hostel.viewmodels.HostelViewModel
import com.example.esm.login.viewmodels.LoginViewModel
import com.example.esm.notice.viewModel.CommonApiViewModel
import com.example.esm.paymenthistory.viewmodels.PaymentViewModel
import com.example.esm.policy.PolicyViewModel
import com.example.esm.report.ReportViewModel
import com.example.esm.results.viewmodels.ResultViewModel
import com.example.esm.signup.viewmodel.SignUpViewModel
import com.example.esm.studentconsultancy.viewmodels.StudentConsultancyViewModel
import com.example.esm.studenttimetable.viewmodels.TimeTableViewModel
import com.example.esm.utils.AppConstants.SHARED_PREF_NAME
import com.example.esm.utils.SharedPrefsHelper
import com.example.esm.welcome.viewmodels.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
@Keep
val sharedPreference = module {
    single {
        provideSharedPreference(get())
    }
    single {
        provideEditor(get())
    }
    single {
        SharedPrefsHelper(get())
    }
}
val repoModule = module {
    factory {
        Repository(get())
    }

}
val viewModelModule= module {
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        SignUpViewModel(get())
    }
    viewModel {
        WelcomeViewModel(get())
    }
    viewModel {
        PaymentViewModel(get())
    }
    viewModel {
        AttendanceViewModel(get())
    }
    viewModel {
        ResultViewModel(get())
    }
    viewModel {
        AlertSmsViewModel(get())
    }
    viewModel {
        FeeCardViewModel(get())
    }
    viewModel {
        TimeTableViewModel(get())
    }
    viewModel {
        StudentConsultancyViewModel(get())
    }
    viewModel {
        HostelViewModel(get())
    }
    viewModel {
        ComplaintViewModel(get())
    }
    viewModel {
        DashboardFragmentViewModel(get())
    }
    viewModel {
        DiaryViewModel(get())
    }
    viewModel {
        CommonApiViewModel(get())
    }
    viewModel {
        EventsViewModel(get())
    }
    viewModel {
        PolicyViewModel(get())
    }
    viewModel {
        ReportViewModel(get())
    }

}
fun provideSharedPreference(appContext: Context): SharedPreferences {
    return appContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
}
fun provideEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
    return sharedPreferences.edit()
}


