// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.application.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.spotless) apply false
}


subprojects {
    apply {
        plugin(rootProject.libs.plugins.spotless.get().pluginId)
    }

    configure<SpotlessExtension> {
        // Configuration for Java files
        java {
            target("**/*.java")
            googleJavaFormat().aosp() // Use Android Open Source Project style
            removeUnusedImports() // Automatically remove unused imports
            trimTrailingWhitespace() // Remove trailing whitespace
        }

        // Configuration for Kotlin files
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt") // Exclude files in the build directory
            ktlint(libs.versions.klint.get()).setEditorConfigPath(rootProject.file(".editorconfig").path) // Use ktlint with version 1.2.1 and custom .editorconfig
            toggleOffOn() // Allow toggling Spotless off and on within code files using comments
            trimTrailingWhitespace()
        }

        // Additional configuration for Kotlin Gradle scripts
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(libs.versions.klint.get()) // Apply ktlint to Gradle Kotlin scripts
        }
    }
}