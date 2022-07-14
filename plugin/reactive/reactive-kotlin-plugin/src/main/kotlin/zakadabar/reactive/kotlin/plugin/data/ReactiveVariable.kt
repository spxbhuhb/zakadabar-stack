/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.data

import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrVariable

class ReactiveVariable(
    val irVariable: IrVariable,
    val index: Int
) {
    lateinit var field : IrField
    lateinit var property : IrProperty

    fun dump(): String {
        val lines = mutableListOf<String>()
        lines += "    ${index.toString().padStart(5)}  ${irVariable.startOffset.toString().padStart(5)}  ${irVariable.name}"
        return lines.joinToString("\n")
    }
}