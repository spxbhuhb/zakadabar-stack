# Logging

The backend uses [LOGBack](http://logback.qos.ch) for logging.

[RecordBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/record/RecordBackend.kt)
logs all data modify operations automatically. It logs reads and queries when the
`logReads` property of the [Server](/src/jvmMain/kotlin/zakadabar/stack/backend/Server.kt)
is `true`.

[ActionBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/action/ActionBackend.kt)
logs all actions when the 'logAction' property is `true`. This is an instance property, not a server-wide one.

[QueryBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/query/QueryBackend.kt)
logs queries when the `logReads` property of the [Server](/src/jvmMain/kotlin/zakadabar/stack/backend/Server.kt)
is `true`.

## Configure Logback

The default configuration is in [logback.xml](/src/jvmMain/resources/logback.xml).

To create your own configuration write a modified logback.xml file, place it in the
`etc` directory of the backend and use the following command line argument to when starting the backend:

```shell script
java -Dlogback.configurationFile=etc/logback.xml
```

## Switch of Read and Query Logs

To stop the automatic RecordBackend logging for reads and queries:

```kotlin
Server.logReads = false
```
