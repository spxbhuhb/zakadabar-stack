# Zakadabar

[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.zakadabar/core)](https://mvnrepository.com/artifact/hu.simplexion.zakadabar/core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
![Kotlin](https://img.shields.io/github/languages/top/spxbhuhb/zakadabar-stack)

Zakadabar is a multiplatform application development library.

## Status

As of 2021.11.15 I removed the alpha marker from the library as I feel we've
reached a point when we do not add structural changes often.

There are a missing pieces (i.e. rich text editor, date picker), and a few areas
could be made better with some cleanup. However, I don't really plan to work 
on these in the near future.

The next step will be a rather significant change: adding our own compiler plugin
to support declarative UI building with proper state management, automatic re-render
on state changes and transform function optimization.

I plan to keep the current UI as is, adding bugfixes and then create the same
functionality with a declarative UI that is able to refresh on state changes
(same as in React, Compose, Flutter).

I also plan to keep most of the concepts from the current UI, the code
will look like quite similar, but elements will re-render on state changes.

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
                implementation("hu.simplexion.zakadabar:core:2021.12.3")
            }
        }
    }
}
```

## License

> Copyright (c) 2020-2021 Simplexion Kft, Hungary and contributors
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
