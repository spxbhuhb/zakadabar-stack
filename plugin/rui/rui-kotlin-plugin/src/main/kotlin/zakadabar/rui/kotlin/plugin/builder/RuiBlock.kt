/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

abstract class RuiBlock(
    val ruiClass: RuiClass,
    val index: Int
) : RuiElement {
    val dependencies = mutableListOf<Int>()
    val renderingSlots = mutableMapOf<String, RuiRenderingSlot>()

    val name : String = "block$index\$"

    abstract fun transform()

}