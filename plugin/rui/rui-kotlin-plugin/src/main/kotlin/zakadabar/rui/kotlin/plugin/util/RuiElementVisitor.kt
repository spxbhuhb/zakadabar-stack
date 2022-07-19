/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

import zakadabar.rui.kotlin.plugin.builder.*

interface RuiElementVisitor<out R, in D> {
    fun visitElement(element: RuiElement, data: D): R
    fun visitClass(ruiClass: RuiClass, data: D) = visitElement(ruiClass, data)
    fun visitCallBlock(ruiCallBlock: RuiCallBlock, data : D) = visitElement(ruiCallBlock, data)
    fun visitCallParameter(ruiCallParameter: RuiCallParameter, data : D) = visitElement(ruiCallParameter, data)
    fun visitBranchBlock(ruiBranchBlock: RuiBranchBlock, data : D) = visitElement(ruiBranchBlock, data)
    fun visitLoopBlock(ruiLoopBlock: RuiLoopBlock, data : D) = visitElement(ruiLoopBlock, data)
    fun visitRenderingSlot(ruiRenderingSlot: RuiRenderingSlot, data : D) = visitElement(ruiRenderingSlot, data)
    fun visitStateVariable(ruiStateVariable: RuiStateVariable, data : D) = visitElement(ruiStateVariable, data)
    fun visitDirtyMask(ruiDirtyMask: RuiDirtyMask, data : D) = visitElement(ruiDirtyMask, data)
}