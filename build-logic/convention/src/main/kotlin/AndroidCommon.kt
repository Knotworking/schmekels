import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

internal fun CommonExtension.configureAndroidCommon() {
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
    defaultConfig.minSdk = 33
    compileOptions.sourceCompatibility = JavaVersion.VERSION_11
    compileOptions.targetCompatibility = JavaVersion.VERSION_11
}
