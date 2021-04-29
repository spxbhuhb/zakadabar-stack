/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_TYPEALIAS_PARAMETER") // record id should be record type bound

package zakadabar.stack.data.record

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class RecordId<T> : Comparable<RecordId<T>> {
    abstract fun fromString(value: String): RecordId<T>
    abstract fun toLong(): Long
    abstract fun isEmpty(): Boolean
}

@Serializable
@SerialName("empty")
class EmptyRecordId<T> : RecordId<T>() {

    override fun fromString(value: String) = EmptyRecordId<T>()
    override fun toString() = ""

    override fun compareTo(other: RecordId<T>): Int {
        return 0
    }

    override fun toLong() = throw IllegalStateException("empty record id cannot be converted to long")

    override fun isEmpty() = true

    override fun equals(other: Any?) = if (other is RecordId<*>) other.isEmpty() else false

    override fun hashCode(): Int {
        return this::class.hashCode()
    }

}

@Serializable
@SerialName("long")
class LongRecordId<T>(
    val value: Long
) : RecordId<T>() {

    override fun fromString(value: String) = LongRecordId<T>(value.toLong())
    override fun toString() = value.toString()

    override fun compareTo(other: RecordId<T>): Int {
        if (other is LongRecordId) return this.value.compareTo(other.value)
        return 0
    }

    override fun toLong() = value

    override fun isEmpty() = (value == 0L)

    override fun equals(other: Any?) = when (other) {
        is LongRecordId<*> -> other.value == this.value
        is StringRecordId<*> -> other.value.toLong() == this.value
        is EmptyRecordId<*> -> this.isEmpty()
        else -> false
    }

    override fun hashCode() = value.hashCode()

}

@Serializable
@SerialName("string")
class StringRecordId<T>(
    val value: String
) : RecordId<T>() {

    override fun fromString(value: String) = StringRecordId<T>(value)
    override fun toString() = value

    override fun compareTo(other: RecordId<T>): Int {
        if (other is StringRecordId) return this.value.compareTo(other.value)
        return 0
    }

    override fun toLong() = value.toLong()

    override fun isEmpty() = value.isEmpty()

    override fun equals(other: Any?) = when (other) {
        is LongRecordId<*> -> other.value == this.value.toLong()
        is StringRecordId<*> -> other.value == this.value
        is EmptyRecordId<*> -> this.isEmpty()
        else -> false
    }

    override fun hashCode() = value.hashCode()

}