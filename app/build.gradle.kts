plugins {
    id("android.application")
    id("compose")
    id("koin")
}

android {
    namespace = "com.knotworking.schmekels"

    defaultConfig {
        applicationId = "com.knotworking.schmekels"
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

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
