plugins {
    id("schmekels.android.library")
    id("schmekels.ktor")
    id("schmekels.koin")
    id("schmekels.room")
    id("schmekels.kotlinx.serialization")
    id("schmekels.testing")
}

android {
    namespace = "com.knotworking.schmekels.feature.converter.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":feature:converter:domain"))
    implementation(libs.datastore.preferences)
}
