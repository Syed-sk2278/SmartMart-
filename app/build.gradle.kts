plugins {
    alias(libs.plugins.android.application)
}

android {

    namespace = "com.example.smartmartplus"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.smartmartplus"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }
}

dependencies {

    // Android Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity.ktx)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson Converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Logging Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Unit Testing
    testImplementation(libs.junit)

    // Android Testing
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}