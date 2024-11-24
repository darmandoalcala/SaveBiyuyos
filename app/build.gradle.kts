plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt") // Agrega esto si no está presente
}

android {
    namespace = "com.fergodev.savebiyuyos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fergodev.savebiyuyos"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

configurations.all {
    exclude(group="com.intellij", module = "annotations")
}

dependencies {

    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler) // Correcto para KTS
    implementation(libs.androidx.room.ktx) // Extensiones opcionales
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}