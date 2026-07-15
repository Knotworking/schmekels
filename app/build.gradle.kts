plugins {
    id("schmekels.android.application")
    id("schmekels.compose")
    id("schmekels.koin")
}

android {
    namespace = "com.knotworking.schmekels"

    defaultConfig {
        applicationId = "com.knotworking.schmekels"
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
}

dependencies {
    implementation(project(":core:design-system"))
    implementation(project(":feature:converter:data"))
    implementation(project(":feature:converter:presentation"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
