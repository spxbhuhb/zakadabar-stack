/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

fun <T:Node> Node.add(el: T) : T = el.also(elements::add)

fun Node.add(name : String, text : String) = add(Node(name, text))

fun Node.add(name: String, vararg attributes : Pair<String, String>, builder : (()->Unit)? = null) =
    add(Node(name, attributes, builder))
