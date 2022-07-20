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

    fun visitStatement(statement: RuiStatement) = visitElement(statement)
    override fun visitStatement(statement: RuiStatement, data: Nothing?) = visitStatement(statement)

    fun visitBlock(statement: RuiBlock) = visitElement(statement)
    override fun visitBlock(statement: RuiBlock, data: Nothing?) = visitBlock(statement)

    fun visitCall(statement: RuiCall) = visitElement(statement)
    override fun visitCall(statement: RuiCall, data: Nothing?) = visitCall(statement)

    fun visitWhen(statement: RuiWhen) = visitElement(statement)
    override fun visitWhen(statement: RuiWhen, data: Nothing?) = visitWhen(statement)

    fun visitForLoop(statement: RuiForLoop) = visitElement(statement)
    override fun visitForLoop(statement: RuiForLoop, data: Nothing?) = visitForLoop(statement)

    fun visitExpression(expression: RuiExpression) = visitElement(expression)
    override fun visitExpression(expression: RuiExpression, data: Nothing?) = visitExpression(expression)

    fun visitDeclaration(declaration: RuiDeclaration) = visitElement(declaration)
    override fun visitDeclaration(declaration: RuiDeclaration, data: Nothing?) = visitDeclaration(declaration)

    fun visitBranch(branch: RuiBranch) = visitElement(branch)
    override fun visitBranch(branch: RuiBranch, data: Nothing?) = visitBranch(branch)

}