This directory contains resources the application uses:

* [Strings](Strings.kt) - text resources, subject to I18N
* [Icons](Icons.kt) - build-in icons from Material Icons
* [Theme][ZkTheme.kt] - theme for the UI, colors and such

**NOTE**

This module is unfinished, it is quite confused at the moment. Strings are close to the final state, the missing thing is the internationalization but that should not change the use pattern (namely,
just use the field).

Icons and the theme is something we'll change into something better more conceptual.

## Strings

The basic concept of the string implementation is very similar to the one Android uses: define the string resources somewhere collected and let the application use only references to the resources. We
do not use XML but Kotlin source.

In [Strings.kt](Strings.kt) you will find the [StringImpl](Strings.kt)
class. This class plays with the property delegation feature of Kotlin such a way that:

* in the code you can use the property of the class
* there is a map that stores the values, so
  * I18N can easily load translations
  * form and table can find values easily by property name

You usually want to extend the `StringImpl` like the demo does:
[demo.Strings](../../../../../../../../demo/src/jsMain/kotlin/zakadabar/demo/frontend/resources/Strings.kt).

```kotlin
val Strings = DemoStringsImpl()

class DemoStringsImpl : StringsImpl() {
  val tortuga by string("Torguga")
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

Forms and tables set the field label / header label automatically from the field name whenever possible (for custom columns it is not).

So, this table definition will have proper, translated header names because the `Strings` of demo has the `id`, `description`, `value` and `actions`.

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
