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
            id = "schmekels.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "schmekels.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "schmekels.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("domainModule") {
            id = "schmekels.domain.module"
            implementationClass = "DomainModuleConventionPlugin"
        }
        register("compose") {
            id = "schmekels.compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("koin") {
            id = "schmekels.koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("ktor") {
            id = "schmekels.ktor"
            implementationClass = "KtorConventionPlugin"
        }
        register("room") {
            id = "schmekels.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("kotlinxSerialization") {
            id = "schmekels.kotlinx.serialization"
            implementationClass = "KotlinxSerializationConventionPlugin"
        }
        register("testing") {
            id = "schmekels.testing"
            implementationClass = "TestingConventionPlugin"
        }
    }
}
