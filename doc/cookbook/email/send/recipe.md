# Send an Email

```yaml
level: Beginner
targets:
  - common
tags:
  - email
  - create
  - send
```

1. Add a dispatcher and a worker to the configuration as described in [Schedule: Introduction](/doc/guides/libraries/schedule/Introduction.md#Setup)
2. Add the email modules as described in the [Email: Introduction](/doc/guides/libraries/email/Introduction.md#Setup)
3. Use the `sendMail` function to send the e-mail.

### Plain Text

```kotlin
sendMail("noreply@simplexion.hu", "Subject", "Content")
```

### HTML Text

```kotlin
sendMail("noreply@simplexion.hu", "Subject", "<html><body>Content</body></html>", "text/html")
```

### HTML Text and Attachment

```kotlin
val a1 = "attachment 1".encodeToByteArray()
val a2 = "attachment 2".encodeToByteArray()

val content = "<html><body>Content</body></html>"

sendMail(
    "noreply@simplexion.hu", "Subject", content, "text/html",
    listOf(
        Triple(a1, "a1.bin", "application/binary"),
        Triple(a2, "a2.bin", "application/binary")
    )
)
```

## Guides

- [Email: Introduction](/doc/guides/libraries/email/Introduction.md#Setup)
- [Schedule: Introduction](/doc/guides/libraries/schedule/Introduction.md#Setup)
