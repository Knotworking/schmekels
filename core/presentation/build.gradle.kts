plugins {
    id("schmekels.android.library")
    id("schmekels.compose")
}

android {
    namespace = "com.knotworking.schmekels.core.presentation"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.lifecycle.runtime.compose)
}
