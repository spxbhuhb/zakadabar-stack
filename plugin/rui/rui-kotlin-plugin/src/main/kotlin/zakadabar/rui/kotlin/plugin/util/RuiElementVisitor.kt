/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

import zakadabar.rui.kotlin.plugin.model.*

interface RuiElementVisitor<out R, in D> {
    fun visitElement(element: RuiElement, data: D): R

    fun visitClass(ruiClass: RuiClass, data: D) = visitElement(ruiClass, data)

    fun visitStateVariable(stateVariable: RuiStateVariable, data : D) = visitElement(stateVariable, data)
    fun visitExternalStateVariable(stateVariable: RuiExternalStateVariable, data : D) = visitStateVariable(stateVariable, data)
    fun visitInternalStateVariable(stateVariable: RuiInternalStateVariable, data : D) = visitStateVariable(stateVariable, data)

    fun visitDirtyMask(dirtyMask: RuiDirtyMask, data : D) = visitElement(dirtyMask, data)

    fun visitStatement(statement: RuiStatement, data : D) = visitElement(statement, data)
    fun visitBlock(statement : RuiBlock, data : D) = visitStatement(statement, data)
    fun visitCall(statement: RuiCall, data : D) = visitStatement(statement, data)
    fun visitWhen(statement: RuiWhen, data : D) = visitStatement(statement, data)
    fun visitForLoop(statement: RuiForLoop, data : D) = visitStatement(statement, data)

    fun visitExpression(expression: RuiExpression, data : D) = visitElement(expression, data)
    fun visitValueArgument(valueArgument: RuiValueArgument, data : D) = visitElement(valueArgument, data)

    fun visitDeclaration(declaration : RuiDeclaration, data : D) = visitElement(declaration, data)

    fun visitBranch(branch: RuiBranch, data : D) = visitElement(branch, data)

}