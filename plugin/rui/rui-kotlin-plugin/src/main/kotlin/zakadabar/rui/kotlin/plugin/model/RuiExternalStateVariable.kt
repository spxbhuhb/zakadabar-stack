/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import zakadabar.rui.kotlin.plugin.transform.builders.RuiStateVariableBuilder
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiExternalStateVariable(
    override val ruiClass: RuiClass,
    override val index: Int,
    val irValueParameter: IrValueParameter
) : RuiStateVariable {

    override val originalName = irValueParameter.name.identifier
    override val name = irValueParameter.name

    override val builder = RuiStateVariableBuilder.builderFor(ruiClass.builder, this)

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitExternalStateVariable(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) = Unit

}