// --- build.gradle.kts (Project Level) ---
// File ini adalah build.gradle untuk root proyek Anda (di folder paling atas).
// Pastikan Anda menempatkan kode ini di file build.gradle.kts yang benar.

plugins {
    // Ini adalah plugin Android Gradle Plugin dan Kotlin Gradle Plugin.
    // Sesuaikan versi jika Android Studio Anda menyarankan versi yang berbeda.
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    // Ini adalah plugin Google Services yang diperlukan untuk Firebase.
    // Pastikan versi ini sesuai atau lebih baru dari yang disarankan Firebase.
    id("com.google.gms.google-services") version "4.4.2" apply false
}