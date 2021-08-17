# Sessions

## Backend

* Uses Ktor session feature with:
    * [StackSession](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/server/ktor/StackSession.kt)
    * [SessionStoreSQL](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/server/ktor/SessionStorageSql.kt)
    * [SessionAuthenticationProvider](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/server/ktor/SessionAuthenticationProvider.kt)
* Stores session data in the `session` SQL table:
    * [SessionTable](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/server/ktor/SessionTable.kt)
* Handled by:
    * [KtorSessionBl](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/business/KtorSessionBl.kt)
        * [LoginAction](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/LoginAction.kt)
        * [LogoutAction](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/LogoutAction.kt)

## Browser Frontend

The browser frontend usually fetches the session data in `main.kt` by calling `initSession(SessionManager())`.

This call sets the `executor` global variable and the `sessionDescription` property of `application`.

```kotlin
with(application) {
    initSession(SessionManager())
    // ...
}
```

### Login and Logout

Login and logout is performed by [LoginAction](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/LoginAction.kt)
and [LogoutAction](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/LogoutAction.kt).

**IMPORTANT** Refresh the page after login / logout. This is ensures that all components use the current login information.

There is a built-in [Login](/lib/accounts/src/jsMain/kotlin/zakadabar/lib/accounts/browser/login/Login.kt) page that shows how to use the login action.

To execute logout you may add the following code.

```kotlin
io {
    LogoutAction().execute() // call the action
    window.location.href = "/" // go to the home page and refresh
}
```

### Settings

Session handling parameters are stored in [ModuleSettings](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/ModuleSettings.kt).
The server loads this through the standard setting loader mechanism, see [Settings](../../backend/Settings.md).
Default filename is `lib.accounts.yaml`.

## Expiration

Session expiration timeout is defined by the settings (see above). Default value is 30 minutes.

A background task - started by [KtorSessionBl.configure](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/business/KtorSessionBl.kt) - 
looks for expired sessions and removes them from the session storage.

When the backend receives an unknown session cookie it creates a new session and then calls
`onLoginTimeout` method of the [Server](/core/core/src/jvmMain/kotlin/zakadabar/core/server/Server.kt).

`onLoginTimeout` decides what to do with this request, in the default implementation API
requests (uri starts with `/api`) respond with 440 Login Timeout, non-API requests are
processed as normal. For more detailed explanation see comments of the function.

### Expiration of Multiple Simultaneous Requests

The client may initiate more than one request with an expired session id.

To ensure that only one new session is created:

* The backend keeps track of old to new session id mappings for a short while.
* All requests with an expired session id check if an old to new mapping has already happened.
* If so, the request:
    * uses the value from the previous mapping,
    * calls `onLoginTimeout`
* If not, the request:
    * creates a new session and saves the old to new mapping,
    * calls `onLoginTimeout`

On the client side, comm instances:

* Check if the response code is 440.
* If so, call `renew` function of the session manager (`sessionManager` property of `ZkApplication`, default:
  [ZkSessionManager](/core/core/src/jsMain/kotlin/zakadabar/core/browser/application/ZkSessionManager.kt))
* The `renew` method displays a modal login window to ask the user for re-login.
* When renewal is successful, the comm retries the failed call.






