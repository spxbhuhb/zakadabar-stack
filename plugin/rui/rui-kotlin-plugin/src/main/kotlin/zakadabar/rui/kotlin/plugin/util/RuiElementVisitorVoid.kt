/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

import zakadabar.rui.kotlin.plugin.model.*

interface RuiElementVisitorVoid<out R> : RuiElementVisitor<R, Nothing?> {

    fun visitElement(element: RuiElement): R
    override fun visitElement(element: RuiElement, data: Nothing?) = visitElement(element)

    fun visitClass(ruiClass: RuiClass) = visitElement(ruiClass)
    override fun visitClass(ruiClass: RuiClass, data: Nothing?) = visitClass(ruiClass)

    fun visitStateVariable(stateVariable: RuiStateVariable) = visitElement(stateVariable)
    override fun visitStateVariable(stateVariable: RuiStateVariable, data: Nothing?) = visitStateVariable(stateVariable)

    fun visitExternalStateVariable(stateVariable: RuiExternalStateVariable) = visitElement(stateVariable)
    override fun visitExternalStateVariable(stateVariable: RuiExternalStateVariable, data: Nothing?) = visitExternalStateVariable(stateVariable)

    fun visitInternalStateVariable(stateVariable: RuiInternalStateVariable) = visitElement(stateVariable)
    override fun visitInternalStateVariable(stateVariable: RuiInternalStateVariable, data: Nothing?) = visitInternalStateVariable(stateVariable)

    fun visitDirtyMask(dirtyMask: RuiDirtyMask) = visitElement(dirtyMask)
    override fun visitDirtyMask(dirtyMask: RuiDirtyMask, data: Nothing?) = visitDirtyMask(dirtyMask)

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

    fun visitValueArgument(valueArgument: RuiValueArgument) = visitExpression(valueArgument)
    override fun visitValueArgument(valueArgument: RuiValueArgument, data : Nothing?) = visitValueArgument(valueArgument)

    fun visitDeclaration(declaration: RuiDeclaration) = visitElement(declaration)
    override fun visitDeclaration(declaration: RuiDeclaration, data: Nothing?) = visitDeclaration(declaration)

    fun visitBranch(branch: RuiBranch) = visitElement(branch)
    override fun visitBranch(branch: RuiBranch, data: Nothing?) = visitBranch(branch)

}