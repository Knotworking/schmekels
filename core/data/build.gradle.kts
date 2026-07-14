plugins {
    id("schmekels.android.library")
    id("schmekels.ktor")
}

android {
    namespace = "com.knotworking.schmekels.core.data"

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://open.er-api.com/v6\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:domain"))
}
