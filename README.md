# Zakadabar

[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.zakadabar/core)](https://mvnrepository.com/artifact/hu.simplexion.zakadabar/core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
![Kotlin](https://img.shields.io/github/languages/top/spxbhuhb/zakadabar-stack)

Zakadabar is a multiplatform application development library.

## Status

We use software built on Zakadabar in production, with happy customers. It is not perfect nor complete,
there are areas in need of refactoring, some others are a bit confused. That said, it works, we've been
able to ship quality software based on it.

It is under continuous development, we add features as needed for our projects. If you need something
feel free to open an issue.

## News

Development of the current functionality is a bit slow as we work on the next big step: a declarative
and reactive UI inspired by Svelte.

This is a bit complex as it involves writing a Kotlin compiler plugin. We are almost there (actually,
the plugin works, we just have to finish it). If you would like to have a preview you can check the `rui`
branch, there are some preliminary docs and of course the source code of the plugin with a few tests.

This change won't affect the current UI much. You will be able to use it as before and mix it with the
new one.

We would like to release production ready version of the new UI, called Rui, by the end of the year.

## Upcoming

Apart Rui, we plan to add a fancy new function to the website: a breaking change database.
This will let you list and export the breaking changes between two distant versions, so you will
be able to upgrade to a new version easily.

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
