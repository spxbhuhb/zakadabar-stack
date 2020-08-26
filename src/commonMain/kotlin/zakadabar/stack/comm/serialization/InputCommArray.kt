/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package zakadabar.stack.comm.serialization

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