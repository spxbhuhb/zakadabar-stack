/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

import zakadabar.rui.kotlin.plugin.util.DumpRuiTreeVisitor
import zakadabar.rui.kotlin.plugin.util.RuiElementVisitor

interface RuiElement {
    fun <R, D> accept(visitor: RuiElementVisitor<R, D>, data: D): R

    fun <D> acceptChildren(visitor: RuiElementVisitor<Unit, D>, data: D)

    fun dump() : String {
        val out = StringBuilder()
        this.accept(DumpRuiTreeVisitor(out), null)
        return out.toString()
    }

}