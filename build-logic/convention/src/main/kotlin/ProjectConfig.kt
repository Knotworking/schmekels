import org.gradle.api.JavaVersion

object ProjectConfig {
    const val appId = "com.knotworking.schmekels"
    const val minSdk = 33
    const val compileSdk = 36
    const val targetSdk = 36
    const val versionCode = 1
    const val versionName = "1.0"

    val javaVersion = JavaVersion.VERSION_11
    const val jvmToolChainJdkVersion = 11
}
