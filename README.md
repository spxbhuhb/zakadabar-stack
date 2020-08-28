# Zakadabar Stack

Zakadabar Stack is a set of intertwined tools and habits which make building full-stack
applications fast, easy and fun.

Our aim is to have solutions for the everyday tasks we do as developers: build
a frontend, upload a file, have a server, release an appliance etc. 

As of now the stack is **in-development**. Things might change, it will need
some time to mature. We actively use it in our own projects, and we predict that
it will be production ready in general by the end of the year.

## Highlights

* Basics: Kotlin, Kotlinx, Ktor
* Keywords: multi-platform, coroutines, operator overloading, builders
* Dependencies: Kotlin(x), Ktor, HikariCP, Exposed, Clikt, Kaml, LogBack (yes, that's all of them :D )
* Database: anything supported by Exposed, we use PostgreSQL
* Development tools: IntelliJ IDEA, Gradle, Firefox
* Documentation:
  * [The Cookbook](doc/cookbook/README.md)
  * [Knowledge Base](doc/knowledge-base/README.md)
  * [Developers Guide](doc/developers-guide/README.md)
  * [Productivity Tips](doc/misc/Productivity.md)
  * [Espresso Lessons](doc/espresso-lessons/README.md)
  * API as soon as Dokka doesn't crash (yay 1.4)

# Quick Start
 
* clone the [Zakadabar Samples](https://github.com/spxbhuhb/zakadabar-samples) repository
* choose a sample application to start with
* follow the instructions from the README.md file

## Credits and Dependency Licenses

[Credits](doc/misc/credits.md) to the projects we built on.

## Releases and Gradle dependency

We release only Kotlin 1.4+ multiplatform packages.

During 2020 we publish a new release every Monday. They are as-is, just a build of that Monday morning. We use [continuous version numbers](./doc/misc/ContinuousVersions.md).

Packages of Zakadabar Stack are available from GitHub Packages. To reference them
from your project use the following Gradle setup.

**It might be a better to use the [Zakadabar Template](https://github.com/spxbhuhb/zakadabar-template) or
[Zakadabar Samples](https://github.com/spxbhuhb/zakadabar-samples) to start.**

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/spxbhuhb/zakadabar-stack")
        metadataSources {
            gradleMetadata()
        }
    }
    mavenCentral()
    jcenter()
}

val stackVersion = "2020.8.26"

commonMain {
    dependencies {
       api("hu.simplexion.zakadabar:zakadabar-stack:$stackVersion")
    }
}
```

## Features and Statuses

* `-->` = Moving fast
* RW = Resource (i.e developer, designer, architect) wait
* stable = Mostly stable
* OK = stable + have documentation

| Feature | Status | Details |
| ------- | -------| --------|
| Documentation - [Quick Start](doc/quick-start/README.md) | RW | |
| Documentation - [The Cookbook](doc/cookbook/README.md) | --> | |
| Documentation - [Knowledge Base](doc/knowledge-base/README.md) | --> | |
| Documentation - [Developers Guide](doc/developers-guide/README.md) | --> | |
| Documentation - API | RW | Dokka throws StackOverflowException |
| Development - Appliance | RW | Gradle: one-click appliance build works, need others |
| [Accounts](doc/cookbook/common/Accounts.md) | --> | Very basic implementation works. |
| [Authentication](doc/developers-guide/Authentication.md) | RW | |
| Authorization | RW | Role based, ACL, basics coded |
| Sessions | RW | Have to write SQL backend |
| Routing - [Backend](doc/cookbook/backend/Routing.md) | OK | Ktor, module namespace |
| Routing - Frontend - Menu | RW | |
| Routing - Frontend - Entity Tree | --> | Navigation |
| Data - Common - Serialization | stable | Kotlinx.serialization, CommArray |
| Data - Common - Transfer | stable | Data Transfer Objects (DTOs) - in common code |
| Data - Common - [Entities](doc/developers-guide/Data.md) | OK | Entity Tree, revisions, snapshots |
| Data - Common - Files | stable | upload, download works both for REST and WebSocket |
| Data - Common - Validation | --> | |
| Data - Frontend - REST | stable | DTO, RestComm, CachedRestComm |
| Data - Frontend - WebSocket | stable | FrontendContext.transferSession |
| Data - Backend - DAO, Tables | stable | Exposed, PostgreSQL, HikariCP |
| Frontend - [Elements](doc/cookbook/frontend/Elements.md) | stable | easy and clean DOM handling |
| Frontend - Slots | stable | target slots for GUI elements |
| Frontend - Builtin - Desktop | stable | Desktop, Header, Footer |
| Frontend - Builtin - Navigator | --> | |
| Frontend - Builtin - Utils | --> | Slider, DropArea, [Icons], Header, Status |
| Frontend - Building - Inputs | --> | Input, SingleLineInput |
| Frontend - Components - RTE | --> | Zakadabar Editor |
| Frontend - Context | stable | FrontendContext |
| Frontend - Messaging | stable | Dispatcher |
| Frontend - Form | --> | migrate from previous React based solution | 
| Frontend - Tables | RW | migrate from previous React based solution |
| Frontend - Themes CSS | stable |  |
| Frontend - I18N | stable |  |
| Frontend - Notification | RW | |
| Frontend - Logging | RW |  |
| Frontend - Error Handling | RW | |
| Backend - Search | RW | Apache Lucene? |
| Backend - Document Generation | RW | Zakadabar Editor - Export to PDF/HTML |
| Backend - Notifications | RW | e-mail with templates |
| Backend - Archiving | RW | |
| Backend - Auditing | RW | |
| Backend - Backup and Restore | RW | |
| Backend - Resilience | RW | PostgreSQL DB mirroring |
| API - Command Line | --> | Zakadabar CLI a.k.a ZLI |

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
