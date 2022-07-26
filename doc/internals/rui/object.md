## Original Kotlin Code

```kotlin
val ruiBranch3 = object : RuiTestFragment(ruiAdapter, value + 10) {
    override fun ruiPatch() {
        if (ruiDirty0 and 1 != 0) {
            this.value = this@TestBranch.value + 10
            ruiInvalidate0(1)
        }
        super.ruiPatch()
    }
}
```


## IR by the standard compiler

```text
PROPERTY name:ruiBranch3 visibility:public modality:FINAL [val]
  FIELD PROPERTY_BACKING_FIELD name:ruiBranch3 type:zakadabar.rui.runtime.testing.RuiTestFragment visibility:private [final]
    EXPRESSION_BODY
      BLOCK type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> origin=OBJECT_LITERAL
        CLASS CLASS name:<no name provided> modality:FINAL visibility:local superTypes:[zakadabar.rui.runtime.testing.RuiTestFragment]
          $this: VALUE_PARAMETER INSTANCE_RECEIVER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>
          CONSTRUCTOR visibility:public <> () returnType:zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> [primary]
            BLOCK_BODY
              DELEGATING_CONSTRUCTOR_CALL 'public constructor <init> (ruiAdapter: zakadabar.rui.runtime.RuiAdapter, value: kotlin.Int) [primary] declared in zakadabar.rui.runtime.testing.RuiTestFragment'
                ruiAdapter: CALL 'public final fun <get-ruiAdapter> (): zakadabar.rui.runtime.RuiAdapter [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch' type=zakadabar.rui.runtime.RuiAdapter origin=GET_PROPERTY
                  $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.TestBranch declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch origin=null
                value: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                  $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch' type=kotlin.Int origin=GET_PROPERTY
                    $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.TestBranch declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch origin=null
                  other: CONST Int type=kotlin.Int value=10
              INSTANCE_INITIALIZER_CALL classDescriptor='CLASS CLASS name:<no name provided> modality:FINAL visibility:local superTypes:[zakadabar.rui.runtime.testing.RuiTestFragment]'
          FUN name:ruiPatch visibility:public modality:OPEN <> ($this:zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>) returnType:kotlin.Unit
            overridden:
              public open fun ruiPatch (): kotlin.Unit declared in zakadabar.rui.runtime.testing.RuiTestFragment
            $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>
            BLOCK_BODY
              WHEN type=kotlin.Unit origin=IF
                BRANCH
                  if: CALL 'public final fun not (): kotlin.Boolean [operator] declared in kotlin.Boolean' type=kotlin.Boolean origin=EXCLEQ
                    $this: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EXCLEQ
                      arg0: CALL 'public final fun and (other: kotlin.Int): kotlin.Int [infix] declared in kotlin.Int' type=kotlin.Int origin=null
                        $this: CALL 'public final fun <get-ruiDirty0> (): kotlin.Int [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>' type=kotlin.Int origin=GET_PROPERTY
                          $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>.ruiPatch' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> origin=null
                        other: CONST Int type=kotlin.Int value=1
                      arg1: CONST Int type=kotlin.Int value=0
                  then: BLOCK type=kotlin.Unit origin=null
                    CALL 'public final fun <set-value> (<set-?>: kotlin.Int): kotlin.Unit [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>' type=kotlin.Unit origin=EQ
                      $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>.ruiPatch' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> origin=null
                      <set-?>: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                        $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch' type=kotlin.Int origin=GET_PROPERTY
                          $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.TestBranch declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch origin=null
                        other: CONST Int type=kotlin.Int value=10
                    CALL 'public final fun ruiInvalidate0 (mask: kotlin.Int): kotlin.Unit [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>' type=kotlin.Unit origin=null
                      $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>.ruiPatch' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> origin=null
                      mask: CONST Int type=kotlin.Int value=1
              CALL 'public open fun ruiPatch (): kotlin.Unit declared in zakadabar.rui.runtime.testing.RuiTestFragment' superQualifier='CLASS IR_EXTERNAL_DECLARATION_STUB CLASS name:RuiTestFragment modality:OPEN visibility:public superTypes:[zakadabar.rui.runtime.RuiFragment; zakadabar.rui.runtime.testing.WithName]' type=kotlin.Unit origin=null
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>.ruiPatch' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> origin=null
        /* FAKE OVERRIDES */
        CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided>' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch.ruiBranch3.<no name provided> origin=OBJECT_LITERAL
  FUN DEFAULT_PROPERTY_ACCESSOR name:<get-ruiBranch3> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.TestBranch) returnType:zakadabar.rui.runtime.testing.RuiTestFragment
    correspondingProperty: PROPERTY name:ruiBranch3 visibility:public modality:FINAL [val]
    $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.TestBranch
    BLOCK_BODY
      RETURN type=kotlin.Nothing from='public final fun <get-ruiBranch3> (): zakadabar.rui.runtime.testing.RuiTestFragment declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch'
        GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:ruiBranch3 type:zakadabar.rui.runtime.testing.RuiTestFragment visibility:private [final]' type=zakadabar.rui.runtime.testing.RuiTestFragment origin=null
          receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.TestBranch declared in zakadabar.rui.kotlin.plugin.adhoc.TestBranch.<get-ruiBranch3>' type=zakadabar.rui.kotlin.plugin.adhoc.TestBranch origin=null
```

## Generated JavaScript

```javascript
function TestBranch$ruiBranch3$1(this$0) {
  this.this$0__1 = this$0;
  RuiTestFragment.call(this, this$0._get_ruiAdapter__151867330_2if1ky_k$(), this$0.value_1 + 10 | 0);
}
TestBranch$ruiBranch3$1.prototype.ruiPatch_tplii7_k$ = function () {
  if (!((this._get_ruiDirty0__2687830541_qkuinn_k$() & 1) === 0)) {
    this._set_value__1325260276_xwdays_k$(this.this$0__1.value_1 + 10 | 0);
    this.ruiInvalidate0_fz4sip_k$(1);
  }
  RuiTestFragment.prototype.ruiPatch_tplii7_k$.call(this);
};
TestBranch$ruiBranch3$1.$metadata$ = {
  kind: 'class',
  interfaces: []
};
```  