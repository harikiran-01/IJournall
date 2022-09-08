/**
 * Plugins
 */
object BuildPlugins {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.safeArgs}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
}

object AppPlugins {
    const val oneSignal = "com.onesignal.androidsdk.onesignal-gradle-plugin"
    const val android = "com.android.application"
    const val kotlin = "kotlin-android"
    const val google = "com.google.gms.google-services"
    const val extensions = "kotlin-android-extensions"
    const val kapt = "kotlin-kapt"
    const val crashlytics = "com.google.firebase.crashlytics"
    const val performanceMonitor = "com.google.firebase.firebase-perf"
}

object Deps {
    const val androidCore = "androidx.core:core-ktx:${Versions.androidCore}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
}