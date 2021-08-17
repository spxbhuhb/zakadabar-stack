# Services

[ZkApplication](/core/core/src/jsMain/kotlin/zakadabar/core/browser/application/ZkApplication.kt) property `services` 
is a registry of components that are loosely linked together and/or are replaceable with other implementation.

For example, ZkSessionManager is an interface, and the application requires that there is a service that implements
this interface. However, the exact implementation is not important, we are free to set the session
manager as we need.

`application.services` is an [InstanceStore](/core/core/src/commonMain/kotlin/zakadabar/core/util/InstanceStore.kt), 
it contains the instances that provide the actual service.

## Add a Service

```kotlin
application.services += SessionManager()
```

## Use a Service

Use methods of [InstanceStore](/core/core/src/commonMain/kotlin/zakadabar/core/util/InstanceStore.kt)
to find the service you need.

Examples (check `InstanceStore` for all possibilities):

```kotlin
application.services.first<ZkSessionManager>()
```

```kotlin
application.services.firstOrNull<ZkSessionManager>()
```

