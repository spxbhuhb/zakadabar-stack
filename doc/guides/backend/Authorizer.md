# Authorizer

Authorizers are used by [Business Logic](./BusinessLogic.md) classes to 
perform the actual authorization.

<div data-zk-enrich="Note" data-zk-flavour="Danger" data-zk-title="Endpoint Only">

The automatic call of authorizers works for public endpoints only (access from outside).
If you use cross-BL calls, you have to authorize manually or call the wrapper version
of the methods to kick off full processing.

</div>

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="RoleBlProvider">

Most of these examples use `SimpleRoleAuthorizer`. This authorizer needs a backend
module that implements `RoleBlProvider`. For example,
[RoleBl](/lib/accounts/src/jvmMain/kotlin/zakadabar/lib/accounts/backend/bl/RoleBl.kt) of
[Lib: Accounts](/doc/guides/plug-and-play/accounts/Introduction.md) implements
this interface. If you do not use `Lib: Accounts`, you have to add a module that implements
`RoleBlProvider` or your server won't start.

</div>

## Authorizer Provider [unit test](/core/core-core/src/jvmTest/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizerProviderTest.kt)

An authorizer provider:

- is a module you can use to centralize authorization setup
- implements the [AuthorizerProvider](/core/core-core/src/commonMain/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizer.kt) interface 
- used by the `provider` function from a business logic
- creates a new authorizer instance for each business logic
- returns with an EmptyAuthorizer (which denies everything) when it cannot find an authorizer

```kotlin
class MyBusinessLogic : EntityBusinessLogicBase<MyBo>(MyBo::class){
    override val authorizer by provider()
}
```

You can customize the actual authorizer instance further by passing a builder
method to `provider`. In this case you have to check / assume which kind
of authorizer you work with.

```kotlin
class MyBusinessLogic : EntityBusinessLogicBase<MyBo>(MyBo::class){
  
    override val authorizer by provider {
        this as SimpleRoleAuthorizer
        query(Query::class, StackRoles.siteMember)
    }

}
```

### SimpleRoleAuthorizerProvider [unit test](/core/core-core/src/jvmTest/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizerProviderTest.kt)

This module provides a [SimpleRoleAuthorizer](#SimpleRoleAuthorizer) instance for each class. You can add it easily
from your server configuration module (see [Modules](./Modules.md)):

```kotlin
server += SimpleRoleAuthorizerProvider {
    all = StackRole.siteMember
}
```

## Built-in Authorizers

- [SimpleRoleAuthorizer](/core/core-core/src/commonMain/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizer.kt)  - Role-based authorization, see below.
- [EmptyAuthorizer](/core/core-core/src/commonMain/kotlin/zakadabar/stack/backend/authorize/EmptyAuthorizer.kt) - Denies everything.
- [UnsafeAuthorizer](/core/core-core/src/commonMain/kotlin/zakadabar/stack/backend/authorize/UnsafeAuthorizer.kt) - Allows everything. Must be explicitly enabled at server startup.

## SimpleRoleAuthorizer

[SimpleRoleAuthorizer](/core/core-core/src/commonMain/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizer.kt) uses roles
of the executor to make the authorization decisions. It is very easy to set up: you assign role names
to operations. This example uses stack roles, but you can use any role name.

```kotlin
override val authorizer: Authorizer<TestBlob> = SimpleRoleAuthorizer {
    allReads = StackRoles.siteMember
    allWrites = StackRoles.siteAdmin
}
```

During startup the authorizer looks for a `RoleBlProvider` between the server
modules and resolves all names to role ids. Then it uses those ids to perform
the actual authorization.

**Operations not specified explicitly are denied.**

Operations:

| Name | Description |
| --- | --- |
| all | All operations, including actions and queries. Overwrites preceding settings. |
| allReads | List, read, all queries. Overwrites preceding settings. |
| allWrites | Create, update, delete, all actions. Overwrites preceding settings. |
| list |  |
| read |  |
| create |  |
| update |  |
| delete |  |
| query | All queries. |
| action | All actions. |

You can authorize actions and queries one-by-one. These have precedence over
the general operation settings from the table above.

```kotlin
override val authorizer: Authorizer<TestBlob> = SimpleRoleAuthorizer {
    query(TestQuery::class, StackRoles.siteMember)
    action(TestAction::class, StackRoles.siteAdmin)
}
```

### PUBLIC

SimpleRoleAuthorizer supports the special value of `PUBLIC`. This is not an
actual role, but may be used to provide access for everyone:

```kotlin
override val authorizer: Authorizer<TestBlob> = SimpleRoleAuthorizer {
    allReads = PUBLIC
    allWrites = StackRoles.siteMember
}
```

### LOGGED_IN

SimpleRoleAuthorizer supports the special value of `LOGGED_IN`. This is not an
actual role, but may be used to provide access for all logged-in users:

```kotlin
override val authorizer: Authorizer<TestBlob> = SimpleRoleAuthorizer {
    allReads = LOGGED_IN
    allWrites = StackRoles.siteMember
}
```

## Write an Authorizer

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Data Access">

If you need to access business data from an authorizer, do it through a business
logic, do not read directly into the database, nor call a Persistence API from 
the authorizer.

</div>

Three options to write authorizers:

- write a re-usable one by implementing the [Authorizer](/core/core-core/src/commonMain/kotlin/zakadabar/stack/backend/authorize/Authorizer.kt) interface,
- write a re-usable one by extending one,
- write an anonymous one specific to one business logic class.

The first two are trivial, the third is also simple. The example below extends the Authorizer interface itself
and allows read for all users (public and logged in likewise). You could extend an existing authorizer the
same way.

`onModuleStart` function of the authorizer is called from `onModuleStart` of
the business logic.

```kotlin
class SimpleExampleBl : EntityBusinessLogicBase<SimpleExampleBo>(
    boClass = SimpleExampleBo::class
) {

    override val pa = SimpleExampleBoExposedPaGen()

    override val authorizer = object : Authorizer<SimpleExampleBo> {
        override fun authorizeRead(executor: Executor, entityId: EntityId<SimpleExampleBo>) {
            // pass
        }
    }

    // ... other codes of BL ...
}
```

## Use an Authorizer

Most cases you don't have to worry about using an authorizer. Once it is assigned
to a business logic, it will be called automatically whenever the endpoints 
offered by the BL receive a request.

You can call the authorizer directly if needed:

```kotlin
val otherModule by module<OtherBusinessLogic>()

fun authorizeOther(executor, id) = 
    otherModule
        .authorizer
        .authorizeRead(executor, id)
```