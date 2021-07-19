# Standalone Query BL With Public Access

This query has no persistence API behind it, it does some calculation
and returns with the result.

The trick is to extend the authorizer interface and override the `authorizeQuery`
method to allow the query for everyone.

## Business Object

[Query.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/backend/authorize/query/anonymous/Query.kt)

## Business Logic

[Query.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/backend/authorize/query/anonymous/QueryBl.kt)
