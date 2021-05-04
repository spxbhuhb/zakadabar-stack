# Accounts

The stack differentiates between accounts and principals. An account is a business entity for storing business related
data such as names, contact information, etc. A principal is a security entity for authentication and authorization.

The stack contains handling of principals and accounts out-of-the-box, including:

* basic crud
* password handling
* login / logout
* authorization
* roles

## Authentication

The authentication status is associated with a session. Each session has a unique ID generated by Ktor and sent to the
frontend in a cookie.

* As soon as the frontend connects, the backend creates a session.
* The session has an `executor`, the entity who owns the session.
* Before logging in, the session executor is the user named "anonymous".
* All routes are inside Ktor's `authenticate`.

The actual login means that we assign a new user to the session.

The clients execute [LoginAction](/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LoginAction.kt)
and
[LogoutAction](/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/LogoutAction.kt) to perform login and logout.

```kotlin
var actionStatus = LoginAction("demo", Secret("wrong")).execute()

if (actionStatus.success) {
    println("logged in")
} else {
    println("failed to login")
}

session = SessionDto.read(0L) // this will return with the roles of the logged in user
```

## Get User Information on the Frontend

Frontend user information is in the `executor` property
of [ZkApplication](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt).

The `executor` property stores a [ZkExecutor](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkExecutor.kt)
instance:

```kotlin
class ZkExecutor(
    val account: AccountPublicDto,
    val anonymous: Boolean,
    val roles: List<String>
)
```

The browser frontend usually fetches the session data in `main.kt` by calling `sessionManager.init`.

This call sets the `executor` property of `ZkApplication`. For details see: [Accounts](Accounts.md).

```kotlin
 with(ZkApplication) {
    sessionManager.init()
    // ...
}
```

## Helper Methods on JavaScript Frontend

[ZkApplication](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt)

```kotlin
if (hasRole("role-name")) {
    // do something
}
```

[ZkElement](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkElement.kt)

```kotlin
ifNotAnonymous {
    // do something
}

ifAnonymous {
    // do something
}

withRole("role-name") {
    // do something
}

withoutRole("role-name") {
    // do something
}
```

## Get Account of the User on the Backend

For CRUD, queries, actions and BLOBs the called backend method receives
an [Executor](/src/jvmMain/kotlin/zakadabar/stack/util/Executor.kt)
instance.

```kotlin
open class Executor internal constructor(

    val accountId: Long,
    val roleIds: List<RecordId<RoleDto>>,
    private val roleNames: List<String>

) : Principal {

    fun hasRole(roleName: String): Boolean

    fun hasRole(roleId: RecordId<RoleDto>): Boolean

    fun hasOneOfRoles(roleNames: Array<out String>): Boolean

}
```

## Helper Methods on Backend

[authorize.kt](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize.kt)

```kotlin
authorize(executor, "role-name") // throws Unauthorized when doesn't have the role

authorize(executor, 1L) // throws Unauthorized when doesn't have the role with the given role id

authorize(executor, "role-name-1", "role-name-2") // throws Unauthorized when doesn't have at lease one of the roles

authorize(executor) { // throws Unauthorized when the check returns with false
    return (executor.accountId % 2 == 0)
}

authorize(true) // throws Unauthorized when the parameter is false, use this to enable public access
```

## Get Account in Custom Backends

The backend provides an extension function to Ktor's `ApplicationCall` that returns with
an [Executor](/src/jvmMain/kotlin/zakadabar/stack/util/Executor.kt) instance.

This instance has an `accountId` field which stores the id of the account that executes the code.

```kotlin
override fun onInstallRoutes(route: Route) {
    with(route) {
        get("ping") {
            val executor = call.executor()
            authorize(executor, "pinger")
            call.respond("pong")
        }
    }
}
```

## Public Account Information

To provide generic account data, implement a backend
for [AccountPublicDto](/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/AccountPublicDto.kt).

This DTO is meant to provide public account information for displaying names, avatars etc.

**Be careful with sensitive data, such as e-mail addresses!**

Default implementation:

* [AccountPublicBackend.kt](/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/account/AccountPublicBackend.kt)