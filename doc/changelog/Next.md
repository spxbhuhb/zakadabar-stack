# Next

This page contains the changes included in the next release.

## Core

**added**

* `CommBase.json` field, contains the system-wide Json serializer
* `options2` extension for string select form fields
* `AbstractLocalizedFormats` - contains common localization functions
* `DefaultLocalizedFormats` - the default (english based) localization format
* `HuLocalizedFormats` - Hungarian localized formats
* `LocalizationConfig` - Basic (very) localization settings.
* `skipLocaleFormats` to provide legacy applications an easy way to skip locale implementation changes
* `LocalizedFormats` interface - formatters for Int, Long and Double
* `ZkForm` - `options2` extension method for string based selects to provide both the stored and displayed value

**changed**

* `KtorServerBuilder` now uses `CommBase.json` as the Json serializer
* `ActionCom`, `EntityComm`, `QueryComm` now uses `CommBase.json` as the Json serializer
* Json serialization now allows Double.NaN values (`allowSpecialFloatingPointValues = true`)
* `ZkApplication` now calls `setLocalizedFormats` to set the formatters for the selected locale
* `ZkColumn` now have a `renderer` property that is a function, `render` calls this function to render the rows
* `ZkColumn<T>.renderer` extension function to add renderers to standard columns easily
* numeric `ZkTable` columns now render values with localization

**removed**

* v1 ZkTable column classes

## Lib: Accounts

**changed**

* Account form now shows the locale of the user and lets the user change the locale when the i18n module is installed.
* New user creation lets the administrator select the locale of the user when the i18n module is installed.

## Lib: I18N

**changed**

* `TranslationProvider.getLocales` function to retrieve the available locales
* The default translation provider calls `LocaleBo.all()` to get locales and returns with the public ones.