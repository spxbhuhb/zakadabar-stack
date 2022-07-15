/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName
import zakadabar.rui.kotlin.plugin.builder.RuiClassCompilation
import zakadabar.rui.kotlin.plugin.lower.toRuiClassName

class RuiPluginContext(
    val annotations : List<String>,
    val dumpPoints : List<String>
) {

    lateinit var irPluginContext: IrPluginContext

    val classBuilders = mutableMapOf<String, RuiClassCompilation>()

    fun ruiClassFor(irFunction : IrFunction) : IrClass {
        val segments = irFunction.kotlinFqName.pathSegments()
        segments[segments.lastIndex] = segments.last().toRuiClassName()
        val classFqName = FqName.fromSegments(segments.map { it.toString() })
        return checkNotNull(classBuilders[classFqName.asString()]?.irClass) { "missing Rui class for ${irFunction.symbol}" }
    }

    fun dump(point : String, element : IrElement) {
        if (point in dumpPoints) println(element.dump())
    }

    fun dumpBoundary(declaration: IrFunction, boundary: Int) {
        if (DUMP_BOUNDARY in dumpPoints) {
            println("${declaration.fqNameWhenAvailable} BOUNDARY = $boundary")
        }
    }

    companion object {
        const val DUMP_BEFORE = "before"
        const val DUMP_AFTER = "after"
        const val DUMP_RCD = "rcd"
        const val DUMP_BOUNDARY = "boundary"
    }

}