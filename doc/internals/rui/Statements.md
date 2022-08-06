# Statements

A Rui rendering consists of rendering statements. Statement types:

* Block
* When
* Loop
* Call
* Higher-order Calls

Processing the statements has two phases:

1. All statements are lowered.
2. All statements are transformed into IR.

Lowering may change the type of the statement. Most notably, a block that
contains a single statement is lowered into that statement.

## Scopes

Loops and higher order calls define a new "scope". This is because these two
may execute the block or function more than once. This is trivial for loops,
but also, higher order calls may use the passed function more than once.

From compilation point of view, a new scope needs a new class to contain
the fragments inside the scope. These must not be mixed with the fragments
of the outer scope.

## Block

A `block` is a sequence of statements:

```kotlin
{
    T1(v0)
    T0()
}
```

### Lowering

1. First, all statements of the block is lowered.
2. When the block does not contain any statements is lowered into a call to `RuiEmptyFragment`.
3. When the block contains only one statement is lowered into that one statement.

### Transform

Block transform happens only when the block contains more than one statement.

The transform:

1. Adds a `val` property to the scope. Initialized as `object` that extends `RuiBlock`. 
   1. In the `object`:
      1. Adds and initializes a `val` property for each statement.
      2. Adds an override of the `fragments` array, initialized to store the properties of statements.
      3. Adds an override of the `ruiPatch` method.
         1. In `ruiPatch`:
            1. Adds the external patch instructions for the statement as needed.

### Example

```kotlin
val fragment0 = object : RuiBlock(ruiAdapter) {

    val ruiT10 = RuiT1(ruiAdapter, v0)
    val ruiT01 = RuiT0(ruiAdapter)

    override val fragments: Array<RuiFragment> = arrayOf(
        ruiT10,
        ruiT01
    )

    override fun ruiPatch() {
        if (ruiDirty0 and 1 != 0) {
            ruiT10.p0 = v0
            ruiT10.ruiInvalidate0(1)
            ruiT10.ruiPatch()
        }
    }
}
```

## When

A `when` is a branching statement that covers both Kotlin `if` and `when`.

```kotlin
if (v0 == 1) T0()

if (v0 == 2) {
    T0()
}

if (v0 == 3) T0() else T0()

when (v0) {
    1 -> T0()
    2 -> T0()
}

when(v0) {
    1 -> T0()
    else -> T0()
}
```

Each version results in a list of (condition,statement) pairs.

### Lowering

1. Each the statement is lowered according its type.

### Transform 

1. Adds a `val` property to the scope. Initialized as `object` that extends `RuiWhen`.
    1. In the `object`:
        1. Adds and initializes a `val` property for each branch statement.
        2. Adds an override of the `ruiSelect` function, representing the original condition.

### Example

```kotlin
val fragment0 = object : RuiWhen(ruiAdapter) {

    override var ruiFragment = ruiSelect()

    override fun ruiSelect(): RuiFragment =
        when (v0) {
            1 -> ruiBranch3
            2 -> ruiBranch4
            else -> ruiBranch5
        }

    val ruiBranch3 = object : RuiT1(ruiAdapter, v0 + 10) {
        override fun ruiPatch() {
            if (ruiDirty0 and 1 != 0) {
                this.p0 = v0 + 10
                ruiInvalidate0(1)
            }
            super.ruiPatch()
        }
    }

    val ruiBranch4 = object : RuiT1(ruiAdapter, v0 + 20) {
        override fun ruiPatch() {
            if (ruiDirty0 and 1 != 0) {
                this.p0 = v0 + 20
                ruiInvalidate0(1)
            }
            super.ruiPatch()
        }
    }

    val ruiBranch5 = RuiEmptyFragment(ruiAdapter)

}
```

## Loop

A loop calls the specified block repeatedly as long as the iterator instructs so.
This is a higher-order call in the context of Rui because the block of the loop
statement generates components.

```kotlin
for (i in 1..10) {
   T1(i)
   T1(v0) 
}
```

### Lowering

1. The loop block is lowered according its type.

### Transform

1. Adds a `val` property to the scope. Initialized as `object` that extends `RuiLoop`.
   1. The type parameter of the object is the type parameter of the loop iterator.
   2. In the `object`:
      1. Adds an override of the `makeIterator` function. This creates a new iterator for the loop. 
      2. Adds an override of the `makeFragment` function. This generates a fragment for one iteration step.
    
### Example

```kotlin
val fragment0 = object : RuiLoop<Int>(ruiAdapter) {

    override fun makeIterator(): Iterator<Int> = IntRange(0, 10).iterator()

    override fun makeFragment() = object : RuiBlock(ruiAdapter) {

        val ruiT10 = RuiT1(ruiAdapter, loopVariable!!)
        val ruiT01 = RuiT1(ruiAdapter, v0)

        override val fragments: Array<RuiFragment> = arrayOf(
            ruiT10,
            ruiT01
        )

        override fun ruiPatch() {
            if (ruiT10.p0 != loopVariable) {
                ruiT10.p0 = loopVariable!!
                ruiT10.ruiInvalidate0(1)
            }
            if (ruiDirty0 and 1 != 0) {
                ruiT01.p0 = v0
                ruiT01.ruiInvalidate0(1)
            }
            super.ruiPatch()
        }

    }
}
```