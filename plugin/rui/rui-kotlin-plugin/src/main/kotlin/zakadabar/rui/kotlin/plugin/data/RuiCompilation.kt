/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.data

import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.util.file

class RuiCompilation {

    val files = mutableMapOf<IrSymbol, RuiFile>()
    val classes = mutableMapOf<IrSymbol, RuiClass>()

    fun add(file: IrFile): RuiFile {
        return files[file.symbol] ?: RuiFile(file).also { files[file.symbol] = it }
    }

    fun add(irFunction: IrFunction): RuiFunction {
        return add(irFunction.file).add(irFunction)
    }

    fun dump(): String {
        val lines = mutableListOf<String>()
        files.forEach {
            lines += it.value.dump()
        }
        return lines.joinToString("\n")
    }
}