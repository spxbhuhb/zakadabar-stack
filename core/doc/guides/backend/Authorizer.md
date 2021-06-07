# Authorizer

Authorizers are used by [Business Logic](./BusinessLogic.md) classes to 
perform the actual authorization.

<div data-zk-enrich="Note" data-zk-flavour="Danger" data-zk-title="Endpoint Only">

The automatic call of authorizers works for public endpoints only (access from outside).
If you use cross-BL calls, you have to authorize manually or call the wrapper version
of the methods to kick off full processing.

</div>

## Authorizer Provider

An authorizer provider is a module you can use to centralize authorization setup.
It implements the [AuthorizerProvider](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizer.kt)
interface and is reached with the `authorizer` function from a business logic.

```kotlin
override val authorizer by provider()
```

When the provider cannot find an authorizer for the given BL it should return with an EmptyAuthorizer (which
denies everything).

### SimpleRoleAuthorizerProvider

This module provides a [SimpleRoleAuthorizer](#SimpleRoleAuthorizer) instance for each class. You can add it easily:

```kotlin
server += SimpleRoleAuthorizerProvider {
    all = StackRole.siteMember
}
```

## Built-in Authorizers

- [SimpleRoleAuthorizer](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizer.kt)  - Role-based authorization, see below.
- [EmptyAuthorizer](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/EmptyAuthorizer.kt) - Denies everything.
- [UnsafeAuthorizer](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/UnsafeAuthorizer.kt) - Allows everything. Must be explicitly enabled at server startup.

## SimpleRoleAuthorizer

[SimpleRoleAuthorizer](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizer.kt) uses roles
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

**Operations not specified are denied.**

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

## Write an Authorizer

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Data Access">

If you need to access business data from an authorizer, do it through a business
logic, do not read directly into the database, nor call a Persistence API from 
the authorizer.

</div>

Three options to write authorizers:

- write a re-usable one by implementing the [Authorizer](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/Authorizer.kt) interface,
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


## Timeline

### Changes

- 2021.6.6
    - add AuthorizerProvider and SimpleRoleAuthorizerProvider
    
### Possible Improvements

- add an authorization report function that generates an auditor-ready report of authorizers