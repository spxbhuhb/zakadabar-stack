# Strings

To use string resources in your application:

* extend the [ZkBuiltinStrings](../../core/src/commonMain/kotlin/zakadabar/stack/resources/ZkBuiltinStrings.kt) class,
  as [DemoStrings](../../demo/demo/src/commonMain/kotlin/zakadabar/demo/resources/DemoStrings.kt) does
* in [main.kt](../../demo/demo/src/jsMain/kotlin/main.kt)
  set [ZkApplication](../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt)`.strings` to
  the instance of your class
* use one of the following patterns to access your strings:

To get a string from you the string store, checked by the compiler:

```kotlin
+ div {
    + Strings.something
}
```

To get something you build on the file form the string store (not checked by the compiler):

```kotlin
+ div {
    + t("some" + "thing")
}
```

## Localized Strings

If you want i18n, switchable languages or any other customization that may be changed on the fly:

* add
  a [LocaleStringBackend](../../core/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/resources/LocaleStringBackend.kt)
  to your application,
* make sure you download the localized strings during application startup, example
  in [main.kt](../../demo/demo/src/jsMain/kotlin/main.kt)
* use the strings as described above

The locale is stored in `ZkApplication.executor.account.locale`. If there is no locale, the default browser language is
used.

## Automatic bindings

Forms and tables look up the labels and headers automatically, based on the name of the property. This uses
[ZkApplication](../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt)`.strings`, and in
consequence localization affects forms and tables automatically.

You can switch off this behaviour by specifying labels and headers directly.