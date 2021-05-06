# Sessions

## Backend

* Use Ktor session feature with:
  * [StackSession](/src/jvmMain/kotlin/zakadabar/stack/backend/ktor/session/StackSession.kt)
  * [SessionStoreSQL](/src/jvmMain/kotlin/zakadabar/stack/backend/ktor/session/SessionStorageSql.kt)
  * [SessionAuthenticationProvider](/src/jvmMain/kotlin/zakadabar/stack/backend/ktor/session/SessionAuthenticationProvider.kt)
* Store session data in the `sessions` SQL table:
  * [SessionTable](/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/SessionTable.kt)
* Handled by:
    * [SessionBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/SessionBackend.kt)
        * [LoginAction](/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LoginAction.kt)
        * [LogoutAction](/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LogoutAction.kt)

## Browser Frontend

The browser frontend usually fetches the session data in `main.kt` by calling `sessionManager.init`.

This call sets the `executor` property of `ZkApplication`. For details see: [Accounts](Accounts.md).

```kotlin
with(ZkApplication) {
  sessionManager.init()
  // ...
}
```

### Login and Logout

Login and logout is performed
by [LoginAction](/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LoginAction.kt)
and [LogoutAction](/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LogoutAction.kt).

**IMPORTANT** Refresh the page after login / logout. This is ensures that all components use the current login
information.

There is a built-in [Login](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/account/login/Login.kt) page that
shows how to use the login action.

To execute logout you may add the following code.

```kotlin
io {
  LogoutAction().execute() // call the action
  window.location.href = "/" // go to the home page and refresh
}
```

## Expiration

Session expiration timeout is set by the server parameter `zakadabar.core.session.timeout`. Default value is 30 minutes.

A background task - started
by [SessionBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/session/SessionBackend.kt) - looks for
expired sessions and removes them from the session storage.

When the backend receives an unknown session cookie it creates a new session and then responds with HTTP
status `440 Login Timeout`. The frontend then can decide what to do, default behaviour is to ask the user for re-login.

### Expiration of Multiple Simultaneous Requests

The client may initiate more than one request with an expired session id.

To ensure that only one new session is created:

* The backend keeps track of old to new session id mappings for a short while.
* All requests with an expired session id check if an old to new mapping has already happened.
* If so, the request:
  * uses the value from the previous mapping,
  * returns with HTTP status `440 Login Timeout`.
* If not, the request:
  * creates a new session and saves the old to new mapping,
  * returns with HTTP status `440 Login Timeout`.

On the client side comm instances:

* Check if the response code is 440.
* If so, call `renew` function of the session manager (`sessionManager` property of `ZkApplication`, default:
  [ZkSessionManager](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkSessionManager.kt))
* The `renew` method displays a modal login window to ask the user for re-login.
* When renewal is successful the comm retries the failed call.






