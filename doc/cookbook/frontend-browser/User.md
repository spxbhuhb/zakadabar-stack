# Current User

[Application.executor](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/Application.kt) contains:

* [AccountPublicDto](../../../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/AccountPublicDto.kt) of the
  current user
* list of the names of the roles the current user has

To execute frontend code depending on the current user use methods provided
by [ZkElement](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/ZkElement.kt):

```kotlin
ifAnonymous {
    + "Please log in."
}

ifNotAnonymous {
    + "Hello $displayName!"
}

withRole("vip") {
    + "Welcome to the VIP lounge."
}

withoutRole("vip") {
    + "All of our users are VIPs, but some are even more."
}
```