import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            dependencies {
                add("implementation", libsCatalog.findLibrary("koin-core").get())
                add("implementation", libsCatalog.findLibrary("koin-android").get())
                add("implementation", libsCatalog.findLibrary("koin-androidx-compose").get())
            }
        }
    }
}
