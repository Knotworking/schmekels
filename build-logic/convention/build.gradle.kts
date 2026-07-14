plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "android-application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "android-library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "android-feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("domainModule") {
            id = "domain-module"
            implementationClass = "DomainModuleConventionPlugin"
        }
        register("compose") {
            id = "compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("koin") {
            id = "koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("ktor") {
            id = "ktor"
            implementationClass = "KtorConventionPlugin"
        }
        register("room") {
            id = "room"
            implementationClass = "RoomConventionPlugin"
        }
        register("kotlinxSerialization") {
            id = "kotlinx-serialization"
            implementationClass = "KotlinxSerializationConventionPlugin"
        }
    }
}
