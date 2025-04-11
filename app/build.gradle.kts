plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "android.example.bobo"
    compileSdk = 35

    defaultConfig {
        applicationId = "android.example.bobo"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.runtime)
    implementation(libs.firebase.inappmessaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.google.android.material:material:1.9.0") // Đảm bảo sử dụng phiên bản mới nhất
    implementation ("androidx.navigation:navigation-fragment:2.7.6")
    implementation ("androidx.navigation:navigation-ui:2.7.6")

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Material Components
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
}
