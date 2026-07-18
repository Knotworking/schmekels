plugins {
    id("domain.module")
    id("testing")
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.kotlinx.coroutines.core)
}
