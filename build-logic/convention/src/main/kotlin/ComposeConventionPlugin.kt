import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.getByType<CommonExtension>().apply {
                buildFeatures.compose = true
            }

            dependencies {
                add("implementation", platform(libsCatalog.findLibrary("compose-bom").get()))
                add("implementation", libsCatalog.findLibrary("compose-ui").get())
                add("implementation", libsCatalog.findLibrary("compose-ui-graphics").get())
                add("implementation", libsCatalog.findLibrary("compose-ui-tooling-preview").get())
                add("implementation", libsCatalog.findLibrary("compose-material3").get())
                add("implementation", libsCatalog.findLibrary("compose-material-icons").get())
                add("debugImplementation", libsCatalog.findLibrary("compose-ui-tooling").get())
            }
        }
    }
}
