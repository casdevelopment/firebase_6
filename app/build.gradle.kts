plugins {
    id("com.android.application")
    id("kotlin-kapt")
    id ("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
}

android {
    signingConfigs {



        create("KPSI") {
            storeFile = file("E:\\AndroidProject\\ESM_Firebase6\\KPSI\\kpsi.jks")
            storePassword = "esm123*"
            keyAlias = "esm"
            keyPassword = "esm123*"
        }
        create("FourStarsSchoolAcademy") {
            storeFile = file("E:\\AndroidProject\\ESM_Firebase6\\fssa\\fssa.jks")
            storePassword = "esm123*"
            keyAlias = "esm"
            keyPassword = "esm123*"
        }
        create("RockhillAcademy") {
            storeFile = file("E:\\AndroidProject\\ESM_Firebase6\\rha\\rha.jks")
            storePassword = "esm123*"
            keyAlias = "esm"
            keyPassword = "esm123*"
        }
        create("EduactorsKalaske") {
            storeFile = file("E:\\AndroidProject\\ESM_Firebase6\\edukala\\edukala.jks")
            storePassword = "esm123*"
            keyAlias = "esm"
            keyPassword = "esm123*"
        }
        create("CitiGrammarSchoolSialkot") {
            storeFile = file("E:\\AndroidProject\\ESM_Firebase6\\cgss\\cgss.jks")
            storePassword = "esm123*"
            keyAlias = "esm"
            keyPassword = "esm123*"
        }
        create("SunAcademy") {
            storeFile = file("E:\\AndroidProject\\ESM_Firebase6\\sun\\sun.jks")
            storePassword = "esm123*"
            keyAlias = "esm"
            keyPassword = "esm123*"
        }
        create("GEIs") {
            storeFile = file("E:\\AndroidProject\\ESM_Firebase6\\GEIs\\GEIs.jks")
            storePassword = "esm123*"
            keyAlias = "esm"
            keyPassword = "esm123*"
        }
        create("AgoraSignatureSchools") {
            storeFile = file("E:\\AndroidProject\\ESM_Firebase6\\Agora\\Agora.jks")
            storePassword = "esm123*"
            keyAlias = "esm"
            keyPassword = "esm123*"
        }


    }


    namespace = "com.example.esm"
    compileSdk = 36

    defaultConfig {
        //       applicationId = "com.cyberisol.esm"
        minSdk = 24
        targetSdk = 36
//        versionCode = 1
//        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    // product flavor
    flavorDimensions += "esm"
    productFlavors {



        create("KPSI") {
            applicationId = "com.kpsi.esm"
            dimension = "esm"
            versionCode = 1
            versionName = "0.1"
            resValue("string", "app_name", "KPSI")
            resValue("string", "base_url", "https://apiesm.cyberasol.com/api/Mobile/")
            resValue("string", "mobile_code", "344")
            manifestPlaceholders["authorities"] = "$applicationId.provide"
            manifestPlaceholders["launcher"] = "@mipmap/ic_launcher_kpsi"
            manifestPlaceholders["launcher_round"] = "@mipmap/ic_launcher_round_kpsi"
        }
        create("FourStarsSchoolAcademy") {
            applicationId = "com.fssa.esm"
            dimension = "esm"
            versionCode = 3
            versionName = "0.3"
            resValue("string", "app_name", "Four Stars School & Academy")
            resValue("string", "base_url", "https://apiesm.cyberasol.com/api/Mobile/")
            resValue("string", "mobile_code", "163")
            manifestPlaceholders["authorities"] = "$applicationId.provide"
            manifestPlaceholders["launcher"] = "@mipmap/ic_launcher_fssa"
            manifestPlaceholders["launcher_round"] = "@mipmap/ic_launcher_round_fssa"
        }

        create("RockhillAcademy") {
            applicationId = "com.rha.esm"
            dimension = "esm"
            versionCode = 5
            versionName = "0.5"
            resValue("string", "app_name", "Rockhill Academy")
            resValue("string", "base_url", "https://apiesm.cyberasol.com/api/Mobile/")
            resValue("string", "mobile_code", "320")
            manifestPlaceholders["authorities"] = "$applicationId.provide"
            manifestPlaceholders["launcher"] = "@mipmap/ic_launcher_rha"
            manifestPlaceholders["launcher_round"] = "@mipmap/ic_launcher_round_rha"
        }
        create("EduactorsKalaske") {
            applicationId = "com.edukala.esm"
            dimension = "esm"
            versionCode = 1
            versionName = "0.1"
            resValue("string", "app_name", "Eduactors Kalaske")
            resValue("string", "base_url", "https://apiesm.cyberasol.com/api/Mobile/")
            resValue("string", "mobile_code", "345")
            manifestPlaceholders["authorities"] = "$applicationId.provide"
            manifestPlaceholders["launcher"] = "@mipmap/ic_launcher_edukala"
            manifestPlaceholders["launcher_round"] = "@mipmap/ic_launcher_round_edukala"
        }
        create("CitiGrammarSchoolSialkot") {
            applicationId = "com.cgss.esm"
            dimension = "esm"
            versionCode = 1
            versionName = "0.1"
            resValue("string", "app_name", "CGS Sialkot")
            resValue("string", "base_url", "https://apiesm.cyberasol.com/api/Mobile/")
            resValue("string", "mobile_code", "347")
            manifestPlaceholders["authorities"] = "$applicationId.provide"
            manifestPlaceholders["launcher"] = "@mipmap/ic_launcher_cgss"
            manifestPlaceholders["launcher_round"] = "@mipmap/ic_launcher_round_cgss"
        }
        create("SunAcademy") {
            applicationId = "com.sunacademy.esm"
            dimension = "esm"
            versionCode = 1
            versionName = "0.1"
            resValue("string", "app_name", "Sun Academy")
            resValue("string", "base_url", "https://portal.sunacademy.com.pk/api/api/Mobile/")
            resValue("string", "mobile_code", "343")
            manifestPlaceholders["authorities"] = "$applicationId.provide"
            manifestPlaceholders["launcher"] = "@mipmap/ic_launcher_sun"
            manifestPlaceholders["launcher_round"] = "@mipmap/ic_launcher_round_sun"
        }
        create("GEIs") {
            applicationId = "com.geis.esm"
            dimension = "esm"
            versionCode = 3
            versionName = "0.3"
            resValue("string", "app_name", "GEIs")
            resValue("string", "base_url", "https://coastguards.cyberasol.com/api/api/Mobile/")
            resValue("string", "mobile_code", "346")
            manifestPlaceholders["authorities"] = "$applicationId.provide"
            manifestPlaceholders["launcher"] = "@mipmap/ic_launcher_geis"
            manifestPlaceholders["launcher_round"] = "@mipmap/ic_launcher_round_geis"
        }

        create("AgoraSignatureSchools") {
            applicationId = "com.agora.esm"
            dimension = "esm"
            versionCode = 2
            versionName = "0.2"
            resValue("string", "app_name", "Agora Signature Schools")
            resValue("string", "base_url", "https://apiesm.cyberasol.com/api/Mobile/")
            resValue("string", "mobile_code", "351")
            manifestPlaceholders["authorities"] = "$applicationId.provide"
            manifestPlaceholders["launcher"] = "@mipmap/ic_launcher_agora"
            manifestPlaceholders["launcher_round"] = "@mipmap/ic_launcher_round_agora"
        }

    }


    buildTypes {
        release {
            isShrinkResources=false
            isMinifyEnabled = false
            isDebuggable = false


            signingConfig = signingConfigs.getByName("KPSI")
            signingConfig = signingConfigs.getByName("FourStarsSchoolAcademy")
            signingConfig = signingConfigs.getByName("RockhillAcademy")
            signingConfig = signingConfigs.getByName("EduactorsKalaske")
            signingConfig = signingConfigs.getByName("CitiGrammarSchoolSialkot")
            signingConfig = signingConfigs.getByName("SunAcademy")
            signingConfig = signingConfigs.getByName("GEIs")
            signingConfig = signingConfigs.getByName("AgoraSignatureSchools")


            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
//    buildFeatures {
//        viewBinding= true
//    }
//    dataBinding {
//        enabled = true
//    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures{
        dataBinding = true
    }

}

dependencies {
    // implementation ("fileTree(dir: 'libs', include: ['*.jar'])")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // implementation("com.google.android.play:core-ktx:1.10.3")
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    //  implementation("androidx.activity:activity:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //retrofit
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")

    //coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    //Add KTX dependencies
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    //koin
    // Koin Core features
    implementation ("io.insert-koin:koin-core:3.2.1")
    // Koin main features for Android
    implementation ("io.insert-koin:koin-android:3.2.1")
    //glide
    implementation ("com.github.bumptech.glide:glide:4.13.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")
    //circle image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")

    implementation ("com.varunbarad:highlightable-calendar-view:1.0.0")



    /*ViewPager2*/
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    /////
    implementation (project(":calendar"))
    ////



    // In App Update
    // implementation ("com.google.android.play:core:1.10.3")












}
