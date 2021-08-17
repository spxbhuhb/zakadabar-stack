# Business Logic with Actions and/or Queries Only

```yaml
level: Beginner
targets:
  - common
tags:
  - module
  - business
  - action
  - query
```

You can write business logics that are independent of entities and provide
only queries and actions.

1. use [BusinessLogicCommon](/core/core/src/commonMain/kotlin/zakadabar/core/business/BusinessLogicCommon.kt) with `BaseBo` as type parameter
1. set `namespace`   
1. add actions and queries to the router and the authorizer

## Guides

- [BusinessLogic](/doc/guides/backend/BusinessLogic.md)

## Code

- [MultiBl.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/business/multi/MultiBl.kt)
