/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.dom

/**
 * Simple Dom Node implementation
 *
 * create text node:
 * @constructor(name, text, builder)
 *
 *
 * + Node("foo") {
 *   this["attr"] = "value"
 *   + Node("bar")
 * }
 *
 */
internal open class Node(
    val name: String,
    val text: String? = null,
    builder: (Node.() -> Unit)? = null
) {
    val attributes = mutableMapOf<String, String>()
    val childNodes = mutableListOf<Node>()
    val size: Int get() = childNodes.size

    init {
        builder?.invoke(this)
    }

    fun isEmpty() : Boolean = childNodes.isEmpty() && text == null
    fun hasNoAttribute() : Boolean = attributes.isEmpty()

    operator fun set(key : String, value : String) { attributes[key] = value }

    operator fun plusAssign(n: Node) { childNodes += n }

    operator fun minusAssign(n: Node) { childNodes -= n }

    operator fun <T: Node> T.unaryPlus() : T = also(this@Node.childNodes::add)


}
