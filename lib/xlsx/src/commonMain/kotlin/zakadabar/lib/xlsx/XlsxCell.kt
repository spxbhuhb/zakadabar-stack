/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

data class XlsxCell(val coordinate: XlsxCoordinate) : Comparable<XlsxCell> {

    private var raw : String? = null // internal string representation

    override fun compareTo(other: XlsxCell): Int = coordinate.compareTo(other.coordinate)

    override fun toString() : String = raw ?: ""

    fun set(value: String?) {
        raw = value
    }

    fun set(value: Long?) {
        raw = value?.toString()
    }

    fun set(value: Double?) {
        raw = value?.toString()
    }

    fun isBlank() : Boolean = raw.isNullOrBlank()

    fun asString() : String? = when {
        isBlank() -> null
        else -> raw
    }

    fun asLong() : Long? = when {
        isBlank() -> null
        else -> raw!!.toLong()
    }

    fun asDouble() : Double? = when {
        isBlank() -> null
        else -> raw!!.toDouble()
    }



}