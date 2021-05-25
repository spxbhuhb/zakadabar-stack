# Business Logic

Business logic (BL) components on the backend receive business objects and perform
the business level processing. It is important that BLs do not handle data 
persistence, they are pure implementation of business.

Most BLs extend [EntityBusinessLogicBase](/src/jvmMain/kotlin/zakadabar/stack/backend/business/EntityBusinessLogicBase.kt).
This class hides most of the implementation details like Ktor routing, serialization, data validation and so on.
It lets you concentrate on the business code you have to implement.

## Anatomy of EntityBusinessLogicBase

EBLB (`EntityBusinessLogicBase`) provides many functions out-of-the-box:

- Ktor routing
- validation
- authorization
- audit log
- transaction
- serialization

All of these have sensible defaults, except authorization which you have to choose yourself.
The general pattern is that there is a variable in EBLB which contains the default, and you
can override it if you want. A very minimal example:

```kotlin
class SimpleExampleBlGen : EntityBusinessLogicBase<SimpleExampleBo>(
    boClass = SimpleExampleBo::class
) {

    override val pa = SimpleExampleExposedPaGen()

    override val authorizer = SimpleRoleAuthorizer<PrincipalBo> {
        all = StackRoles.securityOfficer
    }

}
```

This class provides all CRUD endpoints (list, read, create, update, delete) for the users
who have the security officer role.

`pa` is the [Persistence API](./PersistenceApi.md) (PA). It maps `SimpleExampleBo` business objects
into the SQL database. For simple CRUD you can use the generated PA, for more complex cases you
can extend it.

`authorizer` is an instance of [Authorizer](./Authorizer.md). Each business logic needs one, it 
performs authorization of incoming requests.

A somewhat more complex example:

```kotlin
class PrincipalBl : EntityBusinessLogicBase<PrincipalBo>(
    boClass = PrincipalBo::class
) {

    private val settings by setting<ModuleSettingsBo>("zakadabar.lib.accounts")

    override val pa = PrincipalExposedPaGen()

    override val authorizer = SimpleRoleAuthorizer<PrincipalBo> {
        all = StackRoles.securityOfficer
        action(PasswordChangeAction::class, StackRoles.siteMember)
    }

    override val router = router {
        action(PasswordChangeAction::class, ::action)
    }

    override val auditor = auditor {
        includeData = false
    }

    override fun update(executor: Executor, bo: PrincipalBo) : PrincipalBo {
        // ... perform update ...
    }

    private fun action(executor: Executor, action: PasswordChangeAction) : ActionStatusBo {
        // ... execute the action ...
    }
}
```

`settings` - is an instance of `ModuleSettingsBo` (specific to this module) that is loaded
from a setting file or from persistence. See [Settings](./Settings.md) for details.

`authorizer` - Here it has an action added. When an account wants to perform this action, it
needs the site member role.

`router` - As this business logic provides an action, the routing for that specific action has to 
be added. This one binds the `PasswordChangeAction` BO to the `action` method of this class
with the `PasswordChangeAction` parameter. See [Routing](./Routing.md) and [URLs](../common/URLs.md) 
for more information.

`auditor` - Auditors responsible for auditing system access. Here we configure the auditor
such a way that the request data is not included in the audit. In this case it is 
important, because this is the BL of PrincipalBo, and the requests may contain sensitive
information. See [Auditor](./Auditor.md) for more information.

`update` - This is an override of the default CRUD update because we want some special
processing steps there.

`action` - This is an action the BL offers as an endpoint.