# Module Bundle

```yaml
level: Beginner
targets:
  - jvm
tags:
  - module
  - bundle
  - package
  - server
  - backend
```

Module bundles pack other modules together. This makes installation and configuration
easier.

Most libraries provide a module bundle class, so you can add the library to the application
easily, like this:

```kotlin
modules += ExampleModuleBundle()
```

## Guides

- [Modules](/doc/guides/common/Modules.md)

## Code

- [ExampleModuleBundle.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/module/bundle/ExampleModuleBundle.kt)
