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

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Mixing Zk and Sui themes">

If you want to change between Zk and Sui themes on the same page, you have to refresh the page
after the theme change.

The "in-place" theme switch works only when the themes use the same styles which is not true between
Zk and Sui, therefore the switch does not work without refresh.

</div>

### Plain Text

Remote comm:

```kotlin
sendMail("noreply@simplexion.hu", "Subject", "Content")
```

Local comm:

```kotlin
sendMail("noreply@simplexion.hu", "Subject", "Content", executor = executor)
```

### HTML Text

Remote comm:

```kotlin
sendMail("noreply@simplexion.hu", "Subject", "<html><body>Content</body></html>", "text/html")
```

Local comm:

```kotlin
sendMail("noreply@simplexion.hu", "Subject", "<html><body>Content</body></html>", "text/html", executor = executor)
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
