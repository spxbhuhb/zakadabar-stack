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
    FUN name:a visibility:public modality:FINAL <> (b:kotlin.Int) returnType:kotlin.Unit
      VALUE_PARAMETER name:b index:0 type:kotlin.Int
      BLOCK_BODY
        WHEN type=kotlin.Unit origin=IF
          BRANCH
            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
              arg0: GET_VAR 'b: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.a' type=kotlin.Int origin=null
              arg1: CONST Int type=kotlin.Int value=8
            then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
        WHEN type=kotlin.Unit origin=IF
          BRANCH
            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
              arg0: GET_VAR 'b: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.a' type=kotlin.Int origin=null
              arg1: CONST Int type=kotlin.Int value=9
            then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
          BRANCH
            if: CONST Boolean type=kotlin.Boolean value=true
            then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
        WHEN type=kotlin.Unit origin=WHEN
          BRANCH
            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
              arg0: GET_VAR 'b: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.a' type=kotlin.Int origin=null
              arg1: CONST Int type=kotlin.Int value=1
            then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
          BRANCH
            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
              arg0: GET_VAR 'b: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.a' type=kotlin.Int origin=null
              arg1: CONST Int type=kotlin.Int value=2
            then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
        WHEN type=kotlin.Unit origin=WHEN
          BRANCH
            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
              arg0: GET_VAR 'b: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.a' type=kotlin.Int origin=null
              arg1: CONST Int type=kotlin.Int value=3
            then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
          BRANCH
            if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
              arg0: GET_VAR 'b: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.a' type=kotlin.Int origin=null
              arg1: CONST Int type=kotlin.Int value=4
            then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
          BRANCH
            if: CONST Boolean type=kotlin.Boolean value=true
            then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
        BLOCK type=kotlin.Unit origin=WHEN
          VAR IR_TEMPORARY_VARIABLE name:tmp0_subject type:kotlin.Int [val]
            GET_VAR 'b: kotlin.Int declared in zakadabar.rui.kotlin.plugin.adhoc.a' type=kotlin.Int origin=null
          WHEN type=kotlin.Unit origin=WHEN
            BRANCH
              if: CALL 'public final fun EQEQ (arg0: kotlin.Any?, arg1: kotlin.Any?): kotlin.Boolean declared in kotlin.internal.ir' type=kotlin.Boolean origin=EQEQ
                arg0: GET_VAR 'val tmp0_subject: kotlin.Int [val] declared in zakadabar.rui.kotlin.plugin.adhoc.a' type=kotlin.Int origin=null
                arg1: CONST Int type=kotlin.Int value=5
              then: GET_OBJECT 'CLASS IR_EXTERNAL_DECLARATION_STUB OBJECT name:Unit modality:FINAL visibility:public superTypes:[kotlin.Any]' type=kotlin.Unit
```