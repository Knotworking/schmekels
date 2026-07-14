import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", libsCatalog.findLibrary("ktor-client-core").get())
                add("implementation", libsCatalog.findLibrary("ktor-client-okhttp").get())
                add("implementation", libsCatalog.findLibrary("ktor-client-content-negotiation").get())
                add("implementation", libsCatalog.findLibrary("ktor-serialization-kotlinx-json").get())
                add("implementation", libsCatalog.findLibrary("ktor-client-logging").get())
            }
        }
    }
}
