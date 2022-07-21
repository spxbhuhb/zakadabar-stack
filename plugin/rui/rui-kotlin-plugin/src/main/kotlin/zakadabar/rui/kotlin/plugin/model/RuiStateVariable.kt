/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

interface RuiStateVariable : RuiElement {

    val ruiClass: RuiClass
    val index: Int
    val originalName : String
    val name : String

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitStateVariable(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) = Unit

}