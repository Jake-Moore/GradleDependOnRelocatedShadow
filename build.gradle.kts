plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
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