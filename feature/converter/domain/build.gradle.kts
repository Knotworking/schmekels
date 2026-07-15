plugins {
    id("schmekels.domain.module")
    id("schmekels.testing")
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.kotlinx.coroutines.core)
}
