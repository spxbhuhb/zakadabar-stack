/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

import zakadabar.rui.kotlin.plugin.builder.*

interface RuiElementVisitorVoid<out R> : RuiElementVisitor<R, Nothing?> {

    fun visitElement(element: RuiElement): R
    override fun visitElement(element: RuiElement, data: Nothing?) = visitElement(element)

    fun visitClass(ruiClass: RuiClass) = visitElement(ruiClass)
    override fun visitClass(ruiClass: RuiClass, data: Nothing?) = visitClass(ruiClass)

    fun visitStateVariable(ruiStateVariable: RuiStateVariable) = visitElement(ruiStateVariable)
    override fun visitStateVariable(ruiStateVariable: RuiStateVariable, data: Nothing?) = visitStateVariable(ruiStateVariable)

    fun visitDirtyMask(ruiDirtyMask: RuiDirtyMask) = visitElement(ruiDirtyMask)
    override fun visitDirtyMask(ruiDirtyMask: RuiDirtyMask, data: Nothing?) = visitDirtyMask(ruiDirtyMask)

    fun visitRenderingSlot(ruiRenderingSlot: RuiRenderingSlot) = visitElement(ruiRenderingSlot)
    override fun visitRenderingSlot(ruiRenderingSlot: RuiRenderingSlot, data: Nothing?) = visitRenderingSlot(ruiRenderingSlot)

    fun visitCallBlock(ruiCallBlock: RuiCallBlock) = visitElement(ruiCallBlock)
    override fun visitCallBlock(ruiCallBlock: RuiCallBlock, data: Nothing?) = visitCallBlock(ruiCallBlock)

    fun visitCallParameter(ruiCallParameter: RuiCallParameter) = visitElement(ruiCallParameter)
    override fun visitCallParameter(ruiCallParameter: RuiCallParameter, data: Nothing?) = visitCallParameter(ruiCallParameter)

    fun visitBranchBlock(ruiBranchBlock: RuiBranchBlock) = visitElement(ruiBranchBlock)
    override fun visitBranchBlock(ruiBranchBlock: RuiBranchBlock, data: Nothing?) = visitBranchBlock(ruiBranchBlock)

    fun visitLoopBlock(ruiLoopBlock: RuiLoopBlock) = visitElement(ruiLoopBlock)
    override fun visitLoopBlock(ruiLoopBlock: RuiLoopBlock, data: Nothing?) = visitLoopBlock(ruiLoopBlock)

}