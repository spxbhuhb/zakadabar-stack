# Next Release

## Features

* Common title bar concept. Added to pages, form and table.
* New landing page for site.
* Zakadabar Design colors to ZkColors.

## Changes

### ZkApplication

* property `title`, stores the data for top application title bar
* property `onTitleChange`, callback, executed when `title` changes

### ZkDefaultLayout

* Implements the app-handle/sidebar/title-bar/content layout from the demo.
* ZkPageTitle class to store application title bar data.
* ZkAppHandle class to display application name and menu close button.
* ZkAppTitleBar class to display the application title bar.

### ZkArgPage

* Use `ZkApplication.title`.
* Refresh title bar during onResume.

### ZkArgPage

* Use `ZkApplication.title`.
* Refresh title bar during onResume.

### ZkForm

* Use `ZkApplication.title`.
* Refresh title bar during onResume.
* `onConfigure` method to replace `init`

### ZkTable

* Use `ZkApplication.title`.
* Refresh title bar during onResume.
* Actions moved into the `actions` package:
  * ZkAddRowAction
  * ZkExportCsvAction
  * ZkSearchAction

### ZkElement

* New convenience functions:
  * `gridRow` to set CSS grid row placement,
  * `gridColumn` to set CSS grid column placement,
  * `display` to set CSS display property.

### ZkButton

* replace `className=` with `classList +=`.

### ZkIconButton

* `open` class.
* Replace `className=` with `classList +=`.
* Function `onMouseClick`, calls `onClick` by default.

## Deprecated

* `ZkSideBarTitle` - replaced by ZkAppHandle
* `ZkSideBar.title` - replaced by appHandle in ZkDefaultLayout

## Removed

* `ZkForm.titleBar`
* `ZkTableTitleBar`

# 2021.4.13

## Bugfixes

* fixed RoleGrantByPrincipal query use of wrong backend

# 2021.4.12

## Features

* table virtualization
* markdown improvements
* bugfixes
* site and documentation improvements

# 2021.4.8

## Features

* spxbhuhb/zakadabar#16 UUID type in the Dto schema, form, and table
* builtin backend to demo to show handling of all data types
* builtin crud frontend to demo

# 2021.4.7

## Features

* Optional double and int fields for ZkForm
* `appDistZip` gradle task of Demo and Site adds version number to the generated JavaScript file name (also updates
  index.html)

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
