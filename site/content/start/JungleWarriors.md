If you like to put a parachute on and jump off the plane over the jungle to find your way back.

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

Have fun!