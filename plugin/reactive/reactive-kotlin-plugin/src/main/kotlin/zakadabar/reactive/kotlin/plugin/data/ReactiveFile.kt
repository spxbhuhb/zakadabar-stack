/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.data

import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrSymbol

class ReactiveFile(
    val irFile: IrFile
) {
    val functions = mutableMapOf<IrSymbol, ReactiveFunction>()

    fun add(irFunction: IrFunction): ReactiveFunction {
        return functions[irFunction.symbol] ?: ReactiveFunction(irFunction).also { functions[irFunction.symbol] = it }
    }

    fun dump(): String {
        val lines = mutableListOf<String>()
        lines += "file: ${irFile.symbol}"
        functions.forEach {
            lines += it.value.dump()
        }
        return lines.joinToString("\n")
    }
}