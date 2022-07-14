/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import zakadabar.rui.kotlin.plugin.data.RuiCompilation

class RuiPluginContext(
    val annotations : List<String>,
    val dumpPoints : List<String>
) {

    lateinit var irPluginContext: IrPluginContext
    val controlData = RuiCompilation()

    fun dump(point : String, element : IrElement) {
        if (point in dumpPoints) println(element.dump())
    }

    fun dump(point : String, data : RuiCompilation) {
        if (point in dumpPoints) println(data.dump())
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