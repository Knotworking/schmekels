import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("com.google.devtools.ksp")
            dependencies {
                add("implementation", libsCatalog.findLibrary("room-runtime").get())
                add("implementation", libsCatalog.findLibrary("room-ktx").get())
                add("ksp", libsCatalog.findLibrary("room-compiler").get())
            }
        }
    }
}
