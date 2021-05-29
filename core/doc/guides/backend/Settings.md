# Settings

On the backend settings are used customize the backend for specific deployments.

There are three layers of settings:

1. defaults (hard-coded),
1. configuration files,
1. overwrites from the database.

**Override of nested instances and enums is not supported yet.**

## Startup

When the backend starts, it looks for the server settings file:

1. value of the "--settings" program argument
2. one of the files:

```
   ./zakadabar.stack.server.yml
   ./etc/zakadabar.stack.server.yaml
   ./etc/zakadabar.stack.server.yml
   ../etc/zakadabar.stack.server.yaml
   ../etc/zakadabar.stack.server.yml
   ./template/app/etc/zakadabar.stack.server.yaml
```

The directory that contains the server settings file becomes the
"settings directory". All other setting files has to be in this directory.

1. The backend stops if there is no server settings file.
1. Once the file is located, the backend loads the content of it into a
   [ServerSettingsBo](/src/commonMain/kotlin/zakadabar/stack/data/builtin/settings/ServerSettingsBo.kt).
1. From the server settings the database connection is initialized.
1. The setting overrides are loaded for the server settings by calling the `overrideSettings` method of the
   [Server](/src/jvmMain/kotlin/zakadabar/stack/backend/Server.kt). Note that the database connection is already
   initialized at this point, so you cannot override the database parameters from the database.

Default implementation of `overrideSettings` use [SettingsBackend](../../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/resources/SettingBackend.kt)
to load overrides.

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
setting will be the package name of the class specified in the type parameter.

```kotlin
private val settings by setting<ModuleSettings>()
```

You can specify the `namespace` of the setting explicitly.

```kotlin
private val settings by setting<ModuleSettings>("zakadabar.lib.accounts")
```

`namespace` defines:

* the name of the configuration file this setting is loaded from,
* the content of the `namespace` field in the SQL settings table.

You may use the line above multiple times. Note, that in this case any given (type, namespace)
pair returns with the **same instance**.

Setting BOs are loaded with the following mechanism:

1. If there is a setting BO already loaded for the given (type, namespace) use that BO.
1. Create a setting BO instance with the default values.
1. Check if there is a file in the settings directory (see above) with the name `namespace + ".yaml"` or
   `namespace + ".yml`. If there is such a file, load its content into the settings instance.
1. Check if there are entries in the database for this namespace. If there are, use them to override the values in the instance.
1. Validate the settings by checking the `isValid` property of the settings BO.

If any of the following steps produces an error, an exception is thrown and typically the server won't start.