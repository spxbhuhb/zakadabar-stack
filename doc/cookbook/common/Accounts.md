# Accounts

## Frontend

### Get Account of the User on the Frontend

The account of the frontend user is available in [FrontendContext.executor](../../../src/jsMain/kotlin/zakadabar/stack/frontend/FrontendContext.kt)

There is **always** an account, it must not be empty. 

[FrontendContext.init](../../../src/jsMain/kotlin/zakadabar/stack/frontend/FrontendContext.kt) fetches the account during bootstrap:

* when there is an active session: the user of the session,
* when the user haven't logged in yet: the account named "anonymous".

## Backend

### Get Account of the User on the Backend

The backend provides an extension function to Ktor's `ApplicationCall` that returns with an 
[Executor](../../../src/jvmMain/kotlin/zakadabar/stack/util/Executor.kt) instance.

This instance has an `entityId` field which stores the id of the entity that executes the code.

When you use `entityRestApi` or `recordRestApi` the API wrapper passes the backend to your backend
implementation as the first parameter of the called method:

```kotlin
interface EntityRestBackend<T> {

    fun query(executor: Executor, id: Long? = null, parentId: Long? = null): List<T>

    fun create(executor: Executor, dto: T): T

    fun update(executor: Executor, dto: T): T

}
```

When you use your own routing you can get the executor like this:

```kotlin
 override fun install(route: Route) {
    with(route) {
        get("/statistics") { 
            val executor = call.executor()
            BackendContext.requireRole(executor, "/system/roles/statistician")
            call.respondText("some statistics for $executor") 
        }
    }
}
```

## User Passwords

If you implement your own backend you have to encrypt and validate user passwords. To do so
use the [BCrypt](../../../src/jvmMain/kotlin/zakadabar/stack/util/BCrypt.kt) utility.

To Encrypt:

```kotlin
val passwordHash = BCrypt.hashpw(plain_password, BCrypt.gensalt())
``` 

To Validate:

```kotlin
if ( ! BCrypt.checkpw(candidate_password, stored_hash)) throw Unauthorized()
```

## Providing Account Data for Other Modules

You'll probably have your own data model for user accounts.

However, many modules would like to know a few things about your accounts: the name of the user,
the avatar, the e-mail address etc.

To provide these add a backend API and a backend implementation to serve the requests with the route
`CommonAccountDto.type`. 

In most cases it is a good idea to use a cache as the frontend usually access account data very frequently.

```kotlin
object CommonAccountApi : RoutingContract {

    override fun install(route: Route) {
        with(route) {

            route(CommonAccountDto.type) {

                get {
                    call.respond(query(call.executor(), parentId = call.parameters["parent"]?.toLong()))
                }

                get("/{id}") {
                    val folder = query(call.executor(), id = call.parameters["id"]!!.toLong())
                    if (folder.isEmpty()) throw NotFoundException()
                    call.respond(folder[0])
                }

            }
        }
    }
}
```

```kotlin
object CommonAccountBackend : BackendContract {

    fun query(executor: Executor, id: Long? = null, parentId: Long? = null): List<CommonAccountDto> = transaction {

        val condition = if (id == null) {
            (EntityTable.parent eq parentId) and (EntityTable.type eq YourAccountDto.type)
        } else {
            YourAccountDto.id eq id // YourAccountDto is the DTO class you've written for yourself
        }

        YourAccountTable // this is your account table
            .join(EntityTable, JoinType.INNER) { EntityTable.id eq AccountTable.id }
            .select(condition)
            .filter { EntityTable.readableBy(executor, it) } // you probably want to change this
            .map(::toDto) // you will need to write the ::toDto method that converts between your account data and CommonAccountDto
    }

}
```