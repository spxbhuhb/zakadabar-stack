/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_TYPEALIAS_PARAMETER", "unused") // entity id should be type bound

package zakadabar.stack.data.entity

import kotlinx.serialization.Serializable

@Serializable
class EntityId<T> {

    val value : String

    constructor() {
        value = ""
    }

    constructor(value : Long) {
        this.value = value.toString()
    }

    constructor(value : String) {
        this.value = value
    }

    fun toLong() = value.toLong()

    fun isEmpty() = value.isEmpty()

    override fun equals(other: Any?) = if (other is EntityId<*>) other.value == this.value else false

    override fun hashCode() = value.hashCode()

}