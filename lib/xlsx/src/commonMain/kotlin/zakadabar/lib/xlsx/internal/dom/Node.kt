/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.dom

/**
 * Simple Dom Node implementation
 *
 * create text node:
 * @constructor(name, text)
 *
 */
internal open class Node protected constructor(
    val name: String,
    val text: String? = null,
) {
    val attributes = mutableMapOf<String, String>()
    val childNodes = mutableListOf<Node>()
    val size: Int get() = childNodes.size

    fun isEmpty() : Boolean = childNodes.isEmpty() && text == null
    fun hasNoAttribute() : Boolean = attributes.isEmpty()

    operator fun set(key : String, value : String) { attributes[key] = value }

    operator fun plusAssign(n: Node) { childNodes += n }

    operator fun minusAssign(n: Node) { childNodes -= n }

    operator fun <T: Node> T.unaryPlus() : T = also(this@Node.childNodes::add)

    companion object {

        /**
         * create a new Node object
         *
         *      node("foo") {
         *          this["attr"] = "value"
         *          + node("bar", "text")
         *      }
         *
         */
        fun node(
            name: String,
            text: String? = null,
            builder: (Node.() -> Unit)? = null
        ) : Node {
            val n = Node(name, text)
            builder?.invoke(n)
            return n
        }

    }
}

