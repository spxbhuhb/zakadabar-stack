# Settings

On the backend, settings are used customize the backend for specific deployments.

There are two layers of settings:

1. defaults (hard-coded),
1. configuration files.

**Override of nested instances and enums is not supported yet.**

## Startup

When the backend starts, it looks for the server settings file:

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
"settings directory". All other setting files has to be in this directory.

1. The backend stops if there is no server settings file.
1. Once the file is located, the backend loads the content of it into a
   [ServerSettingsBo](/src/commonMain/kotlin/zakadabar/stack/data/builtin/settings/ServerSettingsBo.kt).
1. From the server settings the database connection is initialized.

## Write a Setting BO

A setting BO is just a normal BO, check [Data](../common/Data.md) for details.

One thing to mention is the default values in the schema. If your settings are loaded from file, you have to set the defaults in the class constructor. YAML loaders does not use the default from the
schema so, they need the defaults there.

To prevent the schema overriding the defaults with zeros, you also have to set schema defaults. Best is to use the instance value directly like this:

```kotlin
import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema

@Serializable
class SessionBackendSettingsDto(

   var sessionTimeout: Long = 30000,
   var updateDelay: Long = 120,
   var expirationCheckInterval: Long = 120

) : DtoBase {

   override fun schema() = DtoSchema {
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
1. Validate the settings by checking the `isValid` property of the settings BO.

If any of the following steps produces an error, an exception is thrown and typically the server won't start.