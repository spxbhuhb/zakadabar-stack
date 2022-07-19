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
            with (ruiStateVariable) {
                println { "STATE VARIABLE index:$index name:$name" }
            }
            super.visitStateVariable(ruiStateVariable)
        }
    }

    override fun visitDirtyMask(ruiDirtyMask: RuiDirtyMask) {
        indented {
            with(ruiDirtyMask) {
                println { "DIRTY MASK index:$index"}
            }
            super.visitDirtyMask(ruiDirtyMask)
        }
    }

    override fun visitRenderingSlot(ruiRenderingSlot: RuiRenderingSlot) {
        indented {
            with(ruiRenderingSlot) {
                println { "RENDERING SLOT index:$index name:$name"}
            }
            super.visitRenderingSlot(ruiRenderingSlot)
        }
    }

    override fun visitCallBlock(ruiCallBlock: RuiCallBlock) {
        indented {
            with(ruiCallBlock) {
                println { "CALL BLOCK name:$name"}
            }
            super.visitCallBlock(ruiCallBlock)
        }
    }

    override fun visitCallParameter(ruiCallParameter: RuiCallParameter) {
        indented {
            with(ruiCallParameter) {
                println { "CALL PARAMETER dependencies:${dependencies.joinToString(", ") { it.toString() }}" }
            }
            super.visitCallParameter(ruiCallParameter)
        }
    }

    override fun visitBranchBlock(ruiBranchBlock: RuiBranchBlock) {
        indented {
            with(ruiBranchBlock) {
                println { "BRANCH BLOCK name:$name"}
            }
            super.visitBranchBlock(ruiBranchBlock)
        }
    }

    override fun visitLoopBlock(ruiLoopBlock: RuiLoopBlock) {
        indented {
            with(ruiLoopBlock) {
                println { "LOOP BLOCK name:$name"}
            }
            super.visitLoopBlock(ruiLoopBlock)
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

}