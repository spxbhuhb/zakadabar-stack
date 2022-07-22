/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.parentOrNull
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui
import zakadabar.rui.kotlin.plugin.model.RuiClass

fun Name.toRuiClassName(): Name {
    return Name.identifier("Rui" + identifier.capitalizeAsciiOnly())
}

fun IrFunction.toRuiClassFqName(): FqName {
    return (kotlinFqName.parentOrNull() ?: FqName.ROOT).child(name.toRuiClassName())
}

class RuiCompilationException(error: ErrorsRui.RuiIrError, ruiClass: RuiClass, irCall: IrCall) : Exception() {
    init {
        error.report(ruiClass, irCall)
    }
}