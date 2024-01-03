import org.jetbrains.kotlin.kapt3.base.Kapt.kapt
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.hk.portfolio.today_weather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hk.portfolio.today_weather"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "KAKAO_API_KEY", "" + localProperties["KAKAO_API_KEY"])
            buildConfigField("String", "TOUR_API_KEY", "" + localProperties["TOUR_API_KEY"])
            buildConfigField("String", "TOUR_HOST", "" + localProperties["TOUR_HOST"])
            buildConfigField("String", "WEATHER_API_KEY", "" + localProperties["WEATHER_API_KEY"])
            buildConfigField("String", "WEATHER_HOST", "" + localProperties["WEATHER_HOST"])
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "KAKAO_API_KEY", "" + localProperties["KAKAO_API_KEY"])
            buildConfigField("String", "TOUR_API_KEY", "" + localProperties["TOUR_API_KEY"])
            buildConfigField("String", "TOUR_HOST", "" + localProperties["TOUR_HOST"])
            buildConfigField("String", "WEATHER_API_KEY", "" + localProperties["WEATHER_API_KEY"])
            buildConfigField("String", "WEATHER_HOST", "" + localProperties["WEATHER_HOST"])
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    // retrofit2에서 사용하는 json 직렬화 라이브러리
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // hiltViewModel
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    // Navigation For Compose
    val nav_version = "2.7.6"
    val material3_version = "1.1.2"
    val material_version = "1.5.4"

    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.compose.material3:material3:$material3_version")
    implementation("androidx.compose.material:material:$material_version")
    // retrofit2 로그
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")

    val paging_version = "3.2.1"

    implementation("androidx.paging:paging-runtime-ktx:$paging_version")

    // alternatively - without Android dependencies for tests
    testImplementation("androidx.paging:paging-common-ktx:$paging_version")

    // optional - Jetpack Compose integration
    implementation("androidx.paging:paging-compose:3.3.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0-rc02")

    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.33.2-alpha")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // image coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // This dependency is downloaded from the Google’s Maven repository.
    // So, make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:app-update:2.1.0")
    // For Kotlin users also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}