# Authorizer

Authorizers are used by [Business Logic](./BusinessLogic.md) classes to 
perform the actual authorization.

<div data-zk-enrich="Note" data-zk-flavour="Danger" data-zk-title="Endpoint Only">
The automatic call of authorizers works for public endpoints only (access from outside).
If you use cross-BL calls, you have to authorize manually or call the wrapper version
of the methods to kick off full processing.
</div>


## Built-in Authorizers

[UnsafeAuthorizer](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/UnsafeAuthorizer.kt) - Allows everything. Must be explicitly enabled at server startup.
[SimpleRoleAuthorizer](/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/SimpleRoleAuthorizer.kt)  - Role-based authorization.

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