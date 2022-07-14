/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.kotlin.plugin.data

abstract class ReactiveComponentBase(
    val index: Int
) {
    abstract fun dump(): String
}