# GradleDependOnRelocatedShadow
- This repository was created for anyone who wanted to use a gradle dependency on another submodule, but couldn't seem to import relocated shadowJar packages
- This project has 2 modules:
  - `module-a` which is meant to represent your main project
  - `module-b` which is an api project, with shadowJar and relocation
- `module-b` uses `com.github.johnrengelman.shadow` to shade `org.yaml:snakeyaml` while relocating that dependency
  - It produces its own jar file (shaded and relocated)
  - It has a `unpackShadow` task that produces an unpacked folder within `build`. These files are used as the dependency for `module-a`

This is by far not the most clean solution, but after a couple of days of struggling to get something to work, this is the best I could come up with.  

**NOTE:** If the project fails to build, refresh gradle in IntelliJ, which will trigger `idea-ext` to re-compile the `unpacked-shadow` folder

## Simpler Solution
Note:
- The solution above was created to solve an issue when using abstract classes from `module-b`
  - Those classes were being directly imported from the submodule, rather than the relocated package
  - As a result, IntelliJ would expect you to provide `org.yaml...` objects, instead of the relocated objects
- If you just need to import the relocations, you can use the following dependency format instead (and remove the unpack task)
```kotlin
implementation(project(":module-b")) {
    attributes {
        attribute(BUNDLING_ATTRIBUTE, objects.named<Bundling>(SHADOWED))
    }
}
```
If you use the above implementation, you can access the relocated dependencies, but classes from `module-b` will be imported directly from the submodule  
This appears to be a bug in IntelliJ where the `module-a.main` module in project structure loads `module-b` as a dependency, with higher priority than the relocated jar
- As a result, IntelliJ uses the raw class files from `module-b`, and then adds the relocated dependencies (snakeyaml)


## Information
For those wondering, this appears to be an issue with IntelliJ and Gradle   
https://youtrack.jetbrains.com/issue/IDEA-163411/Gradle-integration-is-broken-when-using-the-Shadow-Plugin   
IntelliJ recently released an update that seems to fix a similar issue with Maven  
[Support for the Maven Shade Plugin’s renaming workflow](https://www.jetbrains.com/idea/whatsnew/2024-1/#page__content-build-tools)  
Hopefully there's a fix coming soon for Gradle and the shadow plugin
