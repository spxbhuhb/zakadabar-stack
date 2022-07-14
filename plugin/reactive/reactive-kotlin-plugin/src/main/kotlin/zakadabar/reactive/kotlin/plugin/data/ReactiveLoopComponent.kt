/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.data

import org.jetbrains.kotlin.ir.expressions.IrLoop

class ReactiveLoopComponent(
    val irLoop: IrLoop,
    index: Int
) : ReactiveComponentBase(index) {
    override fun dump(): String {
        val lines = mutableListOf<String>()
        lines += "    ${index.toString().padStart(5)}  ${irLoop.startOffset.toString().padStart(5)}  loop"
        return lines.joinToString("\n")
    }
}