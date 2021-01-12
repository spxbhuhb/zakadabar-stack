# DTO Backends

DTO Backends:

- provide access to the data stored on the server,
- are Kotlin objects that extend [DtoBackend](../../../src/jvmMain/kotlin/zakadabar/stack/backend/data/DtoBackend.kt),
- **are not** added automatically.

## Add DTO Backend From Configuration

You may add DTO backends directly from the configuration, without creating a module:

```yaml
modules:
  - zakadabar.samples.holygrail.backend.rabbit.RabbitBackend
```

To add a DTO backend from a module see: [Modules](./Modules.md)

## Add DTO Backend Programmatically

You can add a backend module programmatically by adding it to BackendContext.

The BackendContext calls the `init` method of DtoBackend when it is added.

```kotlin
BackendContext += RabbitBackend
```