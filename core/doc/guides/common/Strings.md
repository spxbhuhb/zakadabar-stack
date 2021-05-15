# Strings

In Zakadabar the "static" strings your application presents to the users come from a string store. Think of labels on
buttons, table headers, form field labels, or strings in an Excel file you generate on the backend and send in an
e-mail.

Most string stores extend [ZkBuiltinStrings](/src/commonMain/kotlin/zakadabar/stack/resources/ZkBuiltinStrings.kt),
which in turn is an extension of [ZkStringStore](/src/commonMain/kotlin/zakadabar/stack/resources/ZkStringStore.kt).

The built-in string store contains strings the stack uses by itself, for example label of the "back" buttons, so it is a
good practice to extend it when you write your own string store.

String stores are in `commonMain`. The reason behind this is, that this way they are accessible from all code which we
really want.

## Use a String Store

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="strings and stringStore">

If you follow the conventions, the variable `strings` and `stringStore`
in [ZkApplication.kt](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt) will be the exact same
instance.

This is quite convenient when writing code because:

* automatic lookups works with your application specific strings, these use `stringStore`
* you can use your strings like `strings.something`, there will be a compiler check

The examples below assume that you follow this convention.
</div>

Get a string from the string store, checked by the compiler:

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

## Write a String Store [source code](../../../../lib/examples/src/commonMain/kotlin/zakadabar/lib/examples/resources/ExamplesStrings.kt)

1. Extend [ZkBuiltinStrings](/src/commonMain/kotlin/zakadabar/stack/resources/ZkBuiltinStrings.kt). Override anything
   you would like and add new ones as needed.
1. On the browser frontend:
    1. define a internal `strings` variable that stores that instance (this is just a convention, to make the strings
       easily accessible),
    1. pass `strings` to `initLocale` in `main.kt`, see [Introduction](../browser/Introduction.md) for details.

```kotlin
internal var strings = ExamplesStrings()

open class ExamplesStrings : ZkBuiltinStrings() {

    val accountName by "Account Name"

}
```

```kotlin
initLocale(strings)
```

## Localized Strings (I18N)

The localization is supported by:

* on the backend
    * [LocaleBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/resources/LocaleBackend.kt)
    * [TranslationBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/builtin/resources/TranslationBackend.kt)
* on the browser frontend
    * [Locales](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/resources/locales/Locales.kt)
    * [Translations](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/resources/translations/Translations.kt)

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Changing Locales and Translations">

The idea is that the site administrator can create/change locales and translations freely, during runtime, without
modifying the code of the application. The `Locales` and `Translations` pages on the browser frontend provide this
management function.
</div>

### Browser Frontend

The locale of the application is stored in `application.locale`. This field is initialized by `initLocale` from the URL
of the browser window. For more information see [Introduction](../browser/Introduction.md).

During application startup the stack downloads the localized strings from the server. You can switch off this behaviour
by passing `false` in the `downloadTranslations` parameter of `application.initLocale`.

Your string store (both `application.stringStore` and `strings`, if you followed the convention) will store the
localized strings, so everything shows up in the current language.

## Automatic Binding

### Browser

Form, tables, navigation components can automatically look up the labels and headers, based on the name of the property
or target. This uses the [stringStore](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt). In
consequence localization affects forms and tables automatically.

These automatic lookups use `getNormalized`.

You can switch off this behaviour by specifying labels and headers explicitly.

## Timeline

### Possible Improvements

#### Android Integration

It would be possible (I think) to integrate the stack translation into the Android frontends. Android has the string
files anyway, but they are cumbersome and cannot be changed once the application is compiled (I think) and that's not
good.

#### Namespaces

Introduce a variable binding approach like CSS styles do. This would be like:

```kotlin
val myStrings by stringStoreExtension(MyStrings())

open class MyStrings : ZkStringStore(namespace = "my.strings") {
    open val myString by "My String"
}
```

With this pattern it would be possible to have string namespaces, so elements may have their own set of strings.

### Changes

* 2021.5.15
    * introduction of normalized keys
    * documentation update