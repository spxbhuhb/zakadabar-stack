/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.data

import org.jetbrains.kotlin.ir.expressions.IrCall

class RuiCallComponent(
    val irCall: IrCall,
    index: Int
) : RuiCompilationBase(index) {
    override fun dump(): String {
        val lines = mutableListOf<String>()
        lines += "    ${index.toString().padStart(5)}  ${irCall.startOffset.toString().padStart(5)}  ${irCall.symbol}"
        return lines.joinToString("\n")
    }
}