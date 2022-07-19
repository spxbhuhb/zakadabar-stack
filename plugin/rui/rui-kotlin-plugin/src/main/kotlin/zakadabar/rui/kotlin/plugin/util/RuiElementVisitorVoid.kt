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

    fun visitCall(ruiCall: RuiCall) = visitElement(ruiCall)
    override fun visitCall(ruiCall: RuiCall, data: Nothing?) = visitCall(ruiCall)

    fun visitCallParameter(ruiCallParameter: RuiCallParameter) = visitElement(ruiCallParameter)
    override fun visitCallParameter(ruiCallParameter: RuiCallParameter, data: Nothing?) = visitCallParameter(ruiCallParameter)

    fun visitWhen(ruiWhen: RuiWhen) = visitElement(ruiWhen)
    override fun visitWhen(ruiWhen: RuiWhen, data: Nothing?) = visitWhen(ruiWhen)

    fun visitBranch(ruiBranch: RuiBranch) = visitElement(ruiBranch)
    override fun visitBranch(ruiBranch: RuiBranch, data: Nothing?) = visitBranch(ruiBranch)

    fun visitLoop(ruiLoop: RuiLoop) = visitElement(ruiLoop)
    override fun visitLoop(ruiLoop: RuiLoop, data: Nothing?) = visitLoop(ruiLoop)

}