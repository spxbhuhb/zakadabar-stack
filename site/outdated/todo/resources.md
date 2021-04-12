This directory contains resources the application uses:

* [Strings](Strings.kt) - text resources, subject to I18N
* [Icons](Icons.kt) - build-in icons from Material Icons
* [MaterialColors](MaterialColors.kt) - colors from the Material palette
* [Theme](ZkTheme.kt) - theme for the UI, colors and such

**Status**

Strings usage pattern won't change.

Icons and the theme is something we'll change into something better more conceptual.

## Strings

**IMPORTANT** When adding strings use `by`. I18n and automatic labels doesn't work with `=`.

**IMPORTANT** Remember to set Application.stringMap in `main.kt`.

The basic concept of the string implementation is very similar to the one Android uses. Define the string resources
somewhere collected and let the application use only references to the resources. Only we Kotlin instead of XML.

In [Strings.kt](Strings.kt) you will find the [StringImpl](Strings.kt)
class. This class plays with the property delegation feature of Kotlin such a way that:

* you can use the properties of the class
* there is a map that stores the values, so
  * I18N can easily load translations
  * form and table can automatically set labels and fields based on property name

You usually want to extend the `StringImpl` like the demo does:
[DemoStringImpl](../../../../../../../../demo/src/jsMain/kotlin/zakadabar/demo/frontend/resources/Strings.kt).

```kotlin
val Strings = DemoStringsImpl()

class DemoStringsImpl : StringsImpl() {
  val tortuga by "Torguga"
}
```

Then use the extended class in your code:

```kotlin
object Tortuga : ZkPage() {

  override fun init() = build {
    + Strings.tortuga
  }

}
```

### Forms and tables

Forms and tables set the field label / header label automatically from the field name whenever possible (for custom
columns it is not).

So, this table definition will have proper, translated header names because
the [Strings](../../../../../../../../demo/src/jsMain/kotlin/zakadabar/demo/frontend/resources/Strings.kt)
of demo has the `id`, `description`, `value` and `actions` (some of these are defined in the core module).

```kotlin
class Table : ZkTable<SpeedDto>() {

  init {
    title = Strings.speeds
    onCreate = { Speeds.openCreate() }

    + SpeedDto::id
    + SpeedDto::description
    + SpeedDto::value
    + SpeedDto::id.actions(Speeds)
  }

}
```

This functionality uses `Application.stringMap` to look up the names by the name of the property.

Set `Application.stringMap` in [main.kt](../../../../../../../../demo/src/jsMain/kotlin/main.kt):

```kotlin
with(Application) {

  // Set string map for automatic form label / table header lookup.

  stringMap = Strings.map

}
```
