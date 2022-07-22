/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.transform.builders.RuiPropertyBuilder
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

class RuiDirtyMask(
    val ruiClass: RuiClass,
    val index : Int
) : RuiElement {

    val name = Name.identifier("\$dirty$index")

    val builder = RuiPropertyBuilder(ruiClass.builder, name, ruiClass.builder.irBuiltIns.intType)

    override fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R =
        visitor.visitDirtyMask(this, data)

    override fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D) = Unit

}