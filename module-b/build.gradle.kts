plugins {
    id("com.github.johnrengelman.shadow")
    id("maven-publish")

    // Required since we are using idea-ext afterSync
    id("java")
    id("java-library")
}

// var intelliJ = project.property("intellijDep") as String
dependencies {
    implementation("org.yaml:snakeyaml:2.2")

    // IntelliJ annotations
    implementation("org.jetbrains:annotations:24.1.0") // Required since we are using idea-ext afterSync
}

tasks {
    build {
        dependsOn("shadowJar")
    }
    // build.get().dependsOn("shadowJar")
    shadowJar {
        archiveClassifier.set("")
        relocate("org.yaml.snakeyaml", "com.kamikazejam.kamicommon.snakeyaml")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = rootProject.version.toString()

            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://nexus.luxiouslabs.net/public")
            credentials {
                username = System.getenv("LUXIOUS_NEXUS_USER")
                password = System.getenv("LUXIOUS_NEXUS_PASS")
            }
        }
    }
}

tasks.register<Copy>("unpackShadow") {
    dependsOn(tasks.shadowJar)
    from(zipTree(layout.buildDirectory.dir("libs").map { it.file(tasks.shadowJar.get().archiveFileName) }))
    into(layout.buildDirectory.dir("unpacked-shadow"))
}
tasks.getByName("assemble").finalizedBy(tasks.getByName("unpackShadow"))