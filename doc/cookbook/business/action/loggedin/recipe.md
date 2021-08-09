# Action For Logged-In Users

```yaml
level: Beginner
targets:
  - jvm
tags:
  - business logic
  - action
  - authorize
  - logged in
```

## Guides

- [Authorizer](/doc/guides/backend/Authorizer.md)
- [Business Logic](/doc/guides/backend/BusinessLogic.md)

## With SimpleRoleAuthorizer

Set query role to `LOGGED_IN`.

### Code

- [Action.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/action/loggedin/Action.kt)
- [ActionSraBl.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/action/loggedin/ActionSraBl.kt)

## With General Authorizer

Extend the authorizer and override the `authorizeAction` method.

### Code

- [Action.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/action/loggedin/Action.kt)
- [ActionBl.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/action/loggedin/ActionBl.kt)
