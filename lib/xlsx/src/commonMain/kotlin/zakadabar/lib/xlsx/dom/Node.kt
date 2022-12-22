/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

open class Node (
    val name : String,
    val text : String?,
    vararg attributes : Pair<String, String>
) {
    val attributes = mutableMapOf<String, String>()
    val elements = mutableListOf<Node>()

    init {
        this.attributes.putAll(attributes)
    }

    constructor(
        name: String,
        vararg attributes : Pair<String, String>,
        builder : (Node.()->Unit)? = null
    ) : this(name, null, *attributes) {
        builder?.invoke(this)
    }

    fun isEmpty() : Boolean = elements.isEmpty() && text == null

    operator fun plusAssign(el: Node) {
        elements += el
    }
    operator fun <T: Node> T.unaryPlus() : T = also(this@Node.elements::add)

}
