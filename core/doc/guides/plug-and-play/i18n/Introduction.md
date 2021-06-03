# Plug and Play: I18N

I18N is a simple internationalization module to provide translations for different locales.

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Changing Locales and Translations">

The idea is that the site administrator can create/change locales and translations freely, during runtime, without
modifying the code of the application. The `Locales` and `Translations` pages on the browser frontend provide this
management function.
</div>

## Setup

To use i18n in your application:

1. add the gradle dependency,
1. add the module to your server configuration, for details see [Modules](../../backend/Modules.md),
1. add the service to your application, see [Introduction: Browser](../../browser/Introduction.md)   
1. add the routing to your frontend, for details see [Routing](../../browser/structure/Routing.md)
1. add the navigation to your side to open the pages, see [SideBar](../../browser/builtin/SideBar.md)

**gradle**

```kotlin
implementation("hu.simplexion.zakadabar:i18n:$i18nVersion")
```

**backend**

```kotlin
zakadabar.lib.i18n.backend.install()
```

**frontend: application**

```kotlin
zakadabar.lib.i18n.frontend.install(application)
```

**frontend: routing**

```kotlin
zakadabar.lib.i18n.frontend.install(this)
```

**frontend: navigation** (for sidebar)

```kotlin
withRole(StackRoles.siteAdmin) {
    + item<Locales>()
    + item<Translations>()
}
```

## Database

The module uses SQL for data persistence. At first run it creates these SQL
objects automatically.

| Table | Content |
| --- | --- |
| `locale` | Locales known by the system. |
| `translation` | Translations known by the system. |