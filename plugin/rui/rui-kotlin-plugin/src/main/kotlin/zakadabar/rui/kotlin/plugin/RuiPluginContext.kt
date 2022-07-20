/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.util.IrMessageLogger
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.name.FqName
import zakadabar.rui.kotlin.plugin.builder.RuiClass

class RuiPluginContext(
    val annotations : List<String>,
    val dumpPoints : List<String>
) {

    lateinit var irPluginContext: IrPluginContext
    lateinit var diagnosticReporter : IrMessageLogger

    val ruiClasses = mutableMapOf<FqName, RuiClass>()

    fun dump(point : String, element : IrElement) {
        if (point in dumpPoints) println(element.dump())
    }

    companion object {
        const val DUMP_BEFORE = "before"
        const val DUMP_AFTER = "after"
        const val DUMP_RUI_TREE = "rui-tree"
    }

}