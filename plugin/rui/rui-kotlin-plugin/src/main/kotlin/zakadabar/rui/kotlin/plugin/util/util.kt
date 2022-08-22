/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.isAnonymousFunction
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.parentOrNull
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import zakadabar.rui.kotlin.plugin.diagnostics.ErrorsRui
import zakadabar.rui.kotlin.plugin.model.RuiClass

fun Name.isSynthetic() = identifier.startsWith('$') || identifier.endsWith('$')

fun IrFunction.toRuiClassFqName(): FqName {
    val parent = kotlinFqName.parentOrNull() ?: FqName.ROOT
    return when {
        this.isAnonymousFunction -> parent.child(Name.identifier("RuiRoot${this.startOffset}"))
        this.name.asString() == "<anonymous>" -> parent.child(Name.identifier("RuiRoot${this.startOffset}"))
        else -> parent.child(Name.identifier("Rui" + name.identifier.capitalizeAsciiOnly()))
    }
}

class RuiCompilationException(
    val error: ErrorsRui.RuiIrError,
    var ruiClass: RuiClass? = null,
    var irElement: IrElement? = null,
    val additionalInfo: String = ""
) : Exception() {

    var reported = false

    init {
        report()
    }

    fun report() {
        if (reported) return
        ruiClass?.let { c ->
            irElement?.let { e ->
                error.report(c, e, additionalInfo)
                reported = true
            }
        }
    }
}

val Any.traceName
    get() = "[${this.toString().padEnd(30)}]"

val Any.tracePoint
    get() = "  ${this.toString().padEnd(20)}  |"

fun traceLabel(name: Any, point: Any) =
    name.traceName + point.tracePoint

fun String.camelToSnakeCase(): String {
    var s = ""
    this.toCharArray().forEach {
        s += if (it in 'A'..'Z') "_${it.lowercaseChar()}" else it.toString()
    }
    return s.trimStart('_')
}