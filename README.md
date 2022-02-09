# Zakadabar

[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.zakadabar/core)](https://mvnrepository.com/artifact/hu.simplexion.zakadabar/core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
![Kotlin](https://img.shields.io/github/languages/top/spxbhuhb/zakadabar-stack)

Zakadabar is a multiplatform application development library.

## News

2022.2.9 has some minor breaking changes, listed in the changelog.

During this month I plan to add enhancements to browser components. I'll try to
keep compatibility as much as possible, but most probably there are some
breaking changes coming.

----

2021.12.26 uses Kotlin 1.6.10 and Kotin/Js successfully compiles for IR. This 
gives us the possibility to add IR compiler plugins which will be the next step.

As of now the Kotlin/Js target is BOTH, I plan to switch this to IR only by end
of March.

## Documentation

Please visit [zakadabar.io](https://zakadabar.io).

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
                implementation("hu.simplexion.zakadabar:core:2022.2.9")
            }
        }
    }
}
```

## License

> Copyright (c) 2020-2022 Simplexion Kft, Hungary and contributors
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this work except in compliance with the License.
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

[Credits](/doc/misc/Credits.md) to the projects we built on.
