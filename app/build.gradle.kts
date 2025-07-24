// --- build.gradle.kts (Module: app) ---
// File ini adalah build.gradle untuk modul 'app' menggunakan Kotlin DSL (.kts)
// Pastikan Anda mengganti seluruh isi file build.gradle.kts (Module: app) dengan kode ini.

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services") // Terapkan plugin di sini
}

android {
    namespace = "com.example.bapendacjrapp" // Pastikan nama namespace sesuai
    compileSdk = 34 // Sesuaikan dengan compileSdk Anda

    defaultConfig {
        applicationId = "com.example.bapendacjrapp" // Pastikan ID aplikasi sesuai
        minSdk = 24 // Sesuaikan dengan minSdk Anda
        targetSdk = 34 // Sesuaikan dengan targetSdk Anda
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true // Jika Anda ingin menggunakan View Binding (direkomendasikan)
        compose = false // Pastikan ini false jika Anda tidak menggunakan Compose
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1") // Versi terbaru
    implementation("androidx.appcompat:appcompat:1.6.1") // Versi terbaru
    implementation("com.google.android.material:material:1.12.0") // Versi terbaru
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Versi terbaru
    implementation("androidx.cardview:cardview:1.0.0") // Untuk CardView

    // Firebase BOM (Bill of Materials) - Selalu gunakan versi BOM terbaru
    // Periksa versi terbaru di: https://firebase.google.com/docs/android/setup#available-libraries
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

    // Firebase Authentication (untuk mendapatkan User ID)
    implementation("com.google.firebase:firebase-auth-ktx")

    // Firebase Cloud Firestore (untuk menyimpan ulasan)
    implementation("com.google.firebase:firebase-firestore-ktx")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
