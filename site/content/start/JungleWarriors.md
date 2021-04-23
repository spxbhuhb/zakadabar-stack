```kotlin
repositories {
    mavenCentral()
}
```

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("hu.simplexion.zakadabar:core:$stackVersion")
            }
        }
    }
}
```