plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.nima.openbooksdownloader"
    compileSdk = 35

    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

    defaultConfig {
        applicationId = "com.nima.openbooksdownloader"
        minSdk = 21
        targetSdk = 33
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //DataStore
    implementation(libs.datastore)

    //Coroutines
    implementation (libs.coroutine.core)
    implementation (libs.coroutine.android)

    //ViewModel
    implementation (libs.lifecycle.viewmodel)
    implementation (libs.lifecycle.runtime)

    //navigation
    implementation(libs.navigation)

    //Koin
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.navigation)

    //Retrofit
    implementation(libs.retrofit)

    //Gson
    implementation (libs.gson)

    //Coil
    implementation(libs.coil)

    //Room database
    implementation (libs.room.runtime)
    ksp(libs.room.compiler)
    implementation (libs.room.ktx)


    implementation (libs.core.ktx)
    implementation (libs.activity.compose)
    implementation (libs.compose.ui)
    implementation (libs.compose.ui.tooling.preview)
    implementation (libs.compose.material3)
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.espresso.core)
    androidTestImplementation (libs.ui.test.junit4)
    debugImplementation (libs.ui.tooling)
    debugImplementation (libs.ui.test.manifest)
}