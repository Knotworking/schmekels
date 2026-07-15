plugins {
    id("schmekels.android.feature")
    id("schmekels.kotlinx.serialization")
    id("schmekels.testing")
}

android {
    namespace = "com.knotworking.schmekels.feature.converter.presentation"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:presentation"))
    implementation(project(":core:design-system"))
    implementation(project(":feature:converter:domain"))
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.navigation.compose)
}
