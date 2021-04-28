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
   [ServerSettingsDto](../../../../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/settings/ServerSettingsDto.kt).
1. From the server settings the database connection is initialized.
1. The setting overrides are loaded for the server settings by calling the `overrideSettings` method of the
   [Server](../../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/Server.kt). Note that the database connection is already initialized at this point, so you cannot override the database
   parameters from the database.

Default implementation of `overrideSettings` use [SettingsBackend](../../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/resources/SettingBackend.kt)
to load overrides.

## Write a Setting DTO

A setting DTO is just a normal DTO, check [Data](../Data.md) for details.

One thing to mention is the default values in the schema. If your settings are loaded from file, you have to set the defaults in the class constructor. YAML loaders does not use the default from the
schema so, they need the defaults there.

To prevent the schema overriding the defaults with zeros, you also have to set schema defaults. Best is to use the instance value directly like this:

```kotlin
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

## Use a Setting DTO

Create a delegated property:

```kotlin
private val settings by setting<SessionBackendSettingsDto>("zakadabar.stack.session")
```

The type parameter is the DTO class that stores your settings.

The function parameter is the `namespace` that defines:

* the name of the configuration file this setting is loaded from,
* the content of the `namespace` field in the SQL settings table.

You may use the line above multiple times. Note, that in this case any given (type, namespace)
pair returns with the **same object instance**.

Setting DTOs are loaded with the following mechanism:

1. If there is a setting DTO for the given (type, namespace) use that DTO.
1. Create a setting DTO instance with the default values.
1. Check if there is a file in the settings directory (see above) with the name `namespace + ".yaml"` or
   `namespace + ".yml`. If there is such a file, load its content into the settings instance.
1. Check if there are entries in the database for this namespace. If there are, use them to override the values in the instance.
1. Validate the settings by checking the `isValid` property of the settings DTO.

If any of the following steps produces an error, an exception is thrown and typically the server won't start.