plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("D:\\Code\\Native\\Instagram\\app\\build\\jks\\Instagram.jks")
            storePassword = "123456"
            keyAlias = "kay0"
            keyPassword = "123456"
        }
    }
    namespace = "com.example.instagram_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.instagram_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation ("androidx.navigation:navigation-compose:2.7.7")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.51")
    kapt ("com.google.dagger:hilt-android-compiler:2.51")
    kapt ("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Coroutines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
//
    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    //firebase
//    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
//    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
//    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")

    implementation ("com.google.firebase:firebase-auth:22.3.1")
    implementation (platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation ("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.10.3")
    implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")

    // Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//
    // OkHttp
//    implementation("com.squareup.okhttp3:okhttp:4.10.0")
//
    // JSON Converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//
    //preferences
//    implementation("androidx.datastore:datastore-preferences:1.1.0-beta01")

    //swipe refresh
    //  implementation("com.google.accompanist:accompanist-swiperefresh:0.25.1")

    //implementation("androidx.appcompat:appcompat:1.6.1")
}
kapt {
    correctErrorTypes = true
}