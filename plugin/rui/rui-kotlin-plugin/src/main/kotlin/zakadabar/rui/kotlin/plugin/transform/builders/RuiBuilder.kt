/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.transform.builders

import zakadabar.rui.kotlin.plugin.RuiPluginContext

interface RuiBuilder {

    val ruiContext : RuiPluginContext

    val irContext
        get() = ruiContext.irPluginContext

    val irFactory
        get() = irContext.irFactory

    val irBuiltIns
        get() = irContext.irBuiltIns

}