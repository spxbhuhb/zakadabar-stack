/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.dom

internal open class Node (
    val name : String,
    val text : String?,
    vararg attributes : Pair<String, String>
) {
    val attributes = mutableMapOf(*attributes)
    val childNodes = mutableListOf<Node>()

    constructor(
        name: String,
        vararg attributes : Pair<String, String>,
        builder : (Node.()->Unit)? = null
    ) : this(name, null, *attributes) {
        builder?.invoke(this)
    }

    fun isEmpty() : Boolean = childNodes.isEmpty() && text == null

    operator fun plusAssign(n: Node) { childNodes += n }

    operator fun <T: Node> T.unaryPlus() : T = also(this@Node.childNodes::add)

}
