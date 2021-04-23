# Sessions

## Backend

* Use Ktor session feature with:
    * [StackSession](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/StackSession.kt)
    * [SessionStoreSQL](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/SessionStorageSql.kt)
    * [SessionAuthenticationProvider](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/SessionAuthenticationProvider.kt)
* Store session data in the `sessions` SQL table:
    * [SessionTable](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/SessionTable.kt)
* Handled by:
    * [SessionBackend](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/SessionBackend.kt)
        * [LoginAction](../../../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LoginAction.kt)
        * [LogoutAction](../../../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LogoutAction.kt)

### Expiration

Session expiration is specified by the server parameter `zakadabar.core.session.timeout`. Default value is 1 hour.

A background task started by [SessionBackend](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/SessionBackend.kt)
looks for expired sessions and removes them from the session storage.

When the backend receives an unknown session cookie it creates a new session and then responds with HTTP status `440 Login Timeout`. The frontend then can decide what to do, default behaviour is to
ask the user for re-login.

## Browser Frontend

The browser frontend usually gets the session data in [main.kt](../../src/jsMain/kotlin/main.kt):

```kotlin
val session = SessionDto.read(0L) // 0L is a constant here, this returns with the current session
```

### Login and Logout

Login and logout is performed by [LoginAction](../../../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LoginAction.kt)
and [LogoutAction](../../../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LogoutAction.kt).

There is a built-in [Login](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/account/Login.kt) page that shows how to use the login action.

To execute logout you may add the following code.

```kotlin
io {
    LogoutAction().execute() // call the action
    window.location.href = "/" // go to the home page and refres
}
```

### Expiration




