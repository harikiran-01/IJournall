
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    bundle {
        language {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }

    buildToolsVersion = ConfigData.buildToolsVersion

    defaultConfig {
        applicationId = "com.hk.ijournal"
        minSdk = ConfigData.minSdkVersion
        compileSdk = ConfigData.compileSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.appVersionCode
        versionName = ConfigData.appVersion
        resourceConfigurations.add("en")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

}

dependencies {
    //sdk
    implementation(project(":bliss-sdk"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Core library
    testImplementation("androidx.test:core:1.4.0")

    // AndroidJUnitRunner and JUnit Rules
    testImplementation("androidx.test:runner:1.4.0")
    testImplementation("androidx.test:rules:1.4.0")

    // Assertions
    testImplementation("androidx.test.ext:truth:1.4.0")

    //DI
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")

    // Navigation
    val nav_version = "2.4.2"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.preference:preference-ktx:1.2.0")

    val lifecycle_version = "2.5.1"

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // fragment
    implementation("androidx.fragment:fragment-ktx:1.5.2")
    //glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.10.0")
    //toasty
    implementation("com.github.GrenderG:Toasty:1.4.2")
    //viewpager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    //permissions
    implementation("pub.devrel:easypermissions:3.0.0")
    //kotlin
    val kotlin_version = "1.6.0"
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    //leakcanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")
    //anr watchdog
    implementation("com.github.anrwatchdog:anrwatchdog:1.4.0")
    //room
    val room_version = "2.4.2"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("com.wajahatkarim3:roomexplorer:0.0.2")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")

    //smiley rating
    implementation("com.github.sujithkanna:smileyrating:2.0.0")

    //ui
    implementation("com.airbnb.android:lottie:3.4.0")

    //calendar
    implementation("com.github.kizitonwose:CalendarView:1.0.4")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

repositories {
    mavenCentral()
}
