* [AccountPrivateDto](./AccountPrivateDto.kt) contains data private to the given account, such as authentication information.
* [AccountPublicDto](../../../../../../../../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/AccountPublicDto.kt) contains data that is public and used when selecting/showing other accounts.
* [SessionDto](../../../../../../../../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/SessionDto.kt) data tha belongs to one frontend - backend session.

* All changes of accounts go through private.
* Public account supports list and read.

When the frontend application starts, it requests the session and sets from the server and sets the executor of the application in [main.kt](../../../../../../jsMain/kotlin/main.kt):

```kotlin
val session = SessionDto.read(0L)

with(Application) {
    executor = Executor(session.account.id, session.account.displayName, session.roles)
}
```

The `executor` property of the [Application](../../../../../../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/Application.kt)
always contains a valid account with valid roles.

Before logging in, this is the "anonymous" account with an empty role list.