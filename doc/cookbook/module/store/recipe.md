# Modules Without Server

By default, the global module store `modules` stores application modules.

This is an instance of [ModuleStore](/core/core/src/commonMain/kotlin/zakadabar/stack/module/ModuleStore.kt)
and you can use it to manage, start, stop modules without having a server instance.

Use cases:

- Android applications
- standalone programs based on the stack
- browser site business logic implementations

## Add a Module

Adding a module to the module store is the same as with a server:

```kotlin
modules += ExampleBl()
```

## Settings

If your modules use [Settings](/doc/guides/backend/Settings.md), add a
[SettingProvider](/core/core/src/commonMain/kotlin/zakadabar/stack/setting/SettingProvider.kt). Providers 
included with the stack:

- [SettingBl](/core/core/src/jvmMain/kotlin/zakadabar/stack/backend/setting/SettingBl.kt) for JVM
- [DefaultSettingProvider](/core/core/src/commonMain/kotlin/zakadabar/stack/setting/DefaultSettingProvider.kt) for Common

```kotlin
modules += SettingBl()
```

```kotlin
modules += DefaultSettingProvider()
```

## Startup

When you added all modules, you start them with:

```kotlin
modules.initializeDb()
modules.resolveDependencies()
modules.start()
```

## Shutdown

You can also stop and clear the module store. 

- `stop` calls `onModuleStop` for all modules
- `clear` removes all installed modules from the store.

```kotlin
modules.stop()
modules.clear()
```

## Guides

[Modules](/doc/guides/common/Modules.md)