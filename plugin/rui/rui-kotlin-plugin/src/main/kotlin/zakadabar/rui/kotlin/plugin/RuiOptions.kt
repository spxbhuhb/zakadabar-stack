/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

class RuiOptions(
    val annotations: List<String>,
    val dumpPoints: List<RuiDumpPoint>,
    val rootNameStrategy : RuiRootNameStrategy,
    val withTrace : Boolean,
    val exportState : Boolean,
    val importState : Boolean
)