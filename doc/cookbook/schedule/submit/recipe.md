# Submit a Job

```yaml
level: Beginner
targets:
  - common
tags:
  - schedule
  - job
  - create
  - submit
```

1. Add a dispatcher and a worker to the configuration as described in the [Introduction](/doc/guides/libraries/schedule/Introduction.md#Setup)
2. Create a job. `Action` is the action class to execute.

```kotlin
default<Job> {
    actionNamespace = Action.boNamespace
    actionType = Action::class.simpleName !!
    actionData = Json.encodeToString(Action.serializer(), Action(12L + i))
}.create()
```

## Guides

- [Introduction](/doc/guides/libraries/schedule/Introduction.md#Setup)
