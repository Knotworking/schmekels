import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class TestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }
            dependencies {
                add("testImplementation", libsCatalog.findLibrary("junit5-api").get())
                add("testRuntimeOnly", libsCatalog.findLibrary("junit5-engine").get())
                add("testRuntimeOnly", libsCatalog.findLibrary("junit5-platform-launcher").get())
                add("testImplementation", libsCatalog.findLibrary("junit5-params").get())
                add("testImplementation", libsCatalog.findLibrary("turbine").get())
                add("testImplementation", libsCatalog.findLibrary("assertk").get())
                add("testImplementation", libsCatalog.findLibrary("kotlinx-coroutines-test").get())
            }
        }
    }
}
