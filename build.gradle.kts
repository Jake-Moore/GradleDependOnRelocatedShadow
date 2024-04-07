import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.taskTriggers

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
}

idea.project.settings {
    taskTriggers {
        afterSync(tasks.getByPath(":module-b:clean"), tasks.getByPath(":module-b:build"))
    }
}

// Disable root project build
tasks.jar.get().enabled = false

allprojects {
    group = "com.kamikazejam.kamicommon"
    version = "3.0.0.0-test-SNAPSHOT"
    description = "KamikazeJAM's common library for Spigot and Standalone projects."

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
}

ext {
    set("intellijDep", "org.jetbrains:annotations:24.1.0")
}