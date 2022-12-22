/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

open class Node (
    val name : String,
    val text : String?,
    attributes : Array<out Pair<String, String>>? = null
) {
    val attributes = mutableMapOf<String, String>()
    val elements = mutableListOf<Node>()

    init {
        attributes?.let(this.attributes::putAll)
    }

    constructor(
        name: String,
        attributes : Array<out Pair<String, String>>? = null,
        builder : (()->Unit)? = null
    ) : this(name, null, attributes) {
        builder?.invoke()
    }

    fun isEmpty() : Boolean = elements.isEmpty() && text == null

}
