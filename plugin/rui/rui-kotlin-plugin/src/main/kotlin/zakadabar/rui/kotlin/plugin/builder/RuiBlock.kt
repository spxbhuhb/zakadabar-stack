/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

abstract class RuiBlock(
    val ruiClass: RuiClass,
    val name : String
) : RuiElement {
    val dependencies = mutableListOf<Int>()
    val renderingSlots = mutableMapOf<String, RuiRenderingSlot>()

    abstract fun transform()

}