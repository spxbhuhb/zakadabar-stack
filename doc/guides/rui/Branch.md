```text
MODULE_FRAGMENT name:<main>
  FILE fqName:zakadabar.rui.kotlin.plugin.adhoc fileName:/Users/tiz/src/zakadabar-stack/plugin/rui/rui-kotlin-plugin/src/test/kotlin/zakadabar/rui/kotlin/plugin/adhoc/Adhoc.kt
    CLASS CLASS name:RuiBranchPocTest modality:FINAL visibility:public superTypes:[zakadabar.rui.core.RuiBlock]
      $this: VALUE_PARAMETER INSTANCE_RECEIVER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
      CONSTRUCTOR visibility:public <> (value:kotlin.Int) returnType:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest [primary]
        VALUE_PARAMETER name:value index:0 type:kotlin.Int
        BLOCK_BODY
          DELEGATING_CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in zakadabar.rui.core.RuiBlock'
          INSTANCE_INITIALIZER_CALL classDescriptor='CLASS CLASS name:RuiBranchPocTest modality:FINAL visibility:public superTypes:[zakadabar.rui.core.RuiBlock]'
      PROPERTY name:value visibility:public modality:FINAL [var]
        FIELD PROPERTY_BACKING_FIELD name:value type:kotlin.Int visibility:private
          EXPRESSION_BODY
            GET_VAR 'value: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<init>' type=kotlin.Int origin=INITIALIZE_PROPERTY_FROM_PARAMETER
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-value> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest) returnType:kotlin.Int
          correspondingProperty: PROPERTY name:value visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:value type:kotlin.Int visibility:private' type=kotlin.Int origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<get-value>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
        FUN DEFAULT_PROPERTY_ACCESSOR name:<set-value> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest, <set-?>:kotlin.Int) returnType:kotlin.Unit
          correspondingProperty: PROPERTY name:value visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
          BLOCK_BODY
            SET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:value type:kotlin.Int visibility:private' type=kotlin.Unit origin=null
              receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<set-value>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
              value: GET_VAR '<set-?>: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<set-value>' type=kotlin.Int origin=null
      PROPERTY name:v2 visibility:public modality:FINAL [var]
        FIELD PROPERTY_BACKING_FIELD name:v2 type:kotlin.Int visibility:private
          EXPRESSION_BODY
            CONST Int type=kotlin.Int value=12
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-v2> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest) returnType:kotlin.Int
          correspondingProperty: PROPERTY name:v2 visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-v2> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:v2 type:kotlin.Int visibility:private' type=kotlin.Int origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<get-v2>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
        FUN DEFAULT_PROPERTY_ACCESSOR name:<set-v2> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest, <set-?>:kotlin.Int) returnType:kotlin.Unit
          correspondingProperty: PROPERTY name:v2 visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
          BLOCK_BODY
            SET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:v2 type:kotlin.Int visibility:private' type=kotlin.Unit origin=null
              receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<set-v2>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
              value: GET_VAR '<set-?>: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<set-v2>' type=kotlin.Int origin=null
      ANONYMOUS_INITIALIZER isStatic=false
        BLOCK_BODY
          VAR name:primitive0 type:zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val]
            CONSTRUCTOR_CALL 'public constructor <init> (value: kotlin.Int) [primary] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
              value: CALL 'public final fun <get-v2> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
          VAR name:branch0 type:zakadabar.rui.core.RuiBranch [val]
            CONSTRUCTOR_CALL 'public constructor <init> (select: kotlin.Function0<zakadabar.rui.core.RuiFunWrapper>) [primary] declared in zakadabar.rui.core.RuiBranch' type=zakadabar.rui.core.RuiBranch origin=null
              select: FUN_EXPR type=kotlin.Function0<zakadabar.rui.core.RuiFunWrapper> origin=LAMBDA
                FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> () returnType:zakadabar.rui.core.RuiFunWrapper
                  BLOCK_BODY
                    RETURN type=kotlin.Nothing from='local final fun <anonymous> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
                      BLOCK type=zakadabar.rui.core.RuiFunWrapper origin=WHEN
                        VAR IR_TEMPORARY_VARIABLE name:tmp0_subject type:kotlin.Int [val]
                          CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                            $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                        WHEN type=zakadabar.rui.core.RuiFunWrapper origin=WHEN
                          BRANCH
                            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
                              arg0: GET_VAR 'val tmp0_subject: kotlin.Int [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<anonymous>' type=kotlin.Int origin=null
                              arg1: CONST Int type=kotlin.Int value=1
                            then: CALL 'public final fun <get-block0func> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiFunWrapper origin=GET_PROPERTY
                              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                          BRANCH
                            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
                              arg0: GET_VAR 'val tmp0_subject: kotlin.Int [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<anonymous>' type=kotlin.Int origin=null
                              arg1: CONST Int type=kotlin.Int value=2
                            then: CALL 'public final fun <get-block1func> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiFunWrapper origin=GET_PROPERTY
                              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                          BRANCH
                            if: CONST Boolean type=kotlin.Boolean value=true
                            then: CALL 'public final fun <get-ruiEmptyBlockFunc> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.core.GlobalsKt' type=zakadabar.rui.core.RuiFunWrapper origin=GET_PROPERTY
          FUN LOCAL_FUNCTION name:create visibility:local modality:FINAL <> () returnType:kotlin.Unit
            BLOCK_BODY
              CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                $this: CALL 'public open fun <get-create> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                  $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
              CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                $this: CALL 'public open fun <get-create> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBranch' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                  $this: GET_VAR 'val branch0: zakadabar.rui.core.RuiBranch [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiBranch origin=null
          FUN LOCAL_FUNCTION name:patch visibility:local modality:FINAL <> () returnType:kotlin.Unit
            BLOCK_BODY
              WHEN type=kotlin.Unit origin=IF
                BRANCH
                  if: CALL 'public final fun not (): kotlin.Boolean [operator] declared in kotlin.Boolean' type=kotlin.Boolean origin=EXCLEQ
                    $this: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EXCLEQ
                      arg0: CALL 'public final fun and (other: kotlin.Int): kotlin.Int [infix] declared in kotlin.Int' type=kotlin.Int origin=null
                        $this: CALL 'public open fun <get-dirty> (): kotlin.Int [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                          $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                        other: CONST Int type=kotlin.Int value=1
                      arg1: CONST Int type=kotlin.Int value=0
                  then: BLOCK type=kotlin.Unit origin=null
                    CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                      $this: CALL 'public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBranch' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                        $this: GET_VAR 'val branch0: zakadabar.rui.core.RuiBranch [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiBranch origin=null
              CALL 'public open fun <set-dirty> (<set-?>: kotlin.Int): kotlin.Unit [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Unit origin=EQ
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                <set-?>: CONST Int type=kotlin.Int value=0
          FUN LOCAL_FUNCTION name:dispose visibility:local modality:FINAL <> () returnType:kotlin.Unit
            BLOCK_BODY
              CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                $this: CALL 'public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                  $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
              CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                $this: CALL 'public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBranch' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                  $this: GET_VAR 'val branch0: zakadabar.rui.core.RuiBranch [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiBranch origin=null
          TYPE_OP type=kotlin.Unit origin=IMPLICIT_COERCION_TO_UNIT typeOperand=kotlin.Unit
            CALL 'public final fun set (create: kotlin.Function0<kotlin.Unit>, patch: kotlin.Function0<kotlin.Unit>, dispose: kotlin.Function0<kotlin.Unit>): zakadabar.rui.core.RuiBlock [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiBlock origin=null
              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
              create: FUNCTION_REFERENCE 'local final fun create (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
              patch: FUNCTION_REFERENCE 'local final fun patch (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
              dispose: FUNCTION_REFERENCE 'local final fun dispose (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
      PROPERTY name:block0func visibility:public modality:FINAL [val]
        FIELD PROPERTY_BACKING_FIELD name:block0func type:zakadabar.rui.core.RuiFunWrapper visibility:private [final]
          EXPRESSION_BODY
            CONSTRUCTOR_CALL 'public constructor <init> (func: kotlin.Function0<zakadabar.rui.core.RuiBlock>) [primary] declared in zakadabar.rui.core.RuiFunWrapper' type=zakadabar.rui.core.RuiFunWrapper origin=null
              func: FUN_EXPR type=kotlin.Function0<zakadabar.rui.core.RuiBlock> origin=LAMBDA
                FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> () returnType:zakadabar.rui.core.RuiBlock
                  BLOCK_BODY
                    VAR name:primitive0 type:zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val]
                      CONSTRUCTOR_CALL 'public constructor <init> (value: kotlin.Int) [primary] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                        value: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                          $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                            $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                          other: CONST Int type=kotlin.Int value=10
                    FUN LOCAL_FUNCTION name:create visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-create> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    FUN LOCAL_FUNCTION name:patch visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public final fun <set-value> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Unit origin=EQ
                          $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                          <set-?>: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                            $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                            other: CONST Int type=kotlin.Int value=10
                        CALL 'public open fun <set-dirty> (<set-?>: kotlin.Int): kotlin.Unit [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Unit origin=EQ
                          $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                          <set-?>: CONST Int type=kotlin.Int value=1
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    FUN LOCAL_FUNCTION name:dispose visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    RETURN type=kotlin.Nothing from='local final fun <anonymous> (): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func'
                      CALL 'public final fun set (create: kotlin.Function0<kotlin.Unit>, patch: kotlin.Function0<kotlin.Unit>, dispose: kotlin.Function0<kotlin.Unit>): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.core.RuiBlock' type=zakadabar.rui.core.RuiBlock origin=null
                        $this: CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in zakadabar.rui.core.RuiBlock' type=zakadabar.rui.core.RuiBlock origin=null
                        create: FUNCTION_REFERENCE 'local final fun create (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
                        patch: FUNCTION_REFERENCE 'local final fun patch (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
                        dispose: FUNCTION_REFERENCE 'local final fun dispose (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-block0func> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest) returnType:zakadabar.rui.core.RuiFunWrapper
          correspondingProperty: PROPERTY name:block0func visibility:public modality:FINAL [val]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-block0func> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:block0func type:zakadabar.rui.core.RuiFunWrapper visibility:private [final]' type=zakadabar.rui.core.RuiFunWrapper origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<get-block0func>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
      PROPERTY name:block1func visibility:public modality:FINAL [val]
        FIELD PROPERTY_BACKING_FIELD name:block1func type:zakadabar.rui.core.RuiFunWrapper visibility:private [final]
          EXPRESSION_BODY
            CONSTRUCTOR_CALL 'public constructor <init> (func: kotlin.Function0<zakadabar.rui.core.RuiBlock>) [primary] declared in zakadabar.rui.core.RuiFunWrapper' type=zakadabar.rui.core.RuiFunWrapper origin=null
              func: FUN_EXPR type=kotlin.Function0<zakadabar.rui.core.RuiBlock> origin=LAMBDA
                FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> () returnType:zakadabar.rui.core.RuiBlock
                  BLOCK_BODY
                    VAR name:primitive0 type:zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val]
                      CONSTRUCTOR_CALL 'public constructor <init> (value: kotlin.Int) [primary] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                        value: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                          $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                            $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                          other: CONST Int type=kotlin.Int value=20
                    FUN LOCAL_FUNCTION name:create visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-create> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    FUN LOCAL_FUNCTION name:patch visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public final fun <set-value> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Unit origin=EQ
                          $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                          <set-?>: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                            $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                            other: CONST Int type=kotlin.Int value=20
                        CALL 'public open fun <set-dirty> (<set-?>: kotlin.Int): kotlin.Unit [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Unit origin=EQ
                          $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                          <set-?>: CONST Int type=kotlin.Int value=1
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    FUN LOCAL_FUNCTION name:dispose visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    RETURN type=kotlin.Nothing from='local final fun <anonymous> (): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func'
                      CALL 'public final fun set (create: kotlin.Function0<kotlin.Unit>, patch: kotlin.Function0<kotlin.Unit>, dispose: kotlin.Function0<kotlin.Unit>): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.core.RuiBlock' type=zakadabar.rui.core.RuiBlock origin=null
                        $this: CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in zakadabar.rui.core.RuiBlock' type=zakadabar.rui.core.RuiBlock origin=null
                        create: FUNCTION_REFERENCE 'local final fun create (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
                        patch: FUNCTION_REFERENCE 'local final fun patch (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
                        dispose: FUNCTION_REFERENCE 'local final fun dispose (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-block1func> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest) returnType:zakadabar.rui.core.RuiFunWrapper
          correspondingProperty: PROPERTY name:block1func visibility:public modality:FINAL [val]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-block1func> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:block1func type:zakadabar.rui.core.RuiFunWrapper visibility:private [final]' type=zakadabar.rui.core.RuiFunWrapper origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<get-block1func>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
      PROPERTY FAKE_OVERRIDE name:create visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open create: kotlin.Function0<kotlin.Unit> [var]
        FUN FAKE_OVERRIDE name:<get-create> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock) returnType:kotlin.Function0<kotlin.Unit> [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:create visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-create> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        FUN FAKE_OVERRIDE name:<set-create> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock, <set-?>:kotlin.Function0<kotlin.Unit>) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:create visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-create> (<set-?>: kotlin.Function0<kotlin.Unit>): kotlin.Unit declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Function0<kotlin.Unit>
      PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open dirty: kotlin.Int [var]
        FUN FAKE_OVERRIDE name:<get-dirty> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock) returnType:kotlin.Int [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-dirty> (): kotlin.Int declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        FUN FAKE_OVERRIDE name:<set-dirty> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock, <set-?>:kotlin.Int) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-dirty> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
      PROPERTY FAKE_OVERRIDE name:dispose visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open dispose: kotlin.Function0<kotlin.Unit> [var]
        FUN FAKE_OVERRIDE name:<get-dispose> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock) returnType:kotlin.Function0<kotlin.Unit> [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dispose visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        FUN FAKE_OVERRIDE name:<set-dispose> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock, <set-?>:kotlin.Function0<kotlin.Unit>) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dispose visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-dispose> (<set-?>: kotlin.Function0<kotlin.Unit>): kotlin.Unit declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Function0<kotlin.Unit>
      PROPERTY FAKE_OVERRIDE name:patch visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open patch: kotlin.Function0<kotlin.Unit> [var]
        FUN FAKE_OVERRIDE name:<get-patch> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock) returnType:kotlin.Function0<kotlin.Unit> [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:patch visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        FUN FAKE_OVERRIDE name:<set-patch> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock, <set-?>:kotlin.Function0<kotlin.Unit>) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:patch visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-patch> (<set-?>: kotlin.Function0<kotlin.Unit>): kotlin.Unit declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Function0<kotlin.Unit>
      FUN FAKE_OVERRIDE name:equals visibility:public modality:OPEN <> ($this:kotlin.Any, other:kotlin.Any?) returnType:kotlin.Boolean [fake_override,operator]
        overridden:
          public open fun equals (other: kotlin.Any?): kotlin.Boolean [fake_override,operator] declared in zakadabar.rui.core.RuiBlock
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any
        VALUE_PARAMETER name:other index:0 type:kotlin.Any?
      FUN FAKE_OVERRIDE name:hashCode visibility:public modality:OPEN <> ($this:kotlin.Any) returnType:kotlin.Int [fake_override]
        overridden:
          public open fun hashCode (): kotlin.Int [fake_override] declared in zakadabar.rui.core.RuiBlock
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any
      FUN FAKE_OVERRIDE name:set visibility:public modality:FINAL <> ($this:zakadabar.rui.core.RuiBlock, create:kotlin.Function0<kotlin.Unit>, patch:kotlin.Function0<kotlin.Unit>, dispose:kotlin.Function0<kotlin.Unit>) returnType:zakadabar.rui.core.RuiBlock [fake_override]
        overridden:
          public final fun set (create: kotlin.Function0<kotlin.Unit>, patch: kotlin.Function0<kotlin.Unit>, dispose: kotlin.Function0<kotlin.Unit>): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.core.RuiBlock
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        VALUE_PARAMETER name:create index:0 type:kotlin.Function0<kotlin.Unit>
        VALUE_PARAMETER name:patch index:1 type:kotlin.Function0<kotlin.Unit>
        VALUE_PARAMETER name:dispose index:2 type:kotlin.Function0<kotlin.Unit>
      FUN FAKE_OVERRIDE name:toString visibility:public modality:OPEN <> ($this:kotlin.Any) returnType:kotlin.String [fake_override]
        overridden:
          public open fun toString (): kotlin.String [fake_override] declared in zakadabar.rui.core.RuiBlock
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any

MODULE_FRAGMENT name:<main>
  FILE fqName:zakadabar.rui.kotlin.plugin.adhoc fileName:/Users/tiz/src/zakadabar-stack/plugin/rui/rui-kotlin-plugin/src/test/kotlin/zakadabar/rui/kotlin/plugin/adhoc/Adhoc.kt
    CLASS CLASS name:RuiBranchPocTest modality:FINAL visibility:public superTypes:[zakadabar.rui.core.RuiBlock]
      $this: VALUE_PARAMETER INSTANCE_RECEIVER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
      CONSTRUCTOR visibility:public <> (value:kotlin.Int) returnType:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest [primary]
        VALUE_PARAMETER name:value index:0 type:kotlin.Int
        BLOCK_BODY
          DELEGATING_CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in zakadabar.rui.core.RuiBlock'
          INSTANCE_INITIALIZER_CALL classDescriptor='CLASS CLASS name:RuiBranchPocTest modality:FINAL visibility:public superTypes:[zakadabar.rui.core.RuiBlock]'
      PROPERTY name:value visibility:public modality:FINAL [var]
        FIELD PROPERTY_BACKING_FIELD name:value type:kotlin.Int visibility:private
          EXPRESSION_BODY
            GET_VAR 'value: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<init>' type=kotlin.Int origin=INITIALIZE_PROPERTY_FROM_PARAMETER
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-value> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest) returnType:kotlin.Int
          correspondingProperty: PROPERTY name:value visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:value type:kotlin.Int visibility:private' type=kotlin.Int origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<get-value>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
        FUN DEFAULT_PROPERTY_ACCESSOR name:<set-value> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest, <set-?>:kotlin.Int) returnType:kotlin.Unit
          correspondingProperty: PROPERTY name:value visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
          BLOCK_BODY
            SET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:value type:kotlin.Int visibility:private' type=kotlin.Unit origin=null
              receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<set-value>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
              value: GET_VAR '<set-?>: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<set-value>' type=kotlin.Int origin=null
      PROPERTY name:v2 visibility:public modality:FINAL [var]
        FIELD PROPERTY_BACKING_FIELD name:v2 type:kotlin.Int visibility:private
          EXPRESSION_BODY
            CONST Int type=kotlin.Int value=12
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-v2> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest) returnType:kotlin.Int
          correspondingProperty: PROPERTY name:v2 visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-v2> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:v2 type:kotlin.Int visibility:private' type=kotlin.Int origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<get-v2>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
        FUN DEFAULT_PROPERTY_ACCESSOR name:<set-v2> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest, <set-?>:kotlin.Int) returnType:kotlin.Unit
          correspondingProperty: PROPERTY name:v2 visibility:public modality:FINAL [var]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
          BLOCK_BODY
            SET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:v2 type:kotlin.Int visibility:private' type=kotlin.Unit origin=null
              receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<set-v2>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
              value: GET_VAR '<set-?>: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<set-v2>' type=kotlin.Int origin=null
      ANONYMOUS_INITIALIZER isStatic=false
        BLOCK_BODY
          VAR name:primitive0 type:zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val]
            CONSTRUCTOR_CALL 'public constructor <init> (value: kotlin.Int) [primary] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
              value: CALL 'public final fun <get-v2> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
          VAR name:branch0 type:zakadabar.rui.core.RuiBranch [val]
            CONSTRUCTOR_CALL 'public constructor <init> (select: kotlin.Function0<zakadabar.rui.core.RuiFunWrapper>) [primary] declared in zakadabar.rui.core.RuiBranch' type=zakadabar.rui.core.RuiBranch origin=null
              select: FUN_EXPR type=kotlin.Function0<zakadabar.rui.core.RuiFunWrapper> origin=LAMBDA
                FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> () returnType:zakadabar.rui.core.RuiFunWrapper
                  BLOCK_BODY
                    RETURN type=kotlin.Nothing from='local final fun <anonymous> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
                      BLOCK type=zakadabar.rui.core.RuiFunWrapper origin=WHEN
                        VAR IR_TEMPORARY_VARIABLE name:tmp0_subject type:kotlin.Int [val]
                          CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                            $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                        WHEN type=zakadabar.rui.core.RuiFunWrapper origin=WHEN
                          BRANCH
                            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
                              arg0: GET_VAR 'val tmp0_subject: kotlin.Int [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<anonymous>' type=kotlin.Int origin=null
                              arg1: CONST Int type=kotlin.Int value=1
                            then: CALL 'public final fun <get-block0func> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiFunWrapper origin=GET_PROPERTY
                              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                          BRANCH
                            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
                              arg0: GET_VAR 'val tmp0_subject: kotlin.Int [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<anonymous>' type=kotlin.Int origin=null
                              arg1: CONST Int type=kotlin.Int value=2
                            then: CALL 'public final fun <get-block1func> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiFunWrapper origin=GET_PROPERTY
                              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                          BRANCH
                            if: CONST Boolean type=kotlin.Boolean value=true
                            then: CALL 'public final fun <get-ruiEmptyBlockFunc> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.core.GlobalsKt' type=zakadabar.rui.core.RuiFunWrapper origin=GET_PROPERTY
          FUN LOCAL_FUNCTION name:create visibility:local modality:FINAL <> () returnType:kotlin.Unit
            BLOCK_BODY
              CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                $this: CALL 'public open fun <get-create> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                  $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
              CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                $this: CALL 'public open fun <get-create> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBranch' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                  $this: GET_VAR 'val branch0: zakadabar.rui.core.RuiBranch [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiBranch origin=null
          FUN LOCAL_FUNCTION name:patch visibility:local modality:FINAL <> () returnType:kotlin.Unit
            BLOCK_BODY
              WHEN type=kotlin.Unit origin=IF
                BRANCH
                  if: CALL 'public final fun not (): kotlin.Boolean [operator] declared in kotlin.Boolean' type=kotlin.Boolean origin=EXCLEQ
                    $this: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EXCLEQ
                      arg0: CALL 'public final fun and (other: kotlin.Int): kotlin.Int [infix] declared in kotlin.Int' type=kotlin.Int origin=null
                        $this: CALL 'public open fun <get-dirty> (): kotlin.Int [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                          $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                        other: CONST Int type=kotlin.Int value=1
                      arg1: CONST Int type=kotlin.Int value=0
                  then: BLOCK type=kotlin.Unit origin=null
                    CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                      $this: CALL 'public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBranch' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                        $this: GET_VAR 'val branch0: zakadabar.rui.core.RuiBranch [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiBranch origin=null
              CALL 'public open fun <set-dirty> (<set-?>: kotlin.Int): kotlin.Unit [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Unit origin=EQ
                $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                <set-?>: CONST Int type=kotlin.Int value=0
          FUN LOCAL_FUNCTION name:dispose visibility:local modality:FINAL <> () returnType:kotlin.Unit
            BLOCK_BODY
              CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                $this: CALL 'public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                  $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
              CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                $this: CALL 'public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBranch' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                  $this: GET_VAR 'val branch0: zakadabar.rui.core.RuiBranch [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiBranch origin=null
          TYPE_OP type=kotlin.Unit origin=IMPLICIT_COERCION_TO_UNIT typeOperand=kotlin.Unit
            CALL 'public final fun set (create: kotlin.Function0<kotlin.Unit>, patch: kotlin.Function0<kotlin.Unit>, dispose: kotlin.Function0<kotlin.Unit>): zakadabar.rui.core.RuiBlock [fake_override] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.core.RuiBlock origin=null
              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
              create: FUNCTION_REFERENCE 'local final fun create (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
              patch: FUNCTION_REFERENCE 'local final fun patch (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
              dispose: FUNCTION_REFERENCE 'local final fun dispose (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
      PROPERTY name:block0func visibility:public modality:FINAL [val]
        FIELD PROPERTY_BACKING_FIELD name:block0func type:zakadabar.rui.core.RuiFunWrapper visibility:private [final]
          EXPRESSION_BODY
            CONSTRUCTOR_CALL 'public constructor <init> (func: kotlin.Function0<zakadabar.rui.core.RuiBlock>) [primary] declared in zakadabar.rui.core.RuiFunWrapper' type=zakadabar.rui.core.RuiFunWrapper origin=null
              func: FUN_EXPR type=kotlin.Function0<zakadabar.rui.core.RuiBlock> origin=LAMBDA
                FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> () returnType:zakadabar.rui.core.RuiBlock
                  BLOCK_BODY
                    VAR name:primitive0 type:zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val]
                      CONSTRUCTOR_CALL 'public constructor <init> (value: kotlin.Int) [primary] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                        value: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                          $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                            $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                          other: CONST Int type=kotlin.Int value=10
                    FUN LOCAL_FUNCTION name:create visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-create> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    FUN LOCAL_FUNCTION name:patch visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public final fun <set-value> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Unit origin=EQ
                          $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                          <set-?>: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                            $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                            other: CONST Int type=kotlin.Int value=10
                        CALL 'public open fun <set-dirty> (<set-?>: kotlin.Int): kotlin.Unit [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Unit origin=EQ
                          $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                          <set-?>: CONST Int type=kotlin.Int value=1
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    FUN LOCAL_FUNCTION name:dispose visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    RETURN type=kotlin.Nothing from='local final fun <anonymous> (): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func'
                      CALL 'public final fun set (create: kotlin.Function0<kotlin.Unit>, patch: kotlin.Function0<kotlin.Unit>, dispose: kotlin.Function0<kotlin.Unit>): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.core.RuiBlock' type=zakadabar.rui.core.RuiBlock origin=null
                        $this: CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in zakadabar.rui.core.RuiBlock' type=zakadabar.rui.core.RuiBlock origin=null
                        create: FUNCTION_REFERENCE 'local final fun create (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
                        patch: FUNCTION_REFERENCE 'local final fun patch (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
                        dispose: FUNCTION_REFERENCE 'local final fun dispose (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block0func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-block0func> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest) returnType:zakadabar.rui.core.RuiFunWrapper
          correspondingProperty: PROPERTY name:block0func visibility:public modality:FINAL [val]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-block0func> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:block0func type:zakadabar.rui.core.RuiFunWrapper visibility:private [final]' type=zakadabar.rui.core.RuiFunWrapper origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<get-block0func>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
      PROPERTY name:block1func visibility:public modality:FINAL [val]
        FIELD PROPERTY_BACKING_FIELD name:block1func type:zakadabar.rui.core.RuiFunWrapper visibility:private [final]
          EXPRESSION_BODY
            CONSTRUCTOR_CALL 'public constructor <init> (func: kotlin.Function0<zakadabar.rui.core.RuiBlock>) [primary] declared in zakadabar.rui.core.RuiFunWrapper' type=zakadabar.rui.core.RuiFunWrapper origin=null
              func: FUN_EXPR type=kotlin.Function0<zakadabar.rui.core.RuiBlock> origin=LAMBDA
                FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> () returnType:zakadabar.rui.core.RuiBlock
                  BLOCK_BODY
                    VAR name:primitive0 type:zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val]
                      CONSTRUCTOR_CALL 'public constructor <init> (value: kotlin.Int) [primary] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                        value: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                          $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                            $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                          other: CONST Int type=kotlin.Int value=20
                    FUN LOCAL_FUNCTION name:create visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-create> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    FUN LOCAL_FUNCTION name:patch visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public final fun <set-value> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Unit origin=EQ
                          $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                          <set-?>: CALL 'public final fun plus (other: kotlin.Int): kotlin.Int [operator] declared in kotlin.Int' type=kotlin.Int origin=PLUS
                            $this: CALL 'public final fun <get-value> (): kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=kotlin.Int origin=GET_PROPERTY
                              $this: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
                            other: CONST Int type=kotlin.Int value=20
                        CALL 'public open fun <set-dirty> (<set-?>: kotlin.Int): kotlin.Unit [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Unit origin=EQ
                          $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                          <set-?>: CONST Int type=kotlin.Int value=1
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    FUN LOCAL_FUNCTION name:dispose visibility:local modality:FINAL <> () returnType:kotlin.Unit
                      BLOCK_BODY
                        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
                          $this: CALL 'public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> [fake_override] declared in zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock' type=kotlin.Function0<kotlin.Unit> origin=GET_PROPERTY
                            $this: GET_VAR 'val primitive0: zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock [val] declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock origin=null
                    RETURN type=kotlin.Nothing from='local final fun <anonymous> (): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func'
                      CALL 'public final fun set (create: kotlin.Function0<kotlin.Unit>, patch: kotlin.Function0<kotlin.Unit>, dispose: kotlin.Function0<kotlin.Unit>): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.core.RuiBlock' type=zakadabar.rui.core.RuiBlock origin=null
                        $this: CONSTRUCTOR_CALL 'public constructor <init> () [primary] declared in zakadabar.rui.core.RuiBlock' type=zakadabar.rui.core.RuiBlock origin=null
                        create: FUNCTION_REFERENCE 'local final fun create (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
                        patch: FUNCTION_REFERENCE 'local final fun patch (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
                        dispose: FUNCTION_REFERENCE 'local final fun dispose (): kotlin.Unit declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.block1func.<anonymous>' type=kotlin.reflect.KFunction0<kotlin.Unit> origin=null reflectionTarget=<same>
        FUN DEFAULT_PROPERTY_ACCESSOR name:<get-block1func> visibility:public modality:FINAL <> ($this:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest) returnType:zakadabar.rui.core.RuiFunWrapper
          correspondingProperty: PROPERTY name:block1func visibility:public modality:FINAL [val]
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest
          BLOCK_BODY
            RETURN type=kotlin.Nothing from='public final fun <get-block1func> (): zakadabar.rui.core.RuiFunWrapper declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest'
              GET_FIELD 'FIELD PROPERTY_BACKING_FIELD name:block1func type:zakadabar.rui.core.RuiFunWrapper visibility:private [final]' type=zakadabar.rui.core.RuiFunWrapper origin=null
                receiver: GET_VAR '<this>: zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest declared in zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest.<get-block1func>' type=zakadabar.rui.kotlin.plugin.adhoc.RuiBranchPocTest origin=null
      PROPERTY FAKE_OVERRIDE name:create visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open create: kotlin.Function0<kotlin.Unit> [var]
        FUN FAKE_OVERRIDE name:<get-create> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock) returnType:kotlin.Function0<kotlin.Unit> [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:create visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-create> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        FUN FAKE_OVERRIDE name:<set-create> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock, <set-?>:kotlin.Function0<kotlin.Unit>) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:create visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-create> (<set-?>: kotlin.Function0<kotlin.Unit>): kotlin.Unit declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Function0<kotlin.Unit>
      PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open dirty: kotlin.Int [var]
        FUN FAKE_OVERRIDE name:<get-dirty> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock) returnType:kotlin.Int [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-dirty> (): kotlin.Int declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        FUN FAKE_OVERRIDE name:<set-dirty> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock, <set-?>:kotlin.Int) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dirty visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-dirty> (<set-?>: kotlin.Int): kotlin.Unit declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Int
      PROPERTY FAKE_OVERRIDE name:dispose visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open dispose: kotlin.Function0<kotlin.Unit> [var]
        FUN FAKE_OVERRIDE name:<get-dispose> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock) returnType:kotlin.Function0<kotlin.Unit> [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dispose visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-dispose> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        FUN FAKE_OVERRIDE name:<set-dispose> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock, <set-?>:kotlin.Function0<kotlin.Unit>) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:dispose visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-dispose> (<set-?>: kotlin.Function0<kotlin.Unit>): kotlin.Unit declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Function0<kotlin.Unit>
      PROPERTY FAKE_OVERRIDE name:patch visibility:public modality:OPEN [fake_override,var]
        overridden:
          public open patch: kotlin.Function0<kotlin.Unit> [var]
        FUN FAKE_OVERRIDE name:<get-patch> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock) returnType:kotlin.Function0<kotlin.Unit> [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:patch visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <get-patch> (): kotlin.Function0<kotlin.Unit> declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        FUN FAKE_OVERRIDE name:<set-patch> visibility:public modality:OPEN <> ($this:zakadabar.rui.core.RuiBlock, <set-?>:kotlin.Function0<kotlin.Unit>) returnType:kotlin.Unit [fake_override]
          correspondingProperty: PROPERTY FAKE_OVERRIDE name:patch visibility:public modality:OPEN [fake_override,var]
          overridden:
            public open fun <set-patch> (<set-?>: kotlin.Function0<kotlin.Unit>): kotlin.Unit declared in zakadabar.rui.core.RuiBlock
          $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
          VALUE_PARAMETER name:<set-?> index:0 type:kotlin.Function0<kotlin.Unit>
      FUN FAKE_OVERRIDE name:equals visibility:public modality:OPEN <> ($this:kotlin.Any, other:kotlin.Any?) returnType:kotlin.Boolean [fake_override,operator]
        overridden:
          public open fun equals (other: kotlin.Any?): kotlin.Boolean [fake_override,operator] declared in zakadabar.rui.core.RuiBlock
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any
        VALUE_PARAMETER name:other index:0 type:kotlin.Any?
      FUN FAKE_OVERRIDE name:hashCode visibility:public modality:OPEN <> ($this:kotlin.Any) returnType:kotlin.Int [fake_override]
        overridden:
          public open fun hashCode (): kotlin.Int [fake_override] declared in zakadabar.rui.core.RuiBlock
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any
      FUN FAKE_OVERRIDE name:set visibility:public modality:FINAL <> ($this:zakadabar.rui.core.RuiBlock, create:kotlin.Function0<kotlin.Unit>, patch:kotlin.Function0<kotlin.Unit>, dispose:kotlin.Function0<kotlin.Unit>) returnType:zakadabar.rui.core.RuiBlock [fake_override]
        overridden:
          public final fun set (create: kotlin.Function0<kotlin.Unit>, patch: kotlin.Function0<kotlin.Unit>, dispose: kotlin.Function0<kotlin.Unit>): zakadabar.rui.core.RuiBlock declared in zakadabar.rui.core.RuiBlock
        $this: VALUE_PARAMETER name:<this> type:zakadabar.rui.core.RuiBlock
        VALUE_PARAMETER name:create index:0 type:kotlin.Function0<kotlin.Unit>
        VALUE_PARAMETER name:patch index:1 type:kotlin.Function0<kotlin.Unit>
        VALUE_PARAMETER name:dispose index:2 type:kotlin.Function0<kotlin.Unit>
      FUN FAKE_OVERRIDE name:toString visibility:public modality:OPEN <> ($this:kotlin.Any) returnType:kotlin.String [fake_override]
        overridden:
          public open fun toString (): kotlin.String [fake_override] declared in zakadabar.rui.core.RuiBlock
        $this: VALUE_PARAMETER name:<this> type:kotlin.Any
```


```javascript
!function (t, e) {
    "object" == typeof exports && "object" == typeof module ? module.exports = e() : "function" == typeof define && define.amd ? define([], e) : "object" == typeof exports ? exports["rui-core"] = e() : t["rui-core"] = e()
}(this, (function () {
    return t = {
        486: function (t, e) {
            var _, n;
            void 0 === (n = "function" == typeof (_ = function (t) {
                "use strict";

                function e() {
                    _ = this
                }

                var _, n, o;

                function r() {
                }

                function i(t) {
                    r.call(this), this.outputStream_1 = t
                }

                function s() {
                    c.call(this)
                }

                function c() {
                    r.call(this), this.buffer_1 = ""
                }

                function u() {
                    var t;
                    o || (o = !0, t = "undefined" != typeof process && process.versions && process.versions.node ? new i(process.stdout) : new s, n = t)
                }

                return i.prototype = Object.create(r.prototype), i.prototype.constructor = i, c.prototype = Object.create(r.prototype), c.prototype.constructor = c, s.prototype = Object.create(c.prototype), s.prototype.constructor = s, e.prototype.toString = function () {
                    return "kotlin.Unit"
                }, e.$metadata$ = {simpleName: "Unit", kind: "object", interfaces: []}, r.prototype.println_uuzh5q_k$ = function () {
                    this.print_o29p2b_k$("\n")
                }, r.prototype.println_gh3jfj_k$ = function (t) {
                    this.print_o29p2b_k$(t), this.println_uuzh5q_k$()
                }, r.$metadata$ = {simpleName: "BaseOutput", kind: "class", interfaces: []}, i.prototype.print_o29p2b_k$ = function (t) {
                    var e = String(t);
                    this.outputStream_1.write(e)
                }, i.$metadata$ = {simpleName: "NodeJsOutput", kind: "class", interfaces: []}, s.prototype.print_o29p2b_k$ = function (t) {
                    var e = String(t), _ = e.lastIndexOf("\n", 0);
                    if (_ >= 0) {
                        var n, o = this._get_buffer__2513334987_tgqkad_k$();
                        n = e.substring(0, _), this._set_buffer__2424755159_nu2sqj_k$(o + n), this.flush_sgqoqb_k$();
                        var r = _ + 1 | 0;
                        e = e.substring(r)
                    }
                    this._set_buffer__2424755159_nu2sqj_k$(this._get_buffer__2513334987_tgqkad_k$() + e)
                }, s.prototype.flush_sgqoqb_k$ = function () {
                    console.log(this._get_buffer__2513334987_tgqkad_k$()), this._set_buffer__2424755159_nu2sqj_k$("")
                }, s.$metadata$ = {simpleName: "BufferedOutputToConsoleLog", kind: "class", interfaces: []}, c.prototype._set_buffer__2424755159_nu2sqj_k$ = function (t) {
                    this.buffer_1 = t
                }, c.prototype._get_buffer__2513334987_tgqkad_k$ = function () {
                    return this.buffer_1
                }, c.prototype.print_o29p2b_k$ = function (t) {
                    var e, _ = this, n = this.buffer_1;
                    e = String(t), _.buffer_1 = n + e
                }, c.$metadata$ = {simpleName: "BufferedOutput", kind: "class", interfaces: []}, t.$crossModule$ = t.$crossModule$ || {}, t.$crossModule$.Unit_getInstance = function () {
                    return null == _ && new e, _
                }, t.$crossModule$.println = function (t) {
                    u(), (u(), n).println_gh3jfj_k$(t)
                }, t.$crossModule$.equals = function (t, e) {
                    return null == t ? null == e : null != e && ("object" == typeof t && "function" == typeof t.equals ? t.equals(e) : t != t ? e != e : "number" == typeof t && "number" == typeof e ? t === e && (0 !== t || 1 / t == 1 / e) : t === e)
                }, t
            }) ? _.apply(e, [e]) : _) || (t.exports = n)
        }, 148: function (t, e, _) {
            var n, o, r;
            o = [e, _(486)], void 0 === (r = "function" == typeof (n = function (t, e) {
                var _, n, o, r = e.$crossModule$.Unit_getInstance, i = e.$crossModule$.println, s = e.$crossModule$.equals;

                function c(t) {
                    return function () {
                        var e = new a(t.value_1 + 10 | 0);
                        r(), r(), r(), r();
                        var _, n, o = new f, i = (_ = e, (n = function () {
                            return _._get_create__3313892751_g83typ_k$()(), r()
                        }).callableName = "create", n), s = function (t, e) {
                            var _ = function () {
                                return function (t, e) {
                                    t._set_value__1325260276_xwdays_k$(e.value_1 + 10 | 0), t._set_dirty__817502131_p73qw3_k$(1), t._get_patch__3511868553_cy8iuv_k$()()
                                }(t, e), r()
                            };
                            return _.callableName = "patch", _
                        }(e, t);
                        return o.set_w880v5_k$(i, s, function (t) {
                            var e = function () {
                                return t._get_dispose__2411198674_v5jp66_k$()(), r()
                            };
                            return e.callableName = "dispose", e
                        }(e))
                    }
                }

                function u(t) {
                    return function () {
                        var e = new a(t.value_1 + 20 | 0);
                        r(), r(), r(), r();
                        var _, n, o = new f, i = (_ = e, (n = function () {
                            return _._get_create__3313892751_g83typ_k$()(), r()
                        }).callableName = "create", n), s = function (t, e) {
                            var _ = function () {
                                return function (t, e) {
                                    t._set_value__1325260276_xwdays_k$(e.value_1 + 20 | 0), t._set_dirty__817502131_p73qw3_k$(1), t._get_patch__3511868553_cy8iuv_k$()()
                                }(t, e), r()
                            };
                            return _.callableName = "patch", _
                        }(e, t);
                        return o.set_w880v5_k$(i, s, function (t) {
                            var e = function () {
                                return t._get_dispose__2411198674_v5jp66_k$()(), r()
                            };
                            return e.callableName = "dispose", e
                        }(e))
                    }
                }

                function p(t) {
                    f.call(this), this.value_1 = t, this.v2__1 = 12;
                    var e, _, n, o, i = new a(this.v2__1), s = new l((o = this, function () {
                        switch (o.value_1) {
                            case 1:
                                return o.block0func_1;
                            case 2:
                                return o.block1func_1;
                            default:
                                return k()
                        }
                    })), p = (e = i, _ = s, n = function () {
                        return t = _, e._get_create__3313892751_g83typ_k$()(), t._get_create__3313892751_g83typ_k$()(), r();
                        var t
                    }, n.callableName = "create", n), $ = function (t, e) {
                        var _ = function () {
                            return function (t, e) {
                                0 != (1 & t._get_dirty__3175664191_iiejsx_k$()) && e._get_patch__3511868553_cy8iuv_k$()(), t._set_dirty__817502131_p73qw3_k$(0)
                            }(t, e), r()
                        };
                        return _.callableName = "patch", _
                    }(this, s);
                    this.set_w880v5_k$(p, $, function (t, e) {
                        var _ = function () {
                            return _ = e, t._get_dispose__2411198674_v5jp66_k$()(), _._get_dispose__2411198674_v5jp66_k$()(), r();
                            var _
                        };
                        return _.callableName = "dispose", _
                    }(i, s)), r(), this.block0func_1 = new h(c(this)), this.block1func_1 = new h(u(this))
                }

                function a(t) {
                    var e;
                    f.call(this), this.value_1 = t, i("================    Primitive.init: " + this.value_1), this.patch_2 = (e = this, function () {
                        i("================    Primitive.patch: " + e.value_1)
                    })
                }

                function f() {
                    this.dirty_1 = 0, this.create_1 = function () {
                        return r()
                    }, this.patch_1 = function () {
                        return r()
                    }, this.dispose_1 = function () {
                        return r()
                    }
                }

                function l(t) {
                    var e;
                    f.call(this), this.select_1 = t, this.blockFun_1 = k(), this.block_1 = $(), this.create_2 = (e = this, function () {
                        e.blockFun_1 = e.select_1(), r(), e.block_1 = e.blockFun_1._get_func__797377543_d6qkg7_k$()(), r(), e.block_1._get_create__3313892751_g83typ_k$()()
                    }), this.patch_2 = function (t) {
                        return function () {
                            var e = t.select_1();
                            r(), s(e, t.blockFun_1) ? t.block_1._get_patch__3511868553_cy8iuv_k$()() : (t.block_1._get_dispose__2411198674_v5jp66_k$()(), t.blockFun_1 = e, t.block_1 = t.blockFun_1._get_func__797377543_d6qkg7_k$()(), t.block_1._get_create__3313892751_g83typ_k$()())
                        }
                    }(this), this.dispose_2 = function (t) {
                        return function () {
                            t.block_1._get_dispose__2411198674_v5jp66_k$()()
                        }
                    }(this)
                }

                function h(t) {
                    this.func_1 = t
                }

                function k() {
                    return y(), _
                }

                function $() {
                    return y(), n
                }

                function y() {
                    o || (o = !0, _ = new h((function () {
                        return $()
                    })), n = new f)
                }

                return p.prototype = Object.create(f.prototype), p.prototype.constructor = p, a.prototype = Object.create(f.prototype), a.prototype.constructor = a, l.prototype = Object.create(f.prototype), l.prototype.constructor = l, p.$metadata$ = {
                    simpleName: "RuiBranchPoc",
                    kind: "class",
                    interfaces: []
                }, a.prototype._set_value__1325260276_xwdays_k$ = function (t) {
                    this.value_1 = t
                }, a.prototype._set_patch__1153706493_m9y3rp_k$ = function (t) {
                    this.patch_2 = t
                }, a.prototype._get_patch__3511868553_cy8iuv_k$ = function () {
                    return this.patch_2
                }, a.$metadata$ = {simpleName: "RuiPrimitiveBlock", kind: "class", interfaces: []}, f.prototype._set_dirty__817502131_p73qw3_k$ = function (t) {
                    this.dirty_1 = t
                }, f.prototype._get_dirty__3175664191_iiejsx_k$ = function () {
                    return this.dirty_1
                }, f.prototype._set_create__3225312923_wz5zt5_k$ = function (t) {
                    this.create_1 = t
                }, f.prototype._get_create__3313892751_g83typ_k$ = function () {
                    return this.create_1
                }, f.prototype._set_patch__1153706493_m9y3rp_k$ = function (t) {
                    this.patch_1 = t
                }, f.prototype._get_patch__3511868553_cy8iuv_k$ = function () {
                    return this.patch_1
                }, f.prototype._set_dispose__3960191302_ui6ius_k$ = function (t) {
                    this.dispose_1 = t
                }, f.prototype._get_dispose__2411198674_v5jp66_k$ = function () {
                    return this.dispose_1
                }, f.prototype.set_w880v5_k$ = function (t, e, _) {
                    return this._set_create__3225312923_wz5zt5_k$(t), this._set_patch__1153706493_m9y3rp_k$(e), this._set_dispose__3960191302_ui6ius_k$(_), this
                }, f.$metadata$ = {simpleName: "RuiBlock", kind: "class", interfaces: []}, l.prototype._set_create__3225312923_wz5zt5_k$ = function (t) {
                    this.create_2 = t
                }, l.prototype._get_create__3313892751_g83typ_k$ = function () {
                    return this.create_2
                }, l.prototype._set_patch__1153706493_m9y3rp_k$ = function (t) {
                    this.patch_2 = t
                }, l.prototype._get_patch__3511868553_cy8iuv_k$ = function () {
                    return this.patch_2
                }, l.prototype._set_dispose__3960191302_ui6ius_k$ = function (t) {
                    this.dispose_2 = t
                }, l.prototype._get_dispose__2411198674_v5jp66_k$ = function () {
                    return this.dispose_2
                }, l.$metadata$ = {simpleName: "RuiBranch", kind: "class", interfaces: []}, h.prototype._get_func__797377543_d6qkg7_k$ = function () {
                    return this.func_1
                }, h.$metadata$ = {simpleName: "RuiFunWrapper", kind: "class", interfaces: []}, new p(1), r(), t
            }) ? n.apply(e, o) : n) || (t.exports = r)
        }
    }, e = {}, function _(n) {
        var o = e[n];
        if (void 0 !== o) return o.exports;
        var r = e[n] = {exports: {}};
        return t[n].call(r.exports, r, r.exports, _), r.exports
    }(148);
    var t, e
}));
//# sourceMappingURL=rui-core.js.map
```