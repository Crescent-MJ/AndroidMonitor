plugins {
    id("com.android.application")
    id ("kotlin-android")
    id("monitor-plugin")
}

android {
    namespace = "com.example.wifidetection"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wifidetection"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("com.squareup.okio:okio:2.10.0")

    val alsVersion = "0.0.1"
    implementation ("io.github.lygttpod.android-local-service:core:" + alsVersion)
    implementation ("io.github.lygttpod.android-local-service:annotation:" + alsVersion)
    annotationProcessor ("io.github.lygttpod.android-local-service:processor:" + alsVersion)

    //debugImplementation ("io.github.lygttpod:monitor:0.1.2")
    //debugImplementation (project(":monitor"))
}