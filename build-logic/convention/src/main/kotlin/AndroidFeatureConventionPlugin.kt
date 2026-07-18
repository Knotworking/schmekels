import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("android.library")
                apply("compose")
                apply("koin")
            }
            dependencies {
                add("implementation", libsCatalog.findLibrary("kotlinx-coroutines-android").get())
            }
        }
    }
}
