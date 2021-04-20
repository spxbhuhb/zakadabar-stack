# 2021.4.6

## Breaking changes

**Move app into template directory**

Demo `app` directory moved into `template\app`. The server loads configuration from `template\app` instead of `app`.

**Move account related DTOs into data.builtin.account**

Move the following classes into `zakadabar.stack.data.builtin.account`

* AccountPublicDto
* LoginAction
* LogoutAction
* PasswordChangeAction
* PrincipalDto
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

## Features

* Docker configuration for demo (postgres + backend)
* Documentation improvements
* StringsByLocale DTO and backend - i18n
* SettingString DTO and backend
* StringStore refactor
* Separate record, action and query comms
* Add table sort, search, export, column resize
* `site` subproject - documentation site for the stack
* `lib:markdown` subproject - Markdown file display for browser frontend
