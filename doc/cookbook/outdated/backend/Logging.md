# Logging

The backend uses [LOGBack](http://logback.qos.ch) for logging.

[DtoBackend](../../../src/jvmMain/kotlin/zakadabar/stack/backend/data/DtoBackend.kt)
logs all CRUD operations and all queries automatically. If you do not add
special functions you don't need to add any additional logs.

## Configure Logback

The default configuration is in [logback.xml](../../../src/jvmMain/resources/logback.xml).

To create your own configuration write a modified logback.xml file, place it in the
`etc` directory of the backend and use the following command line argument to when
starting the backend:

```shell script
java -Dlogback.configurationFile=etc/logback.xml
```

## Switch of Read and Query Logs

To stop the automatic DtoBackend logging for reads and queries:

```kotlin
BackendContext.logReads = false
```
