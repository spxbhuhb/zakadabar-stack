Click on the buttons above to copy the source code you've just generated to the
clipboard and then paste them into IDEA. You can find the directories and the
actual source codes if you scroll down.

---

You have to define the authorization, this code rejects everything by default. Example (assumes role based, default authorization):

```kotlin
override val authorizer : Authorizer<TestBo> = SimpleRoleAuthorizer {
    allReads = StackRoles.siteMember
    allWrites = StackRoles.siteAdmin
}
```

Remember: **deny first, allow explicitly**!

---

To include the generated codes in your application you have to add them to these
places (assuming default file and directory names).

**translation** `commonMain/kotlin/@packageName@/resources/strings.kt`

```kotlin
val className by "className"
```

**frontend routing** `jsMain/kotlin/@packageName@/frontend/Routing.kt`

```kotlin
+ browserCrudName()
```

**frontend navigation** `jsMain/kotlin/@packageName@/frontend/SideBar.kt`

```kotlin
+ item<browserCrudName>()
```

**backend module** `jvmMain/kotlin/@packageName@/backend/Module.kt`

```kotlin
server += businessLogicName()
```

---

**common** `commonMain/kotlin/@packageName@/data`

```kotlin
// commonSource
```

**browser frontend**`jsMain/kotlin/@packageName@/frontend/pages`

```kotlin
// browserSource
```

**jvm backend** `jvmMain/kotlin/@packageName@/backend`

```kotlin
// blSource
```

```kotlin
// paSource
```