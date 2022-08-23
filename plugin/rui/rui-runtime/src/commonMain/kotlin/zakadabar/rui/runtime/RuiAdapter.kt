/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

interface RuiAdapter<BT> {

    val rootBridge : RuiBridge<BT>

    fun createPlaceholder() : RuiBridge<BT>

    fun newId() : Int

    fun trace(name : String, point : String, vararg data : Any?) {
        // FIXME should we escape the data string? think about security
        println("[ ${name.padEnd(30)} ]  ${point.padEnd(20)}  |  ${data.joinToString(" ") { it.toString() }}")
    }
}