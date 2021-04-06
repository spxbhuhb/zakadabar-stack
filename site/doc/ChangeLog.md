# 2021.4.6

## Breaking changes

**Sort account related DTOs into directories**

Move the following classes into `zakadabar.stack.data.builtin.account`

* AccountPublicDto
* LoginAction
* LogoutAction
* PasswordChangeAction
* PrinicipalDto
* RoleDto
* RoleGrantDto
* RoleGrantsByPrincipal
* SessionDto

Move the following classes into `zakadabar.stack.data.builtin.misc`

* Secret

**Action and query DTO coding pattern**

Before:

```kotlin
override suspend fun execute() = comm().action(this, serializer(), ActionStatusDto.serializer())

companion object : ActionDtoCompanion<ActionStatusDto>()
```

After:

```kotlin
override suspend fun execute() = comm.action(this, serializer(), ActionStatusDto.serializer())

companion object : ActionDtoCompanion<ActionStatusDto>(SessionDto.recordType)
```

Infrastructure:

* Kotlin 1.4.31
* Kotlin Serialization 1.1.0
* Kotlin Coroutines 1.4.3
* Ktor 1.5.3

Feature

* Docker configuration for demo (postgres + backend)
* Documentation improvements
* StringsByLocale DTO and backend - i18n
* SettingString DTO and backend
* StringStore refactor
* Separate record, action and query comms
* Add table sort, search, export, column resize
* `site` subproject - documentation site for the stack
* `lib:markdown` subproject - Markdown file display for browser frontend