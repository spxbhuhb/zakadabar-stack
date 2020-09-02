# Authentication

**Highlights**

* as soon as the frontend connects the backend creates a session, no exceptions
* the session has an `executor`: the entity who owns the session
* before logging in, the session executor is the user named "anonymous"
* all routes are inside Ktor's `authenticate`, no exceptions (but the user may be "anonymous")

The actual login means that we assign a new user to the session.

This is not implemented yet. The plan is to use authentication methods supplied by Ktor like this:

```kotlin
authenticate("basic") {
    patch("/apis/${Stack.shid}/session/login") {
       TODO("do login")
       TODO("update session executor")
    }
    patch("/apis/${Stack.shid}/session/logout") {
       TODO("do logout")
       TODO("update session executor")
    }
}
```
