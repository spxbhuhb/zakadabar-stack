/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import zakadabar.rui.kotlin.plugin.transform.builders.RuiEntryPointBuilder
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiEntryPoint(
    val ruiClass: RuiClass,
    val irFunction: IrSimpleFunction,
) : RuiElement {

    val builder = RuiEntryPointBuilder(ruiClass.builder, this)

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitEntryPoint(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) {

    }
}