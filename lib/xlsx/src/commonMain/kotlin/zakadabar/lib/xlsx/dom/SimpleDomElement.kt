/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

open class SimpleDomElement(
    val name : String,
    val text : String? = null
) {
    val attributes = mutableMapOf<String, String>()
    val childNodes = mutableListOf<SimpleDomElement>()

    operator fun plusAssign(child: SimpleDomElement) {
        childNodes += child
    }

    companion object {

        fun of(name: String, vararg attributes : Pair<String, String>) = SimpleDomElement(name).also {
            it.attributes.putAll(attributes)
        }

    }
}

