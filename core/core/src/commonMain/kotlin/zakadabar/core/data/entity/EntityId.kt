/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_TYPEALIAS_PARAMETER", "unused") // entity id should be type bound

package zakadabar.core.data.entity

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo

@Serializable
class EntityId<T> : Comparable<EntityId<T>> {

    val value : String

    constructor() {
        value = ""
    }

    constructor(value : EntityId<out BaseBo>) {
        this.value = value.value
    }

    constructor(value : Long) {
        this.value = value.toString()
    }

    constructor(value : String?) {
        this.value = value ?: ""
    }

    fun toLong() = value.toLong()

    fun isEmpty() = value.isEmpty()

    override fun equals(other: Any?) = if (other is EntityId<*>) other.value == this.value else false

    override fun hashCode() = value.hashCode()

    override fun toString() = value

    override fun compareTo(other: EntityId<T>) : Int {
        if (value.isEmpty() && other.value.isEmpty()) return 0

        val tl = value.toLongOrNull()
        val ol = value.toLongOrNull()

        if (tl != null && ol != null) return tl.compareTo(ol)

        return value.compareTo(other.value)
    }

}