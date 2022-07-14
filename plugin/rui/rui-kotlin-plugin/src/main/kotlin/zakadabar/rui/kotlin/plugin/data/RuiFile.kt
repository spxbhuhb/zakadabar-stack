/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.data

import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrSymbol

class RuiFile(
    val irFile: IrFile
) {
    val functions = mutableMapOf<IrSymbol, RuiFunction>()

    fun add(irFunction: IrFunction): RuiFunction {
        return functions[irFunction.symbol] ?: RuiFunction(irFunction).also { functions[irFunction.symbol] = it }
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