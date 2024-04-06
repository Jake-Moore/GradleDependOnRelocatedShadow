import org.gradle.api.attributes.Bundling.*

plugins {
    // module-a specific plugins
}

dependencies {
    // implementation(project(":module-b"))

    implementation(project(":module-b")) {
        attributes {
            attribute(BUNDLING_ATTRIBUTE, objects.named<Bundling>(SHADOWED))
        }
    }
}