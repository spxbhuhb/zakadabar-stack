# Zakadabar

[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.zakadabar/core)](https://mvnrepository.com/artifact/hu.simplexion.zakadabar/core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
![Kotlin](https://img.shields.io/github/languages/top/spxbhuhb/zakadabar-stack)

Zakadabar is a multiplatform application development library.

## Status

**IMPORTANT** 

I plan to introduce breaking changes in the next few months. As far as I know, no-one else uses the
project in production besides us, so this should not cause any problems.

If it is a problem for you, let me know, and I'll try to take that into account.

## News

I've shelved the declarative and reactive ui development for now as workload is high on other areas.
I want to completely abandon the project, but I won't be able to work on it in the first half of 
the year for sure.

What I really would like to do, and it seems to be not that hard is to bring in the Material 3 design.
SoftUI is not really far from it, so it should be easy. Only if I would have a reactive UI. :D

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
                implementation("hu.simplexion.zakadabar:core:<version>")
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
