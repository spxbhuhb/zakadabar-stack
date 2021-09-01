# Settings

Settings provide customization for specific deployments.

There are three layers:

1. defaults (hard-coded),
1. configuration files,
1. environment variables.

## Startup

When the application starts, it looks for the server settings file:

1. value of the "--settings" program argument
2. one of the files:

```
   ./stack.server.yml
   ./etc/stack.server.yaml
   ./etc/stack.server.yml
   ../etc/stack.server.yaml
   ../etc/stack.server.yml
   ./template/app/etc/stack.server.yaml
```

The directory that contains the server settings file becomes the
"settings directory". All other setting files have to be in this directory.

1. The application stops if there is no server settings file.
1. [Server](/core/core/src/jvmMain/kotlin/zakadabar/core/server/Server.kt) uses 
   [ServerSettingLoader](/core/core/src/jvmMain/kotlin/zakadabar/core/server/ServerSettingLoader.kt) 
   to load the server settings file into a
   [ServerSettingsBo](/core/core/src/commonMain/kotlin/zakadabar/core/server/ServerSettingsBo.kt).
1. The application initializes the database connection with the loaded settings.

## Write a Setting BO

A setting BO is just a normal BO, check [Data](../common/Data.md) for details.

One thing to mention is the default values in the schema. If your settings are loaded from file, you have to set the defaults in the class constructor. YAML loaders does not use the default from the
schema so, they need the defaults there.

To prevent the schema overriding the defaults with zeros, you also have to set schema defaults. Best is to use the instance value directly like this:

```kotlin
import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema

@Serializable
class SessionBackendSettingsBo(

   var sessionTimeout: Long = 30000,
   var updateDelay: Long = 120,
   var expirationCheckInterval: Long = 120

) : BaseBo {

   override fun schema() = BoSchema {
        + ::sessionTimeout default sessionTimeout
        + ::updateDelay default updateDelay
        + ::expirationCheckInterval default expirationCheckInterval
    }

}
```

## Use a Setting BO

Create a delegated property as shown below. In this case the `namespace` of the
setting will be is generated from the class specified in the type parameter:

1. remove `zakadabar.` from the front
1. remove part after last `.` (class name)
1. remove `.data` when the package name ends with it


```kotlin
private val settings by setting<ModuleSettings>()
```

You can specify the `namespace` of the setting explicitly.

```kotlin
private val settings by setting<ModuleSettings>("lib.accounts")
```

`namespace` defines the name of the configuration file this setting is loaded from.

You may use the line above multiple times. Note, that in this case any given (type, namespace)
pair returns with the **same instance**.

Setting BOs are loaded with the following mechanism:

1. If there is a setting BO already loaded for the given (type, namespace) use that BO.
1. Create a setting BO instance with the default values.
1. Check if there is a file in the settings directory (see above) with the name `namespace` + `.yaml` or
   `namespace` + `.yml`. If there is such a file, load its content into the settings instance.
1. Merge environment variables.   
1. Validate the settings by checking the `isValid` property of the settings BO.

If any of the following steps produces an error, an exception is thrown and typically the server won't start.

## Environment Variables

The setting loader merges environment variables into setting BOs. If the variable
does not exist, the appropriate BO field remains unchanged.

The merge of environment variables does not happen if you start application with 
the `--ignore-environment` option.

Environment variable name construction:

1. uppercase `namespace` and the `propertyName`
1. replace `.` with `_` in the `namespace`
1. if namespace is not empty: `<namespace>_<propertyName>` else `propertyName`

Environment variable merge **does not support** override of:

- nested instances
- lists
