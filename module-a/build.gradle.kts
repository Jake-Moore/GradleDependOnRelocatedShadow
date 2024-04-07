plugins {
    // module-a specific plugins
}

dependencies {
    // I hate this, but IntelliJ's gradle integration won't allow me to depend on the relocated module-b jar
    implementation(files(project(":module-b")
        .dependencyProject.layout.buildDirectory.dir("unpacked-shadow"))
    )
}

tasks {
    jar {

    }
}