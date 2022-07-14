/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.data

abstract class RuiCompilationBase(
    val index: Int
) {
    abstract fun dump(): String
}