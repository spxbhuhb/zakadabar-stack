# Query With Public Access

```yaml
level: Beginner
targets:
  - common
tags:
  - business logic
  - query
  - authorize
  - public
```

## Guides

- [Authorizer](/doc/guides/backend/Authorizer.md)
- [Business Logic](/doc/guides/backend/BusinessLogic.md)

## With SimpleRoleAuthorizer

Set query role to `PUBLIC`.

### Code

- [Query.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/query/anonymous/Query.kt)
- [QuerySraBl.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/query/anonymous/QuerySraBl.kt)

## With General Authorizer

Extend the authorizer and override the `authorizeQuery` method to allow the query for everyone.

### Code

- [Query.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/query/anonymous/Query.kt)
- [QueryBl.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/query/anonymous/QueryBl.kt)
