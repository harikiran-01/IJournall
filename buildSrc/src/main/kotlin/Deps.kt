/**
 * Plugins
 */
object BuildPlugins {
    const val gradle = "com.android.tools.build:gradle:${PluginVersions.gradlePlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersions.kotlin}"
    const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${PluginVersions.safeArgs}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${PluginVersions.hilt}"
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
    const val androidCore = "androidx.core:core-ktx:${DepsVersions.androidCore}"
    const val appCompat = "androidx.appcompat:appcompat:${DepsVersions.appCompat}"
    const val materialDesign = "com.google.android.material:material:${DepsVersions.materialDesign}"

    // Navigation
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${DepsVersions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${DepsVersions.navigation}"
    // DI
    const val hilt = "com.google.dagger:hilt-android:${DepsVersions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${DepsVersions.hilt}"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-fragment:${DepsVersions.hiltNav}"

    // room
    const val room = "androidx.room:room-runtime:${DepsVersions.room}"
    const val roomKtx = "androidx.room:room-ktx:${DepsVersions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${DepsVersions.room}"

    // lifecycle
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${DepsVersions.lifecycle}"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${DepsVersions.lifecycle}"


    const val fragmentKtx = "androidx.fragment:fragment-ktx:${DepsVersions.fragmentKtx}"
}

object TestDeps {
    const val jUnit = "junit:junit:${TestDepsVersions.jUnit}"
}