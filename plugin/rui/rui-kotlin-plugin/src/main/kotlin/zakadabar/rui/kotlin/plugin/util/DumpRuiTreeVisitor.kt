/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

import org.jetbrains.kotlin.utils.Printer
import zakadabar.rui.kotlin.plugin.builder.*

class DumpRuiTreeVisitor(
    out: Appendable
) : RuiElementVisitorVoid<Unit> {

    private val printer = Printer(out, "  ")

    override fun visitElement(element: RuiElement) {
        element.acceptChildren(this, null)
    }

    override fun visitClass(ruiClass: RuiClass) {
        indented {
            with(ruiClass) {
                println { "CLASS name:$name boundary:$boundary" }
            }
            super.visitClass(ruiClass)
        }
    }

    override fun visitStateVariable(ruiStateVariable: RuiStateVariable) {
        indented {
            with(ruiStateVariable) {
                println { "STATE_VARIABLE index:$index name:$name" }
            }
            super.visitStateVariable(ruiStateVariable)
        }
    }

    override fun visitDirtyMask(ruiDirtyMask: RuiDirtyMask) {
        indented {
            with(ruiDirtyMask) {
                println { "DIRTY_MASK index:$index name:$name" }
            }
            super.visitDirtyMask(ruiDirtyMask)
        }
    }

    override fun visitRenderingSlot(ruiRenderingSlot: RuiRenderingSlot) {
        indented {
            with(ruiRenderingSlot) {
                println { "RENDERING_SLOT index:$index name:$name" }
            }
            super.visitRenderingSlot(ruiRenderingSlot)
        }
    }

    override fun visitCall(statement: RuiCall) {
        indented {
            with(statement) {
                println { "BLOCK type:CALL index:$index name:$name targetRuiClass:<$targetRuiClass>" }
            }
            super.visitCall(statement)
        }
    }

    override fun visitWhen(statement: RuiWhen) {
        indented {
            with(statement) {
                println { "BLOCK type:WHEN index:$index name:$name" }
            }
            super.visitWhen(statement)
        }
    }

    override fun visitForLoop(statement: RuiForLoop) {
        indented {
            with(statement) {
                println { "BLOCK type:FOR_LOOP index:$index name:$name" }
            }
            super.visitForLoop(statement)
        }
    }

    override fun visitBranch(branch: RuiBranch) {
        indented {
            with(branch) {
                println { "BRANCH index:$index" }
            }
            super.visitBranch(branch)
        }
    }

    override fun visitExpression(expression: RuiExpression) {
        indented {
            with(expression) {
                println { "EXPRESSION index:$index ${expression.dependencies.withLabel("dependencies")}" }
            }
            super.visitExpression(expression)
        }
    }

    override fun visitDeclaration(declaration: RuiDeclaration) {
        indented {
            with(declaration) {
                println { "DECLARATION index:$index ${declaration.dependencies.withLabel("dependencies")}" }
            }
            super.visitDeclaration(declaration)
        }
    }


    private inline fun println(body: () -> String) {
        printer.println(body())
    }

    private inline fun indented(body: () -> Unit) {
        printer.pushIndent()
        body()
        printer.popIndent()
    }

    fun List<RuiStateVariable>.withLabel(label: String) =
        "$label:[${this.joinToString(", ") { it.index.toString() }}]"

}