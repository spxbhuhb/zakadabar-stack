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

    override fun visitCall(ruiCall: RuiCall) {
        indented {
            with(ruiCall) {
                println { "BLOCK type:CALL index:$index name:$name targetRuiClass:$targetRuiClass" }
            }
            super.visitCall(ruiCall)
        }
    }

    override fun visitCallParameter(ruiCallParameter: RuiCallParameter) {
        indented {
            with(ruiCallParameter) {
                println { "PARAMETER index:$index ${dependencies.withLabel("dependencies")}" }
            }
            super.visitCallParameter(ruiCallParameter)
        }
    }

    override fun visitWhen(ruiWhen: RuiWhen) {
        indented {
            with(ruiWhen) {
                println { "BLOCK type:WHEN index:$index name:$name" }
            }
            super.visitWhen(ruiWhen)
        }
    }

    override fun visitBranch(ruiBranch: RuiBranch) {
        indented {
            with(ruiBranch) {
                println {
                    listOf(
                        "BRANCH index:$index",
                        conditionDependencies.withLabel("conditionDependencies"),
                        bodyDependencies.withLabel("bodyDependencies")
                    ).joinToString(" ")
                }
                super.visitBranch(ruiBranch)
            }
        }
    }

    override fun visitLoop(ruiLoop: RuiLoop) {
        indented {
            with(ruiLoop) {
                println { "BLOCK type:LOOP index:$index name:$name" }
            }
            super.visitLoop(ruiLoop)
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

    fun List<Int>.withLabel(label: String) =
        "$label:[${this.joinToString(", ") { it.toString() }}]"

}