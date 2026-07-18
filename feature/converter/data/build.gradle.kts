plugins {
    id("android.library")
    id("ktor")
    id("koin")
    id("room")
    id("kotlinx.serialization")
    id("testing")
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
