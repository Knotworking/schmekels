plugins {
    id("android.library")
    id("compose")
}

android {
    namespace = "com.knotworking.schmekels.core.presentation"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.lifecycle.runtime.compose)
}
