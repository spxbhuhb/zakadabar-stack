# Strings

Zakadabar uses string stores for strings that are presented to the user some way. For example:

- button labels, table headers, form field labels ,
- column headers of a generated Excel file,
- strings in an e-mail.

The string stores:

- provide localization
- are updated from the server during application startup
- provide compile-time checking of string variables

The [i18n](/doc/guides/libraries/i18n/Introduction.md) library module provides
basic localized string handling: management UI, business logic and persistence.

String stores work without the i18n module, but then you have no localization, all strings
are final in the code.

## Write a String Store [source code](/lib/examples/src/commonMain/kotlin/zakadabar/lib/examples/resources/ExamplesStrings.kt)

Most string stores extend [ZkBuiltinStrings](/core/core/src/commonMain/kotlin/zakadabar/core/resource/ZkBuiltinStrings.kt),
which in turn is an extension of [ZkStringStore](/core/core/src/commonMain/kotlin/zakadabar/core/resource/ZkStringStore.kt).

The built-in string store contains strings the stack uses by itself, for example label of the "back" buttons, so it is a
good practice to extend it when you write your own string store.

String stores are in `commonMain`. The reason behind this is, that this way they are accessible from all code.

1. Extend [ZkBuiltinStrings](/core/core/src/commonMain/kotlin/zakadabar/core/resource/ZkBuiltinStrings.kt). Override anything
   you would like and add new ones as needed.
1. On the browser frontend:
    1. define an internal `strings` variable that stores that instance
    1. pass `strings` to `initLocale` in `main.kt`, see [Introduction](../browser/Introduction.md) for details.

```kotlin
internal var strings = ExamplesStrings()

open class ExamplesStrings : ZkBuiltinStrings() {

    val accountName by "Account Name"

}
```

In `main.kt`:

```kotlin
initLocale(strings)
```

With this setup the variable `strings` and `localizedStrings` in
[ZkApplication.kt](/core/core/src/jsMain/kotlin/zakadabar/core/browser/application/ZkApplication.kt) 
will be the exact same instance.

This is quite convenient when writing code because:

* automatic lookups works with your application specific strings, these use `localizedStrings`
* you can use your application specific strings like `strings.something`

## Use a String Store

Get a string from the string store:

```kotlin
+ div {
    + strings.something
}
```

Get with a key you build on the fly (not checked by the compiler).

```kotlin
+ div {
    + strings["some" + "thing"]
}
```

The string store provides access with normalized keys. Normalization removes all hyphens and converts the key to
lowercase.

```kotlin
strings.getNormalized("some-Thing")
```

Use any non-string object to get the value for its normalized class name.

```kotlin
strings.getNormalized(MyElement()) // same as getNormalized("MyElement")
```

You can easily translate class names into strings with the `translate` method:

## Use Localized

There are a few convenience functions that transform a name into the localized
version:

Get localized version of a string store key:

```kotlin
"back".localized
```

Get the localized name of a `KClass`:

```kotlin
MyElement::class.localized
```

Get the localized name of a `KMutableProperty` (note that this results in the
localized property **name**, not the value):

```kotlin
MyBo::myProp.localized
```

Get the localized name of a type:

```kotlin
localized<MyElement>() // same as getNormalized("MyElement")
```

## Localization Groups

Localization groups provide local scope for string definitions. This makes
it possible to use the same variable name in different contexts.

The `LocalizationGroup` puts the defined strings into `localizedString` with
the key `<group-name>.<variable-name>`.

```kotlin
object Group1: LocalizationGroup("name1") {
   val text by "localized 1"
}

object Group2: LocalizationGroup("name2") {
    val text by "localized 2"
}
```

It is also possible to use a class in the constructor. This is very useful
when defining the group as a companion object. With this pattern all strings
are available in the actual class and all of them are specific to the given
class.

```kotlin
companion object : LocalizationGroup(MyElement::class) {
    val text by "localized 2"
}
```

## Browser Frontend

`application.locale` stores the current locale of the application. 

`application.initLocale`:

- Initializes `application.locale` from the URL of the browser window.
- Downloads localized strings of the given locale from the server and updates the string stores.

For more information see [Introduction: Browser](../browser/Introduction.md).

After `initLocale` both `localizedStrings` and `strings` (if you followed the convention and defined an internal `strings` 
variable) store the localized strings, so everything shows up in the current language.

### Automatic Binding

Forms, tables, navigation components can automatically look up the labels and headers, based on the name of the property
or target. This uses the [localizedStrings](/core/core/src/commonMain/kotlin/zakadabar/core/resource/global.kt). In
consequence localization affects forms and tables automatically.

These automatic lookups use `getNormalized`.

You can switch off this behaviour by specifying labels and headers explicitly.