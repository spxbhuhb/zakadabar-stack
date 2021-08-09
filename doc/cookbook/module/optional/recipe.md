# Optional Module Dependencies

```yaml
level: Intermediate
targets:
  - common
tags:
  - module
  - dependency
  - optional
```

The right way (see [Declare a Dependency as Optional](/doc/guides/common/Modules.md#Declare-a-Dependency-as-Optional)).

```kotlin
val provider2 by optionalModule<ProviderModule2>()
```

The hackish way: (see [Turn a Mandatory Dependency Optional](/doc/guides/common/Modules.md#Turn-a-Mandatory-Dependency-Optional)).

```kotlin
modules.dependencies<ConsumerModule, ProviderModule1>().optional = true
```

## Guides

- [Modules](/doc/guides/common/Modules.md)

## Code

- [ConsumerModule.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/module/optional/ConsumerModule.kt)
- [main.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/module/optional/main.kt)
