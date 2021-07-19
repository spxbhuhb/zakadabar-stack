# Strings

In Zakadabar the "static" strings your application presents to the users come from a string store. Think of labels on
buttons, table headers, form field labels, or strings in an Excel file you generate on the backend and send in an
e-mail.

Most string stores extend [ZkBuiltinStrings](/core/core-core/src/commonMain/kotlin/zakadabar/stack/resources/ZkBuiltinStrings.kt),
which in turn is an extension of [ZkStringStore](/core/core-core/src/commonMain/kotlin/zakadabar/stack/resources/ZkStringStore.kt).

The built-in string store contains strings the stack uses by itself, for example label of the "back" buttons, so it is a
good practice to extend it when you write your own string store.

String stores are in `commonMain`. The reason behind this is, that this way they are accessible from all code which we
really want.

## Use a String Store

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="strings and stringStore">

If you follow the conventions, the variable `strings` and `stringStore`
in [ZkApplication.kt](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt) will be the exact same
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

Use any non-string object to get the value for its normalized class name.

```kotlin
strings.getNormalized(MyElement()) // same as getNormalized("MyElement")
```

You can easily translate class names into strings with the `translate` method:

```kotlin
translate<MyElement>() // same as getNormalized("MyElement")
```


## Write a String Store [source code](/lib/examples/src/commonMain/kotlin/zakadabar/lib/examples/resources/ExamplesStrings.kt)

1. Extend [ZkBuiltinStrings](/core/core-core/src/commonMain/kotlin/zakadabar/stack/resources/ZkBuiltinStrings.kt). Override anything
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

Use the [lib:i18n](/doc/guides/plug-and-play/i18n/Introduction.md) plug-and-play module to provide localized strings.

### Browser Frontend

The locale of the application is stored in `application.locale`. This field is initialized by `initLocale` from the URL
of the browser window. For more information see [Introduction](../browser/Introduction.md).

Your string store (both `application.stringStore` and `strings`, if you followed the convention) will store the
localized strings, so everything shows up in the current language.

## Automatic Binding

### Browser

Form, tables, navigation components can automatically look up the labels and headers, based on the name of the property
or target. This uses the [stringStore](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt). In
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

* 2021.5.30
    * introduction of lib:i18n
    * remove localization backends from core  
* 2021.5.15
    * introduction of normalized keys
    * documentation update