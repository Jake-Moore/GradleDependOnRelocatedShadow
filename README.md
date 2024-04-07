# GradleDependOnRelocatedShadow
- This repository was created for anyone who wants to use a gradle dependency on another submodule, but faced issues importing relocated shadowJar packages
- This project has 2 modules:
  - `module-a` which is meant to represent your main project
  - `module-b` which is an api project, with shadowJar and relocation
- `module-b` uses `com.github.johnrengelman.shadow` to shade `org.yaml:snakeyaml` while relocating it (sometimes necessary)
  - It produces its own jar file (shaded and relocated)

## Solution 1
- This solution is the simplest and perhaps the cleanest way to make relocated shaded dependencies available
- **Catch:** abstract classes using relocated dependency objects will error in IntelliJ, and request the wrong package for those objects
  - If you don't have abstract classes, or don't face issues, this solution may work for you
- You can use the following dependency format (and remove the module-b `unpackShadow` task)
```kotlin
implementation(project(":module-b")) {
    attributes {
        attribute(BUNDLING_ATTRIBUTE, objects.named<Bundling>(SHADOWED))
    }
}
```
## Solution 2
- This is the only solution I found to make IntelliJ resolve the correct packages for abstract classes using relocated objects
- This is by far not the most clean solution, but after a couple of days of struggling to get something to work, this is the best I could come up with.
- **NOTE:** If the project fails to build using this solution, refresh gradle in IntelliJ
  - This will trigger the `idea-ext` plugin to re-compile the `unpacked-shadow` folder
#### 1. Add the `unpackShadow` task to `module-b`
```kotlin
tasks.register<Copy>("unpackShadow") {
    dependsOn(tasks.shadowJar)
    from(zipTree(layout.buildDirectory.dir("libs").map { it.file(tasks.shadowJar.get().archiveFileName) }))
    into(layout.buildDirectory.dir("unpacked-shadow"))
}
tasks.getByName("build").finalizedBy(tasks.getByName("unpackShadow"))
```
#### 2. Add a modified `module-b` dependency to `module-a`
```kotlin
implementation(files(project(":module-b")
    .dependencyProject.layout.buildDirectory.dir("unpacked-shadow"))
)
```


## Information
For those wondering, this appears to be an issue with IntelliJ and Gradle   
https://youtrack.jetbrains.com/issue/IDEA-163411/Gradle-integration-is-broken-when-using-the-Shadow-Plugin   
IntelliJ recently released an update that seems to fix a similar issue with Maven  
[Support for the Maven Shade Plugin’s renaming workflow](https://www.jetbrains.com/idea/whatsnew/2024-1/#page__content-build-tools)  
Hopefully there's a fix coming soon for Gradle and the shadow plugin
