# Record Backends

* provide access to the data stored on the server,
* are Kotlin objects that
  extend [RecordBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/record/RecordBackend.kt),
* **are not** added automatically.

## Add Record Backend From Configuration

You may add record backends directly from the configuration, without creating a module:

```yaml
modules:
  - zakadabar.demo.backend.misc.ShipBackend
```

To add a record backend from a module see: [Modules](./Modules.md)

## Add Record Backend Programmatically

You can add a backend module programmatically by adding it
to [Server](/src/jvmMain/kotlin/zakadabar/stack/backend/Server.kt)

The Server calls the `init` method of RecordBackend when it is added.

```kotlin
Server += ShipBackend
```