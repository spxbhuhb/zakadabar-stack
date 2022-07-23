/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.model

import org.jetbrains.kotlin.name.Name
import zakadabar.rui.kotlin.plugin.transform.builders.RuiStateVariableBuilder

interface RuiStateVariable : RuiElement {

    val ruiClass: RuiClass
    val index: Int
    val originalName : String
    val name : Name

    val builder : RuiStateVariableBuilder

}