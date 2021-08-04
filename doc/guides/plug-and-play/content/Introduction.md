# Plug and Play: Content

Content is a simple content module to provide basic content management functions.

## Setup

To use content in your application:

1. add the gradle dependency,
1. add the module to your server configuration, for details see [Modules](../../backend/Modules.md)
1. provide authorization, see [Authorizer](/doc/guides/backend/Authorizer.md)   
1. add the service to your application, see [Introduction: Browser](../../browser/Introduction.md)   
1. add the routing to your frontend, for details see [Routing](../../browser/structure/Routing.md)
1. add the navigation to your side to open the pages, see [SideBar](../../browser/builtin/SideBar.md)

All content modules get authorizer from the authorization provider.

**gradle**

```kotlin
implementation("hu.simplexion.zakadabar:content:$contentVersion")
```

**backend**

```kotlin
zakadabar.lib.content.backend.install()
```

**frontend: application**

```kotlin
zakadabar.lib.content.frontend.browser.install(application)
```

**frontend: routing**

```kotlin
zakadabar.lib.content.frontend.browser.install(this)
```

**frontend: navigation** (for sidebar)

```kotlin
withRole(StackRoles.siteAdmin) {
    + item<ContentCrud>()
    + item<ContentCategoryCrud>()
    + item<ContentStatusCrud>()
}
```

## Database

The module uses SQL for data persistence. At first run it creates these SQL
objects automatically.

| Table | Content |
| --- | --- |
| `content` | Content entities. |
| `content_blob` | Binary data (content text, attachments, images, etc.)
| `content_category` | Content categories. |
| `content_status` | Content statuses. |