# JVM Client

```yaml
level: Beginner
targets:
  - jvm
tags:
  - client
```

The JVM client offers the very same interface as the browser client. The only
difference is that the JVM client has to set the URL:

```kotlin
CommConfig.global = CommConfig(baseUrl = "http://localhost:8080")
```

## Guides

- [Data](/doc/guides/common/Data.md)

### Code

- [main.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/jvm/client/main.kt)
