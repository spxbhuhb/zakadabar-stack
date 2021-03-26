# Zakadabar

Full-stack development platform implemented as a Kotlin Multiplatform project.

Goal of the stack is to implement small business applications very fast without re-doing the same programming task again
and again.

Project status is **alpha testing**.

**Links below are mostly broken at the moment.**

## Getting Started

1. [Full-Stack in 15 minutes](https://zakadabar.com/CodeLab) - code lab
1. [Concepts](https://zakadabar.com/concepts.pdf) - PDF slide show
1. [Gotchas](https://zakadabar.com/gotchas) - common gotchas, avoid frustration!
1. [Cheat Sheet](https://zakadabar.com/CheatSeat) - for copy-paste knights
1. [Demo](https://zakadabar.com/Demo) and the [source code](demo/demo) of the demo
1. [Documentation](https://zakadabar.com/Documentation)

## Gradle

* Add the Maven Central repository if it is not already there:
```kotlin
repositories {
    mavenCentral()
}
```

* Add a dependency to the commonMain source set dependencies:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("hu.simplexion.zakadabar:core:2021.3.26")
            }
        }
    }
}
```

## License

> Copyright (c) 2020 Simplexion Kft, Hungary and contributors
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.

## Credits

[Credits](doc/misc/credits.md) to the projects we built on.