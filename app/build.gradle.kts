plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt") // Agrega esto si no está presente
}

android {
    namespace = "com.fergodev.savebiyuyos"
    compileSdk = 34

    buildFeatures {
        dataBinding = true
    }

    viewBinding {
        enable = true
    }

    defaultConfig {
        applicationId = "com.fergodev.savebiyuyos"
        minSdk = 26
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
    packaging {
        resources {
            excludes += "META-INF/AL2.0"
            excludes += "xsd/catalog.xml"
            excludes += "META-INF/LGPL2.1"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/io.netty.versions.properties"
            excludes += "META-INF/INDEX.LIST"
        }
    }

}

configurations.all {
    exclude(group="com.intellij", module = "annotations")
    exclude (group= "com.sun.activation", module = "javax.activation")
}

dependencies {

    implementation(libs.androidx.room.runtime)
    implementation(libs.gradle)
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
    implementation("net.java.dev.jna:jna:5.6.0") {
        exclude (group = "net.java.dev.jna", module = "jna-platform")
    }
    implementation("com.android.tools.ddms:ddmlib:31.7.2") {
        exclude (group = "com.android.tools.ddms")
    }

}