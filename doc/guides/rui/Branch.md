```kotlin
fun a(b : Int) {
    if (b == 8) Unit

    if (b == 9) Unit else Unit

    when {
        b == 1 -> Unit
        b == 2 -> Unit
    }

    when {
        b == 3 -> Unit
        b == 4 -> Unit
        else -> Unit
    }

    when (b) {
        5 -> Unit
    }

}
```

```text
DUMP BEFORE
MODULE_FRAGMENT name:<main>
  FILE fqName:zakadabar.rui.kotlin.plugin.adhoc fileName:/Users/tiz/src/zakadabar-stack/plugin/rui/rui-kotlin-plugin/src/test/kotlin/zakadabar/rui/kotlin/plugin/adhoc/Adhoc.kt
    CLASS CLASS name:Branch modality:FINAL visibility:public superTypes:[zakadabar.rui.runtime.RuiGeneratedFragment<zakadabar.rui.runtime.testing.TestNode>]
      annotations:
        Suppress(names = ['JoinDeclarationAndAssignment', 'unused'])
      $this: VALUE_PARAMETER INSTANCE_RECEIVER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
      CONSTRUCTOR visibility:public <> (ruiAdapter:zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode>) returnType:zakadabar.rui.kotlin.plugin.adhoc.Branch [primary]
        VALUE_PARAMETER name:ruiAdapter index:0 type:zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode>
        BLOCK_BODY
          DELEGATING_CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in kotlin.Any'
          INSTANCE_INITIALIZER_CALL classDescriptor='CLASS CLASS name:Branch modality:FINAL visibility:public superTypes:[zakadabar.rui.runtime.RuiGeneratedFragment<zakadabar.rui.runtime.testing.TestNode>]'
      PROPERTY name:ruiAdapter visibility:public modality:OPEN [val]
        overridden:
          public abstract ruiAdapter: zakadabar.rui.runtime.RuiAdapter<BT of zakadabar.rui.runtime.RuiGeneratedFragment> [fake_override,val]
        FIELD PROPERTY_BACKING_FIELD name:ruiAdapter type:zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> visibility:private [final]
          EXPRESSION_BODY
            GET_VAR 'ruiAdapter: zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<init>' type=zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> origin=INITIALIZE_PROPERTY_FROM_PARAMETER
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-ruiAdapter> visibility:public modality:OPEN <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode>
          correspondingProperty: PROPERTY name:ruiAdapter visibility:public modality:OPEN [val]
          overridden:
            public abstract fun <get-ruiAdapter> (): zakadabar.rui.runtime.RuiAdapter<BT of zakadabar.rui.runtime.RuiGeneratedFragment> [fake_override] declared in zakadabar.rui.runtime.RuiGeneratedFragment
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public open fun <get-ruiAdapter> (): zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:ruiAdapter type:zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> visibility:private [final]' type=zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<get-ruiAdapter>' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
      PROPERTY name:ruiParent visibility:public modality:OPEN [val]
        overridden:
          public abstract ruiParent: zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.RuiGeneratedFragment>? [fake_override,val]
        FIELD PROPERTY_BACKING_FIELD name:ruiParent type:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>? visibility:private [final]
          EXPRESSION_BODY
            CONST Null type=kotlin.Nothing? value=null
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-ruiParent> visibility:public modality:OPEN <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>?
          correspondingProperty: PROPERTY name:ruiParent visibility:public modality:OPEN [val]
          overridden:
            public abstract fun <get-ruiParent> (): zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.RuiGeneratedFragment>? [fake_override] declared in zakadabar.rui.runtime.RuiGeneratedFragment
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public open fun <get-ruiParent> (): zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>? declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:ruiParent type:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>? visibility:private [final]' type=zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>? origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<get-ruiParent>' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
      PROPERTY name:ruiExternalPatch visibility:public modality:OPEN [val]
        overridden:
          public abstract ruiExternalPatch: kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.RuiGeneratedFragment>, kotlin.Unit> [fake_override,val]
        FIELD PROPERTY_BACKING_FIELD name:ruiExternalPatch type:kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>, kotlin.Unit> visibility:private [final]
          EXPRESSION_BODY
            FUN_EXPR type=kotlin.Function1<zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>, kotlin.Unit> origin=LAMBDA
              FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> (it:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>) returnType:kotlin.Unit
                VALUE_PARAMETER name:it index:0 type:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>
                BLOCK_BODY
                  RETURN type=kotlin.Nothing from='local final fun <anonymous> (it: zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiExternalPatch'
                    GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-ruiExternalPatch> visibility:public modality:OPEN <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>, kotlin.Unit>
          correspondingProperty: PROPERTY name:ruiExternalPatch visibility:public modality:OPEN [val]
          overridden:
            public abstract fun <get-ruiExternalPatch> (): kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.RuiGeneratedFragment>, kotlin.Unit> [fake_override] declared in zakadabar.rui.runtime.RuiGeneratedFragment
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public open fun <get-ruiExternalPatch> (): kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>, kotlin.Unit> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:ruiExternalPatch type:kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>, kotlin.Unit> visibility:private [final]' type=kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>, kotlin.Unit> origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<get-ruiExternalPatch>' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
      PROPERTY name:fragment visibility:public modality:OPEN [val]
        overridden:
          public abstract fragment: zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.RuiGeneratedFragment> [val]
        FIELD PROPERTY_BACKING_FIELD name:fragment type:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> visibility:private [final]
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-fragment> visibility:public modality:OPEN <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>
          correspondingProperty: PROPERTY name:fragment visibility:public modality:OPEN [val]
          overridden:
            public abstract fun <get-fragment> (): zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.RuiGeneratedFragment> declared in zakadabar.rui.runtime.RuiGeneratedFragment
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public open fun <get-fragment> (): zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:fragment type:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> visibility:private [final]' type=zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<get-fragment>' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
      PROPERTY name:v0 visibility:public modality:FINAL [var]
        FIELD PROPERTY_BACKING_FIELD name:v0 type:kotlin.Int visibility:private
          EXPRESSION_BODY
            CONST Int type=kotlin.Int value=1
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-v0> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:kotlin.Int
          correspondingProperty: PROPERTY name:v0 visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-v0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:v0 type:kotlin.Int visibility:private' type=kotlin.Int origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<get-v0>' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
        FUN DEFAULT_PROPERTY_ACCESSOR name:<set-v0> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch, <set-?>:kotlin.Int) returnType:kotlin.Unit
          correspondingProperty: PROPERTY name:v0 visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
          BLOCK_BODY
            SET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:v0 type:kotlin.Int visibility:private' type=kotlin.Unit origin=null
              receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<set-v0>' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              value: GET_VAR '<set-?>: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<set-v0>' type=kotlin.Int origin=null
      PROPERTY name:ruiDirty0 visibility:public modality:FINAL [var]
        FIELD PROPERTY_BACKING_FIELD name:ruiDirty0 type:kotlin.Int visibility:private
          EXPRESSION_BODY
            CONST Int type=kotlin.Int value=0
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-ruiDirty0> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:kotlin.Int
          correspondingProperty: PROPERTY name:ruiDirty0 visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-ruiDirty0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:ruiDirty0 type:kotlin.Int visibility:private' type=kotlin.Int origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<get-ruiDirty0>' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
        FUN DEFAULT_PROPERTY_ACCESSOR name:<set-ruiDirty0> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch, <set-?>:kotlin.Int) returnType:kotlin.Unit
          correspondingProperty: PROPERTY name:ruiDirty0 visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
          BLOCK_BODY
            SET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:ruiDirty0 type:kotlin.Int visibility:private' type=kotlin.Unit origin=null
              receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<set-ruiDirty0>' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              value: GET_VAR '<set-?>: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.<set-ruiDirty0>' type=kotlin.Int origin=null
      FUN name:ruiInvalidate0 visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch, mask:kotlin.Int) returnType:kotlin.Unit
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
        VALUE_PARAMETER name:mask index:0 type:kotlin.Int
        BLOCK_BODY
          CALL 'public final fun <set-ruiDirty0> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Unit origin=EQ
            $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiInvalidate0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
            <set-?>: CALL 'public final fun or (other: kotlin.Int): kotlin.Int [infix] declared in kotlin.Int' type=kotlin.Int origin=null
              $this: CALL 'public final fun <get-ruiDirty0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Int origin=GET_PROPERTY
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiInvalidate0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              other: GET_VAR 'mask: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiInvalidate0' type=kotlin.Int origin=null
      FUN name:ruiEp0 visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch, it:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>) returnType:kotlin.Unit
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
        VALUE_PARAMETER name:it index:0 type:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>
        BLOCK_BODY
          TYPE_OP type=kotlin.Unit origin=IMPLICIT_COERCION_TO_UNIT typeOperand=kotlin.Unit
            TYPE_OP type=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode> origin=CAST typeOperand=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode>
              GET_VAR 'it: zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp0' type=zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> origin=null
          WHEN type=kotlin.Unit origin=IF
            BRANCH
              if: CALL 'public final fun not (): kotlin.Boolean [operator] declared in kotlin.Boolean' type=kotlin.Boolean origin=EXCLEQ
                $this: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EXCLEQ
                  arg0: CALL 'public final fun and (other: kotlin.Int): kotlin.Int [infix] declared in kotlin.Int' type=kotlin.Int origin=null
                    $this: CALL 'public final fun <get-ruiDirty0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Int origin=GET_PROPERTY
                      $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                    other: CONST Int type=kotlin.Int value=1
                  arg1: CONST Int type=kotlin.Int value=0
              then: BLOCK type=kotlin.Unit origin=null
                CALL 'public final fun <set-p0> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.runtime.testing.RuiT1' type=kotlin.Unit origin=EQ
                  $this: TYPE_OP type=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode> origin=IMPLICIT_CAST typeOperand=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode>
                    GET_VAR 'it: zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp0' type=zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> origin=null
                  <set-?>: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                    $this: CALL 'public final fun <get-v0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Int origin=GET_PROPERTY
                      $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                    other: CONST Int type=kotlin.Int value=10
                CALL 'public final fun ruiInvalidate0 (mask: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Unit origin=null
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                  mask: CONST Int type=kotlin.Int value=1
      FUN name:ruiEp1 visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch, it:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>) returnType:kotlin.Unit
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
        VALUE_PARAMETER name:it index:0 type:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>
        BLOCK_BODY
          TYPE_OP type=kotlin.Unit origin=IMPLICIT_COERCION_TO_UNIT typeOperand=kotlin.Unit
            TYPE_OP type=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode> origin=CAST typeOperand=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode>
              GET_VAR 'it: zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp1' type=zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> origin=null
          WHEN type=kotlin.Unit origin=IF
            BRANCH
              if: CALL 'public final fun not (): kotlin.Boolean [operator] declared in kotlin.Boolean' type=kotlin.Boolean origin=EXCLEQ
                $this: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EXCLEQ
                  arg0: CALL 'public final fun and (other: kotlin.Int): kotlin.Int [infix] declared in kotlin.Int' type=kotlin.Int origin=null
                    $this: CALL 'public final fun <get-ruiDirty0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Int origin=GET_PROPERTY
                      $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp1' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                    other: CONST Int type=kotlin.Int value=1
                  arg1: CONST Int type=kotlin.Int value=0
              then: BLOCK type=kotlin.Unit origin=null
                CALL 'public final fun <set-p0> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.runtime.testing.RuiT1' type=kotlin.Unit origin=EQ
                  $this: TYPE_OP type=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode> origin=IMPLICIT_CAST typeOperand=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode>
                    GET_VAR 'it: zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp1' type=zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> origin=null
                  <set-?>: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                    $this: CALL 'public final fun <get-v0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Int origin=GET_PROPERTY
                      $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp1' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                    other: CONST Int type=kotlin.Int value=20
                CALL 'public final fun ruiInvalidate0 (mask: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Unit origin=null
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiEp1' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                  mask: CONST Int type=kotlin.Int value=1
      FUN name:ruiBranch0 visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
        BLOCK_BODY
          RETURN type=kotlin.Nothing from='public final fun ruiBranch0 (): zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
            CONSTRUCTOR_CALL 'public constructor <init> (ruiAdapter: zakadabar.rui.runtime.RuiAdapter<BT of zakadabar.rui.runtime.testing.RuiT1>, ruiParent: zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.testing.RuiT1>?, ruiExternalPatch: kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.testing.RuiT1>, kotlin.Unit>, p0: kotlin.Int) [primary] declared in zakadabar.rui.runtime.testing.RuiT1' type=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode> origin=null
              <class: BT>: zakadabar.rui.runtime.testing.TestNode
              ruiAdapter: CALL 'public open fun <get-ruiAdapter> (): zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> origin=GET_PROPERTY
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              ruiParent: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              ruiExternalPatch: FUNCTION_REFERENCE 'public final fun ruiEp0 (it: zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.reflect.KFunction1<zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>, kotlin.Unit> origin=null reflectionTarget=<same>
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              p0: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                $this: CALL 'public final fun <get-v0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Int origin=GET_PROPERTY
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch0' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                other: CONST Int type=kotlin.Int value=10
      FUN name:ruiBranch1 visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
        BLOCK_BODY
          RETURN type=kotlin.Nothing from='public final fun ruiBranch1 (): zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
            CONSTRUCTOR_CALL 'public constructor <init> (ruiAdapter: zakadabar.rui.runtime.RuiAdapter<BT of zakadabar.rui.runtime.testing.RuiT1>, ruiParent: zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.testing.RuiT1>?, ruiExternalPatch: kotlin.Function1<@[ParameterName(name = 'it')] zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.testing.RuiT1>, kotlin.Unit>, p0: kotlin.Int) [primary] declared in zakadabar.rui.runtime.testing.RuiT1' type=zakadabar.rui.runtime.testing.RuiT1<zakadabar.rui.runtime.testing.TestNode> origin=null
              <class: BT>: zakadabar.rui.runtime.testing.TestNode
              ruiAdapter: CALL 'public open fun <get-ruiAdapter> (): zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> origin=GET_PROPERTY
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch1' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              ruiParent: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch1' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              ruiExternalPatch: FUNCTION_REFERENCE 'public final fun ruiEp1 (it: zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.reflect.KFunction1<zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>, kotlin.Unit> origin=null reflectionTarget=<same>
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch1' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              p0: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                $this: CALL 'public final fun <get-v0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Int origin=GET_PROPERTY
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch1' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                other: CONST Int type=kotlin.Int value=20
      FUN name:ruiBranch2 visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
        BLOCK_BODY
          RETURN type=kotlin.Nothing from='public final fun ruiBranch2 (): zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
            CONSTRUCTOR_CALL 'public constructor <init> (ruiAdapter: zakadabar.rui.runtime.RuiAdapter<BT of zakadabar.rui.runtime.RuiPlaceholder>) [primary] declared in zakadabar.rui.runtime.RuiPlaceholder' type=zakadabar.rui.runtime.RuiPlaceholder<zakadabar.rui.runtime.testing.TestNode> origin=null
              <class: BT>: zakadabar.rui.runtime.testing.TestNode
              ruiAdapter: CALL 'public open fun <get-ruiAdapter> (): zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> origin=GET_PROPERTY
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiBranch2' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
      FUN name:ruiSelect visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.Branch) returnType:kotlin.Int
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.Branch
        BLOCK_BODY
          RETURN type=kotlin.Nothing from='public final fun ruiSelect (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch'
            BLOCK type=kotlin.Int origin=WHEN
              VAR IR_TEMPORARY_VARIABLE name:tmp0_subject type:kotlin.Int [val]
                CALL 'public final fun <get-v0> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.Int origin=GET_PROPERTY
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiSelect' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              WHEN type=kotlin.Int origin=WHEN
                BRANCH
                  if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
                    arg0: GET_VAR 'val tmp0_subject: kotlin.Int [val] declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiSelect' type=kotlin.Int origin=null
                    arg1: CONST Int type=kotlin.Int value=1
                  then: CONST Int type=kotlin.Int value=0
                BRANCH
                  if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
                    arg0: GET_VAR 'val tmp0_subject: kotlin.Int [val] declared in zakadabar.rui.kotlin.plugin.adhoc.Branch.ruiSelect' type=kotlin.Int origin=null
                    arg1: CONST Int type=kotlin.Int value=2
                  then: CONST Int type=kotlin.Int value=1
                BRANCH
                  if: CONST Boolean type=kotlin.Boolean value=true
                  then: CONST Int type=kotlin.Int value=2
      ANONYMOUS_INITIALIZER isStatic=false
        BLOCK_BODY
          SET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:fragment type:zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> visibility:private [final]' type=kotlin.Unit origin=null
            receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
            value: CONSTRUCTOR_CALL 'public constructor <init> (ruiAdapter: zakadabar.rui.runtime.RuiAdapter<BT of zakadabar.rui.runtime.RuiWhen>, ruiSelect: kotlin.Function0<kotlin.Int>, vararg factories: kotlin.Function0<zakadabar.rui.runtime.RuiFragment<BT of zakadabar.rui.runtime.RuiWhen>>) [primary] declared in zakadabar.rui.runtime.RuiWhen' type=zakadabar.rui.runtime.RuiWhen<zakadabar.rui.runtime.testing.TestNode> origin=null
              <class: BT>: zakadabar.rui.runtime.testing.TestNode
              ruiAdapter: CALL 'public open fun <get-ruiAdapter> (): zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.runtime.RuiAdapter<zakadabar.rui.runtime.testing.TestNode> origin=GET_PROPERTY
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              ruiSelect: FUNCTION_REFERENCE 'public final fun ruiSelect (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.reflect.KFunction0<kotlin.Int> origin=null reflectionTarget=<same>
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
              factories: VARARG type=kotlin.Array<out kotlin.Function0<zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>>> varargElementType=kotlin.Function0<zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>>
                FUNCTION_REFERENCE 'public final fun ruiBranch0 (): zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.reflect.KFunction0<zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>> origin=null reflectionTarget=<same>
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                FUNCTION_REFERENCE 'public final fun ruiBranch1 (): zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.reflect.KFunction0<zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>> origin=null reflectionTarget=<same>
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null
                FUNCTION_REFERENCE 'public final fun ruiBranch2 (): zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode> declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=kotlin.reflect.KFunction0<zakadabar.rui.runtime.RuiFragment<zakadabar.rui.runtime.testing.TestNode>> origin=null reflectionTarget=<same>
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.Branch declared in zakadabar.rui.kotlin.plugin.adhoc.Branch' type=zakadabar.rui.kotlin.plugin.adhoc.Branch origin=null

```