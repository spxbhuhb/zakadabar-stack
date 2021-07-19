# Logging

The backend uses [LOGBack](http://logback.qos.ch) for logging.

Business logics log events through auditors. See [Auditor](./Auditor.md) for more information.

## Configure Logback

The default configuration is in [logback.xml](/core/core-core/src/jvmMain/resources/logback.xml).

To create your own configuration write a modified logback.xml file, place it in the
`etc` directory of the backend and use the following command line argument to when starting the backend:

```text
java -Dlogback.configurationFile=etc/logback.xml
```

## Switch of Read and Query Logs

To stop the automatic RecordBackend logging for reads and queries:

```kotlin
Server.logReads = false
```
