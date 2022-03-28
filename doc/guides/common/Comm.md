# Comm

Communication between Zakadabar nodes (servers, browsers, mobile clients, non-UI clients)
usually use CRUDs, actions and queries. All these are built on the `comm`, a small framework
to make sending requests and receiving responses as simple as possible.

The following example creates a new entity of `TestBo`. The nice thing is that this call
works on all platforms, the syntax is the same, the semantics are the same.

```kotlin
default<TestBo>().create()
```

Check [Data](Data.md) for more information about how to write and use CRUDs, actions and queries.

## Customization

It is possible to override the defaults of the communication with a [CommConfig](/core/core/src/commonMain/kotlin/zakadabar/core/comm/CommConfig.kt) instance.

`CommConfig` has fields that may be used to override the endpoint to use for the communication.

| field     | use                                                                                           |
|-----------|-----------------------------------------------------------------------------------------------|
| baseUrl   | The start of the url to connect: `https://zakadabar.io/` for example.                         |
| namespace | Namespace to use.                                                                             |
| fullUrl   | Use this as an URL and do not add anything else but query parameters.                         |
| local     | Shortcut the communication, do not use HTTP but find the module locally and call it directly. |

There are three levels at which you may override the endpoint (in order or precedence):

1. call configuration
2. class configuration
3. global configuration

### Global Configuration

Companion of [CommConfig](/core/core/src/commonMain/kotlin/zakadabar/core/comm/CommConfig.kt) has a `global` property
which is used when no class / call configuration is provided.

You can change this any time as the example below shows, but be aware that this will affect **all** communication.

This example shows a typical setting for an Android or a JVM client. For browsers the default is set automatically:
`baseUrl = ""`.

```kotlin
CommConfig.global = CommConfig(baseUrl = "https://192.168.1.2:8080")
```

### Class Configuration

If you want one BO/action/query to go different way than the rest of the program, you can set
the `commConfig` property of the companion object that belongs to the given BO, action or query.

You can do this any time, but be aware that changing this property overrides all communication of the 
given class.

```kotlin
TestBo.commConfig = CommConfig(baseUrl = "https://10.1.1.1")
```

### Call Configuration

You can override the endpoint for specific calls by passing a [CommConfig](/core/core/src/commonMain/kotlin/zakadabar/core/comm/CommConfig.kt) 
to the call.

```kotlin
TestAction().execute(config = CommConfig(fullUrl = "https://my.own.server/my-own-url"))
```

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Two Methods">

Action and query class definitions usually has an `execute` method that uses the default
configuration.

To pass an executor and/or a configuration you have to define an additional execute
method. Check [TestAction](/core/core/src/jvmTest/kotlin/zakadabar/core/comm/TestAction.kt) or
[TestQuery](/core/core/src/jvmTest/kotlin/zakadabar/core/comm/TestQuery.kt) for an example.

</div>

### Local Calls

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="JVM Only">

Local calls work only on JVM. Support could be added for Javascript easily, but I don't
see the need at the moment.

</div>


By specifying a call configuration with the property `local` set to `true` the system you can
avoid using HTTP/HTTPS. Local calls find the given module in the process that runs the call.

You can specify local call at any level (global, class, call). The following example performs
this single call as a local call, all other queries use the default configuration.

```kotlin
TestQuery().execute(executor, CommConfig(local = true))
```

Local calls use [modules](/core/core/src/commonMain/kotlin/zakadabar/core/module/globals.kt) to find the module
based on the namespace and the name of the BO/action/query class.

Local calls use the `wrapper` functions of the BL instances. This means that:

- authorization, validation, audit calls are performed as for normal HTTP requests
- Exposed transactions are nested (tbh I have no idea what happens exactly, should check)

## Implementation Notes

[CommConfig](/core/core/src/commonMain/kotlin/zakadabar/core/comm/CommConfig.kt) contains the methods to
merge configurations and find local BLs.

[BusinessLogicRouter](/core/core/src/commonMain/kotlin/zakadabar/core/route/BusinessLogicRouter.kt) defines 
`findActionFunc` and `findQueryFunc` to find the handler methods of actions and queries.

For local calls objects are not serialized / deserialized.

`commConfig` instances are protected by `CommConfig.configLock` to ensure JVM thread synchronization.