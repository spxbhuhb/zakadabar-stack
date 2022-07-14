/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.data

import org.jetbrains.kotlin.ir.expressions.IrBranch

class RuiBranchComponent(
    val irBranch: IrBranch,
    index: Int
) : RuiCompilationBase(index) {
    override fun dump(): String {
        val lines = mutableListOf<String>()
        lines += "    ${index.toString().padStart(5)}  ${irBranch.startOffset.toString().padStart(5)}  branch"
        return lines.joinToString("\n")
    }
}