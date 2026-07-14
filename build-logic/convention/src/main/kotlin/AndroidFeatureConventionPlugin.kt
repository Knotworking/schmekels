import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("schmekels.android.library")
                apply("schmekels.compose")
                apply("schmekels.koin")
            }
            dependencies {
                add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())
            }
        }
    }
}
