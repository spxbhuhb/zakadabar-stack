/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.util.dump
import zakadabar.reactive.kotlin.plugin.data.ReactiveCompilation

class ReactivePluginContext(
    val annotations : List<String>,
    val dumpPoints : List<String>
) {

    lateinit var irPluginContext: IrPluginContext
    val controlData = ReactiveCompilation()

    fun dump(point : String, element : IrElement) {
        if (point in dumpPoints) println(element.dump())
    }

    fun dump(point : String, data : ReactiveCompilation) {
        if (point in dumpPoints) println(data.dump())
    }

    companion object {
        const val DUMP_BEFORE = "before"
        const val DUMP_AFTER = "after"
        const val DUMP_RCD = "rcd"
    }

}