plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
    id ("kotlin-parcelize")
    //id ("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.musuemguide"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musuemguide"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.2")
    implementation("com.google.firebase:firebase-auth:22.3.1")

    //ViewModel
    kapt ("androidx.lifecycle:lifecycle-compiler:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    testImplementation("junit:junit:4.13.2")
    implementation("androidx.activity:activity-ktx:1.9.0-alpha01")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.1")

    //sdp Library
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    //ssp Library
    implementation ("com.intuit.ssp:ssp-android:1.1.0")


    //Material Design Navigation Bar
    implementation("com.google.android.material:material:1.12.0-alpha02")

    implementation ("com.budiyev.android:code-scanner:2.1.0")

    //Room dependencies
    /*implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")*/

    //Coroutines Dependencies
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    //Room dependencies
    val room_version = "2.5.2"
    //Room Database
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:2.4.0")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")


    //Coroutines Dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    //Koin-core
    implementation("io.insert-koin:koin-android:3.3.2")

    //Material 3
    implementation ("com.google.android.material:material:1.9.0")
    // Koin main features for Android

    //Pagination
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.room:room-paging:2.6.1")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
}