plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {

    defaultConfig {
        minSdk = 21
        compileSdk = 32
        targetSdk = 32
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {
    api(Deps.androidCore)
    api(Deps.appCompat)
    api(Deps.materialDesign)
}