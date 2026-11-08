plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.portfolio.chitchat"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.portfolio.chitchat"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.navigation.compose)
    // Android Lifecycle Compose library
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation("androidx.compose.runtime:runtime:1.12.1")


    implementation(libs.androidx.compose.material.icons.extended)

    implementation("androidx.navigation:navigation-compose:2.10.2")

    // Firebase Framework Core Components
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")        // Phone SMS Auth Registry
    implementation("com.google.firebase:firebase-firestore")   // Real-Time Chat Channels
    implementation("com.google.firebase:firebase-storage")     // Media & Profile Images
    implementation("com.google.firebase:firebase-messaging-ktx")


    // Coroutines core library jismein Flows aur StateFlow hote hain
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

    // Agar aap Android dev kar rahe hain, toh isko bhi add karein
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")

//    implementation("com.google.accompanist:accompanist-permissions:0.37.3")


}