import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.gms.google.services)
    id ("kotlin-parcelize")
}

android {
    namespace = "com.hiendao.eduschedule"
    compileSdk = libs.versions.compileSdk.get().toInt()

    val file = rootProject.file("local.properties")
    val properties = Properties()
    properties.load(FileInputStream(file))

    defaultConfig {
        applicationId = "com.hiendao.eduschedule"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        buildConfigField("String", "SERVER_CLIENT_ID", properties.getProperty("server_client_id"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resourceConfigurations += listOf("en", "vi")
    }

    androidResources {
        generateLocaleConfig = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        jniLibs {
            excludes += arrayOf("META-INF/groovy/**", "zoneinfo-global/**")
        }
        resources {
            excludes += arrayOf(
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt",
                "**/package-info.java",
                "META-INF/groovy-release-info.properties",
                "META-INF/INDEX.LIST",
                "META-INF/groovy/**",
                "zoneinfo-global/**",
                "org/apache/commons/codec/language/bm/*.txt"
            )
        }
    }
}

dependencies {
    //core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Extended Icons
    implementation(libs.androidx.material3.icons)
    //Timber
    implementation (libs.timber)
    //Coil
    implementation(libs.coil)
    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    //Retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.moshi)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.interceptor)
    //Lifecycle viewmodel
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    //Room
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)
    //Navigation Compose
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    //Work Manager
    implementation(libs.androidx.work)
    implementation(libs.androidx.hilt.work)
    //WebView
    implementation(libs.webview.kevinnzou)
    //Ical4j -> Parse ics file
    implementation("org.mnode.ical4j:ical4j:2.2.7")
    implementation("uk.uuid.slf4j:slf4j-android:2.0.9-0")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
    implementation("org.slf4j:slf4j-simple:2.0.0-alpha1")
    implementation("javax.cache:cache-api:1.1.1")
    // retrofit - gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.12.1")
    //Sign-in w Google
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    // pull to refresh
//    implementation("androidx.compose.material3:material3-pulltorefresh:1.0.0-rc01") // Add the pulltorefresh lib
    implementation(libs.pullrefresh)
}