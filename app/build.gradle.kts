plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.sw03_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sw03_app"
        minSdk = 27
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
    buildToolsVersion = "33.0.1"
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.kakao.sdk:v2-all:2.17.0")
    implementation("com.kakao.sdk:v2-user:2.17.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}