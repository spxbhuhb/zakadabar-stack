# Library: Email

E-mail sending library. This library depends on the [Schedule](../schedule/Introduction.md) library.

## Recipes

- [Send an Email](/doc/cookbook/email/send/recipe.md)

## Setup

**common**

1. add the gradle dependency

**backend**

1. add the module to your server configuration, for details see [Modules](../../common/Modules.md)
2. add settings to configure the SMTP server access

**frontend**

For administrative purposes, you can add [MailCrud](/lib/email/src/jsMain/kotlin/zakadabar/lib/email/MailCrud.kt) to your
browser frontend.

### Common

#### gradle

```kotlin
implementation("hu.simplexion.zakadabar:schedule:$stackVersion")
implementation("hu.simplexion.zakadabar:email:$stackVersion")
```

### Backend

#### add module

```kotlin
zakadabar.lib.schedule.install()
modules += WorkerBl("worker1")

zakadabar.lib.email.install()
```

#### add settings

Create a 'lib.email.yaml' file in your settings directory,
check [MailSettings](/lib/email/src/commonMain/kotlin/zakadabar/lib/email/MailSettings.kt)
for description of parameters.

```yaml
host: smtp.gmail.com
port: 587
username: "noreply@simplexion.hu"
password: "helloworld"
protocol: smtp
tls: true
auth: true
```

Don't forget to create the `worker1.yaml` file as written in [Schedule: Introduction](../schedule/Introduction.md).

## Database

The module uses SQL for data persistence. At first run it creates these SQL objects automatically.

| Table       | Content     |
|-------------|-------------|
| `mail`      | E-mails.    |
| `mail_part` | Body parts. |