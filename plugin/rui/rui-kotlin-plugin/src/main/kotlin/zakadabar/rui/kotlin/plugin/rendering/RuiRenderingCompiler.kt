/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.rendering

import zakadabar.rui.kotlin.plugin.RuiPluginContext
import zakadabar.rui.kotlin.plugin.builder.RuiClass

class RuiRenderingCompiler(
    val ruiPluginContext: RuiPluginContext
) {
    fun compileClasses() {
        ruiPluginContext.ruiClasses.forEach {
            compileClass(it.value)
        }
    }

    fun compileClass(ruiClass : RuiClass) {
        RuiFromIrTransform(ruiClass).transformClass(ruiClass)
    }
}