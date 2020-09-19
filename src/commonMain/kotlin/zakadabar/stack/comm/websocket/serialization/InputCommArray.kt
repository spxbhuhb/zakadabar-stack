/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.comm.websocket.serialization

import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID

open class InputCommArray(source: ByteArray) {

    protected var array = source.asUByteArray()

    protected var position = 0

    @PublicApi
    open fun readByte() = array[position ++].toByte()

    open fun readInt(): Int {
        var value = array[position ++].toUInt() shl 24
        value += array[position ++].toUInt() shl 16
        value += array[position ++].toUInt() shl 8
        value += array[position ++].toUInt()
        return value.toInt()
    }

    open fun readLong(): Long {
        var value = array[position ++].toULong() shl 56
        value += array[position ++].toULong() shl 48
        value += array[position ++].toULong() shl 40
        value += array[position ++].toULong() shl 32
        value += array[position ++].toULong() shl 24
        value += array[position ++].toULong() shl 16
        value += array[position ++].toULong() shl 8
        value += array[position ++].toULong()
        return value.toLong()
    }

    open fun readOptLong(): Long? {
        if (array[position] == 0xf0.toUByte()) {
            position ++ // this cannot be put into the if or JavaScript fails... no idea why
            return null
        }
        position ++
        return readLong()
    }

    open fun readDouble() = Double.fromBits(readLong())

    open fun readFloat() = Float.fromBits(readInt())

    open fun readUUID() = UUID(readInt(), readInt(), readInt(), readInt())

    open fun readString(): String {
        val size = readInt()
        val value = array.asByteArray().decodeToString(position, position + size)
        position += size
        return value
    }

    @PublicApi
    open fun readOptString(): String? {
        if (array[position] == 0xf0.toUByte()) {
            position ++ // this cannot be put into the if or JavaScript fails... no idea why
            return null
        }
        position ++
        return readString()
    }

    open fun readByteArray(): ByteArray {
        val size = readInt()
        val value = ByteArray(size)
        array.asByteArray().copyInto(value, 0, position, position + size)
        position += size
        return value
    }
}