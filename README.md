# Zakadabar

[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.zakadabar/core)](https://mvnrepository.com/artifact/hu.simplexion.zakadabar/core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
![Kotlin](https://img.shields.io/github/languages/top/spxbhuhb/zakadabar-stack)

A Kotlin Multiplatform library for full-stack software development.

Goal is to implement small business applications very fast without re-doing the same programming task again and again.

Project status is **alpha**,
see [components stability](https://kotlinlang.org/docs/reference/evolution/components-stability.html).

## Getting Started

1. [Application Template](https://github.com/spxbhuhb/zakadabar-application-template)
1. [Documentation](site/content) - this is work in progress, improves each day
1. [Source code](demo/demo) of the demo project.
1. [Source code](demo/demo-lib) of the demo library project.
1. [Source code](site) of the site project.

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
                implementation("hu.simplexion.zakadabar:core:2021.4.27")
            }
        }
    }
}
```

## License

> Copyright (c) 2020-2021 Simplexion Kft, Hungary and contributors
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
