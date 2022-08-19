/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

import org.jetbrains.kotlin.utils.Printer
import zakadabar.rui.kotlin.plugin.model.*

class DumpRuiTreeVisitor(
    out: Appendable
) : RuiElementVisitorVoid<Unit> {

    private val printer = Printer(out, "  ")

    override fun visitElement(element: RuiElement) {
        element.acceptChildren(this, null)
    }

    override fun visitEntryPoint(ruiEntryPoint: RuiEntryPoint) {
        indented {
            with(ruiEntryPoint) {
                println { "ENTRY_POINT class:${ruiEntryPoint.ruiClass.name}" }
            }
            super.visitEntryPoint(ruiEntryPoint)
        }
    }

    override fun visitClass(ruiClass: RuiClass) {
        indented {
            with(ruiClass) {
                println { "CLASS name:${irClass.name} boundary:$boundary" }
            }
            super.visitClass(ruiClass)
        }
    }

    override fun visitExternalStateVariable(stateVariable: RuiExternalStateVariable) {
        indented {
            with(stateVariable) {
                println { "EXTERNAL_STATE_VARIABLE index:$index name:$name" }
            }
            super.visitStateVariable(stateVariable)
        }
    }

    override fun visitInternalStateVariable(stateVariable: RuiInternalStateVariable) {
        indented {
            with(stateVariable) {
                println { "INTERNAL_STATE_VARIABLE index:$index name:$name" }
            }
            super.visitStateVariable(stateVariable)
        }
    }

    override fun visitDirtyMask(dirtyMask: RuiDirtyMask) {
        indented {
            with(dirtyMask) {
                println { "DIRTY_MASK index:$index name:$name" }
            }
            super.visitDirtyMask(dirtyMask)
        }
    }

    override fun visitCall(statement: RuiCall) {
        indented {
            with(statement) {
                println { "CALL index:$index name:$name type:<$targetRuiClass>" }
            }
            super.visitCall(statement)
        }
    }

    override fun visitWhen(statement: RuiWhen) {
        indented {
            with(statement) {
                println { "WHEN index:$index name:$name" }
            }
            super.visitWhen(statement)
        }
    }

    override fun visitForLoop(statement: RuiForLoop) {
        indented {
            with(statement) {
                println { "FOR_LOOP index:$index name:$name" }
            }
            super.visitForLoop(statement)
        }
    }

    override fun visitBranch(branch: RuiBranch) {
        indented {
            with(branch) {
                println { "BRANCH index:$index name:$name" }
            }
            super.visitBranch(branch)
        }
    }

    override fun visitExpression(expression: RuiExpression) {
        indented {
            with(expression) {
                println { "$origin ${dependencies.withLabel("dependencies")}" }
            }
            super.visitExpression(expression)
        }
    }

    override fun visitValueArgument(valueArgument: RuiValueArgument) {
        indented {
            with(valueArgument) {
                println { "$origin $index ${dependencies.withLabel("dependencies")}" }
            }
        }
    }

    override fun visitDeclaration(declaration: RuiDeclaration) {
        indented {
            with(declaration) {
                println { "$origin ${dependencies.withLabel("dependencies")}" }
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