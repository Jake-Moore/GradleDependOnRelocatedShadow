plugins {
    id("com.github.johnrengelman.shadow")
    id("maven-publish")
}

var intelliJ = project.property("intellijDep") as String
dependencies {
    implementation("org.yaml:snakeyaml:2.2")

    // IntelliJ annotations
    implementation(intelliJ)
}

tasks {
    build.get().dependsOn("shadowJar")
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