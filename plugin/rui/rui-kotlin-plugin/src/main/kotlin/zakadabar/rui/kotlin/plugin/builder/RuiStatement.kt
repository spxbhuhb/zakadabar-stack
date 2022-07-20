/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.builder

abstract class RuiStatement(
    val ruiClass: RuiClass,
    val index: Int,
) : RuiElement {

    val name : String = "block$index\$"

    fun transform() { }

}