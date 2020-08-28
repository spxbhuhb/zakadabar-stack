/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.util


/**
 * A general catalog of unique elements.
 * The dual-store (map and list) is meant to avoid converting the map into a list again and again.
 */
open class CatalogOfUniques<U : Unique> {

    protected val map = mutableMapOf<UUID, U>()
    protected val list = mutableListOf<U>()

    operator fun plusAssign(item: U) {
        val registered = map[item.uuid]

        if (registered != null) {
            if (registered === item) return
            throw IllegalArgumentException("item ${item.uuid} already registered")
        }

        map[item.uuid] = item
        list += item
    }

    open operator fun get(uuid: UUID) = map[uuid]

    fun forEach(func: (U) -> Unit) = list.forEach(func)

}