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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += listOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
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

    hilt {
        enableExperimentalClasspathAggregation = true
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":omni-sdk"))

    implementation(project(":bliss-auth"))
    implementation(project(":ijournall-database"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Core library
    testImplementation("androidx.test:core:1.4.0")

    // AndroidJUnitRunner and JUnit Rules
    testImplementation("androidx.test:runner:1.4.0")
    testImplementation("androidx.test:rules:1.4.0")

    // Assertions
    testImplementation("androidx.test.ext:truth:1.4.0")

    //DI
    implementation(Deps.hilt)
    kapt(Deps.hiltCompiler)
    implementation(Deps.hiltNavigation)

    // Navigation
    implementation(Deps.navigationFragment)
    implementation(Deps.navigationUi)

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.preference:preference-ktx:1.2.0")

    //ViewModel
    implementation(Deps.lifecycleViewModel)
    // LiveData
    implementation(Deps.lifecycleLiveData)

    // fragment
    implementation("androidx.fragment:fragment-ktx:1.5.2")
    //glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.10.0")

    //room
    implementation(Deps.room)
    implementation(Deps.roomKtx)
    kapt(Deps.roomCompiler)

    //viewpager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    //permissions
    implementation("pub.devrel:easypermissions:3.0.0")
    //kotlin
    val kotlin_version = "1.7.0"
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    //leakcanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.7")
    //anr watchdog
    implementation("com.github.anrwatchdog:anrwatchdog:1.4.0")

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.11.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")

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
