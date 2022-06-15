/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.lower

import org.jetbrains.kotlin.ir.symbols.IrSymbol

class ReactiveControlData {
    val functions = mutableMapOf<IrSymbol, RcdFunction>()

    fun dump(): String {
        val lines = mutableListOf<String>()
        functions.forEach {
            lines += it.value.dump()
        }
        return lines.joinToString("\n")
    }
}

/**
 * Represents:
 *
 * - functions marked with the `@Reactive` annotation
 * - anonymous functions passed in a parameter marked with the `@Reactive` annotation
 * - loops in functions tha fall into the two categories above
 */
class RcdFunction(
    val symbol : IrSymbol
) {

    val slots = mutableListOf<RcdSlot>()

    fun dump(): String {
        val lines = mutableListOf<String>()
        lines += symbol.toString()
        lines += "  Slots:"
        slots.forEach { lines += it.dump() }
        return lines.joinToString("\n")
    }
}

class RcdSlot(
    val renderFunction : IrSymbol,
    val callSiteOffset : Int,
    val index : Int
) {
    fun dump(): String {
        val lines = mutableListOf<String>()
        lines += "    ${index.toString().padStart(5)}  ${callSiteOffset.toString().padStart(5)}  $renderFunction"
        return lines.joinToString("\n")
    }
}