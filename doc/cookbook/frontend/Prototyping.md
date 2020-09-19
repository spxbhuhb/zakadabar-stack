# Prototyping

To create frontend prototypes fast, use the [NYI](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/util/NYI.kt) element.

NYI is just a simple element with a message that act as a placeholder until you write the actual element:

```kotlin
class ProtoElement : ComplexElement() {

    override fun init() : ProtoElement {

        this cssClass coreClasses.column build {
            + NYI("this will be the header")
            + row() build {
                + NYI("first column")
                + NYI("second column")
                + NYI("third column")  
            }
            + NYI("this will be the footer")
        }

        return this
    }

}
```