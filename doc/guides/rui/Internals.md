# Internals

## Transform Algorithm

The `rui` plugin creates a rui component class for each function annotated with `@Rui`.

Transformation starts with `RuiFunctionTransformer`. The transformer:

1. Creates a `RuiClassBuilder`. This builder creates new `IrClass` named `Rui<funcition-name>` that extends `RuiComponentBase`.
2. Runs `RuiBoundaryVisitor` to find the boundary between the state definition and the rendering parts of the original function.
3. Runs `RuiStateDefinitionVisitor` to transform the state definition into state variables and a constructor.
4. Runs `RuiRenderingVisitor` to transform the rendering into slots, create, patch and destroy functions.


## OBSOLETED

### Reactive Component Classes

The plugin creates a class named `Reactive$<name-of-function>` for each function
annotated with `@Reactive`.

Also, each call to reactive functions creates a reactive component instance (a
child component). The parent component manages the child:

- create it during initialization of the parent
- patch it when necessary
- dispose it when the parent component is disposed

The plugin generates the code for child component management.

```kotlin
@Reactive
fun A(value : Int) {
    B(value + 2) // B is also a reactive component    
}

@Reactive
fun B(value : Int) {
    // ...
}
```

is transformed into:

```kotlin
class ReactiveA(
    var value : Int
) : ReactiveComponent() {

    val b0 = ReactiveB(value + 2)

    override fun patch(mask : Array<Int>) {
        dirty = mask // save the mask for higher-order functions (more about this below)
        if (mask[0] and 1 != 0) { // if A.value has been changed
            b0.value = value + 2 // calculate and set B.value
            b0.patch(arrayOf(1)) // patch B, so it will apply the new value wherever it is necessary
        }
    }
    
    override fun dispose() {
        b0.dispose()
    }
}
```

### Parameter Variables

```kotlin
fun A(p1 : String, p2 : Int) {
    // ...
}
```

Is transformed into:

```kotlin
fun A(p1 : String, p2 : Int) {
    var `p1$0` = p1 // mask = 1
    var `p2$0` = p2 // mask = 2
}
```

#### Details

This strategy means an overhead but it is necessary. Let's analyse the following code fragment:

```kotlin
@Reactive
fun A() {
    var counter = 0
    B(counter)
    onClick { counter++ }
}

@Reactive
fun B() {
    Text(counter)
}
```

B is compiled into something like this, introducing an internal variable for the counter.

```kotlin
fun B(counter : Int) : Component {
    // ...
    var counter0 = counter
    // ...
}
```

We need a `var` instead of `val` because in case the origin variable changes in
`A` the patch method of `B` will be called. That patch method has to get the
value from somewhere and that somewhere has to be writable.

Higher order functions makes data handling even more complex, as the blocks passed
to the higher order function has to reach up to the original closure to access
the data.

Svelte uses a `ctx` to store the data but in Kotlin a general array wouldn't
keep the types. So, it seems like a better idea to define the variables one-by-one.

### Framework Variables

The following framework variable declarations are added to the body:

```kotlin
val `self$0` = Component()
val `dirty$0` = arrayOf(0)
```

`self$0` is the `Component` instance that holds the closure together and stores
the component methods, so they can be accessed from outside the closure.

`dirty$0` is mask that represents which variables of the component changed.
The patch method uses dirty to patch the component with the changed variables.
Patch also clears dirty.

#### Details

We store the dirty mask in the closure to let the blocks passed to higher order
functions access it easily. Consider the following code:

```kotlin
@Suppress("CanBeVal")
fun A() {
    var v0 = 1
    B {
        var v1 = 2
        C {
            var v2 = 3
            D(v0, v1, v2)
        }
    }
}
```

We have to call `D.patch` whenever any of the variables change. This is not
trivial because there are two higher order functions between the closure of A 
and the closure C. Instead of trying to pass down the dirty masks in the 
patch calls we've decided to use the closures.

The code above is compiled so that each closure has its own dirty mask variable:

- `dirty$0` for A
- `dirty$1` for the block passed to B
- `dirty$2` for the block passed to C
- `dirty$3` for D

So, the block passed to C can actually check:

- `dirty$0` to see if `v0` has been changed
- `dirty$1` to see if `v1` has been changed
- `dirty$2` to see if `v2` has been changed

Then it has to update the appropriate 


#### IR

```kotlin
import zakadabar.reactive.core.ReactiveComponent

class ReactivePrimitive(
    var value: Int
) : ReactiveComponent() {

    init {
        println("================    Primitive.init: " + value)
    }

    override fun patch(mask: Array<Int>) {
        println("================    Primitive.patch: " + mask[0] + " " + value)
    }
}
```

```text
MODULE_FRAGMENT name:<main>
  FILE fqName:<root> fileName:/Users/tiz/src/zakadabar-stack/plugin/reactive/reactive-kotlin-plugin/./tmp/sources/pluginTest.kt
  
    CLASS CLASS name:ReactiveText modality:FINAL visibility:public superTypes:[zakadabar.reactive.core.ReactiveComponent]
  
      $this: VALUE_PARAMETER INSTANCE_RECEIVER name:<this> type:<root>.ReactiveText
  
      CONSTRUCTOR visibility:public <> () returnType:<root>.ReactiveText [primary]
        BLOCK_BODY
          DELEGATING_CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in zakadabar.reactive.core.ReactiveComponent'
          INSTANCE_INITIALIZER_CALL classDescriptor='CLASS CLASS name:ReactiveText modality:FINAL visibility:public superTypes:[zakadabar.reactive.core.ReactiveComponent]'
  
      PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open dirty: kotlin.Array<kotlin.Int> [var]
        FUN FAKE_OVERRIDE name:<get-dirty> visibility:public modality:OPEN <> ($this:zakadabar.reactive.core.ReactiveComponent) returnType:kotlin.Array<kotlin.Int> [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-dirty> (): kotlin.Array<kotlin.Int> declared in zakadabar.reactive.core.ReactiveComponent
          $this: VALUE_PARAMETER name:<this> type:zakadabar.reactive.core.ReactiveComponent
        FUN FAKE_OVERRIDE name:<set-dirty> visibility:public modality:OPEN <> ($this:zakadabar.reactive.core.ReactiveComponent, <set-?>:kotlin.Array<kotlin.Int>) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-dirty> (<set-?>: kotlin.Array<kotlin.Int>): kotlin.Unit declared in zakadabar.reactive.core.ReactiveComponent
          $this: VALUE_PARAMETER name:<this> type:zakadabar.reactive.core.ReactiveComponent
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Array<kotlin.Int>
          
      FUN FAKE_OVERRIDE name:dispose visibility:public modality:OPEN <> ($this:zakadabar.reactive.core.ReactiveComponent) returnType:kotlin.Unit [fake_override]
        overridden:
          public open fun dispose (): kotlin.Unit declared in zakadabar.reactive.core.ReactiveComponent
        $this: VALUE_PARAMETER name:<this> type:zakadabar.reactive.core.ReactiveComponent
        
      FUN FAKE_OVERRIDE name:equals visibility:public modality:OPEN <> ($this:kotlin.Any, other:kotlin.Any?) returnType:kotlin.Boolean [fake_override,operator]
        overridden:
          public open fun equals (other: kotlin.Any?): kotlin.Boolean [fake_override,operator] declared in zakadabar.reactive.core.ReactiveComponent
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any
        VALUE_PARAMETER name:other index:0 type:kotlin.Any?
        
      FUN FAKE_OVERRIDE name:hashCode visibility:public modality:OPEN <> ($this:kotlin.Any) returnType:kotlin.Int [fake_override]
        overridden:
          public open fun hashCode (): kotlin.Int [fake_override] declared in zakadabar.reactive.core.ReactiveComponent
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any
        
      FUN FAKE_OVERRIDE name:patch visibility:public modality:OPEN <> ($this:zakadabar.reactive.core.ReactiveComponent, mask:kotlin.Array<kotlin.Int>) returnType:kotlin.Unit [fake_override]
        overridden:
          public open fun patch (mask: kotlin.Array<kotlin.Int>): kotlin.Unit declared in zakadabar.reactive.core.ReactiveComponent
        $this: VALUE_PARAMETER name:<this> type:zakadabar.reactive.core.ReactiveComponent
        VALUE_PARAMETER name:mask index:0 type:kotlin.Array<kotlin.Int>
        
      FUN FAKE_OVERRIDE name:toString visibility:public modality:OPEN <> ($this:kotlin.Any) returnType:kotlin.String [fake_override]
        overridden:
          public open fun toString (): kotlin.String [fake_override] declared in zakadabar.reactive.core.ReactiveComponent
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any

```


ANONYMOUS_INITIALIZER isStatic=false
BLOCK_BODY
CALL 'public final fun <set-count> (<set-?>: kotlin.Int): kotlin.Unit declared in <root>.ReactiveTextManual' type=kotlin.Unit origin=EQ
$this: GET_VAR '<this>: <root>.ReactiveTextManual declared in <root>.ReactiveTextManual' type=<root>.ReactiveTextManual origin=null
<set-?>: CONST Int type=kotlin.Int value=0

```text

    VAR name:count type:kotlin.Int [var]
          CONST Int type=kotlin.Int value=0
          
    PROPERTY name:count visibility:public modality:FINAL [var]
        FIELD PROPERTY_BACKING_FIELD name:count type:kotlin.Int visibility:private
          EXPRESSION_BODY
            CONST Int type=kotlin.Int value=0
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-count> visibility:public modality:FINAL <> ($this:<root>.ReactiveTextManual) returnType:kotlin.Int
          correspondingProperty: PROPERTY name:count visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:<root>.ReactiveTextManual
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-count> (): kotlin.Int declared in <root>.ReactiveTextManual'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:count type:kotlin.Int visibility:private' type=kotlin.Int origin=null
                receiver: GET_VAR '<this>: <root>.ReactiveTextManual declared in <root>.ReactiveTextManual.<get-count>' type=<root>.ReactiveTextManual origin=null
        FUN DEFAULT_PROPERTY_ACCESSOR name:<set-count> visibility:public modality:FINAL <> ($this:<root>.ReactiveTextManual, <set-?>:kotlin.Int) returnType:kotlin.Unit
          correspondingProperty: PROPERTY name:count visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:<root>.ReactiveTextManual
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
          BLOCK_BODY
            SET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:count type:kotlin.Int visibility:private' type=kotlin.Unit origin=null
              receiver: GET_VAR '<this>: <root>.ReactiveTextManual declared in <root>.ReactiveTextManual.<set-count>' type=<root>.ReactiveTextManual origin=null
              value: GET_VAR '<set-?>: kotlin.Int declared in <root>.ReactiveTextManual.<set-count>' type=kotlin.Int origin=null

```