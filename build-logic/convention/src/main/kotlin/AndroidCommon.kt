import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

internal fun CommonExtension.configureAndroidCommon() {
    compileSdk = ProjectConfig.compileSdk
    defaultConfig.minSdk = ProjectConfig.minSdk
    compileOptions.sourceCompatibility = ProjectConfig.javaVersion
    compileOptions.targetCompatibility = ProjectConfig.javaVersion
}
