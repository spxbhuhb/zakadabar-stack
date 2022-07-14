/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.data

import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

class ReactiveFunction(
    val irFunction: IrFunction
) {

    val className
        get() = "Reactive" + irFunction.name.identifier.capitalizeAsciiOnly()

    val variables = mutableMapOf<Int, ReactiveVariable>()
    val components = mutableMapOf<Int, ReactiveComponentBase>()

    fun add(irVariable: IrVariable): ReactiveVariable {
        return ReactiveVariable(irVariable, variables.size)
            .also {
                variables[irVariable.startOffset] = it
            }
    }

    fun add(irCall: IrCall): ReactiveCallComponent {
        return ReactiveCallComponent(irCall, components.size)
            .also {
                components[irCall.startOffset] = it
            }
    }

    fun add(irBranch: IrBranch): ReactiveBranchComponent {
        return ReactiveBranchComponent(irBranch, components.size)
            .also {
                components[irBranch.startOffset] = it
            }
    }

    fun add(irLoop: IrLoop): ReactiveLoopComponent {
        return ReactiveLoopComponent(irLoop, components.size)
            .also {
                components[irLoop.startOffset] = it
            }
    }

    fun dump(): String {
        val lines = mutableListOf<String>()
        lines += irFunction.symbol.toString()
        lines += "  variables:"
        variables.forEach { lines += it.value.dump() }
        lines += "  components:"
        components.forEach { lines += it.value.dump() }
        return lines.joinToString("\n")
    }
}