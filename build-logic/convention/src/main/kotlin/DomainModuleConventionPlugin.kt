import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class DomainModuleConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")
            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(ProjectConfig.jvmToolChainJdkVersion)
            }
        }
    }
}
