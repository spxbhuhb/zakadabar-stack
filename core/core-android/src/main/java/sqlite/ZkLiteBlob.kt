/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import java.sql.Blob
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException

class ZkLiteBlob(
    private var b: ByteArray
) : Blob {
    @Throws(SQLException::class)
    override fun getBinaryStream(): InputStream {
        return getBinaryStream(0, b.size.toLong())
    }

    @Throws(SQLException::class)
    override fun getBinaryStream(pos: Long, length: Long): InputStream {
        return ByteArrayInputStream(b, pos.toInt(), length.toInt())
    }

    @Throws(SQLException::class)
    override fun getBytes(pos: Long, length: Int): ByteArray {
        if (pos < 0) {
            throw SQLException("pos must be > 0")
        }
        if (length < 0) {
            throw SQLException("length must be > 0")
        }
        if (pos > 0 || length < b.size) {
            val tmp = ByteArray(length)
            System.arraycopy(b, pos.toInt(), tmp, 0, length)
            return tmp
        }
        return b
    }

    @Throws(SQLException::class)
    override fun length(): Long {
        return b.size.toLong()
    }

    @Throws(SQLException::class)
    override fun position(pattern: Blob, start: Long): Long {
        return position(pattern.getBytes(0, pattern.length().toInt()), start)
    }

    @Throws(SQLException::class)
    override fun position(pattern: ByteArray, start: Long): Long {
        throw SQLFeatureNotSupportedException("position not supported")
    }

    @Throws(SQLException::class)
    override fun setBinaryStream(pos: Long): OutputStream {
        throw SQLFeatureNotSupportedException("setBinaryStream not supported")
    }

    @Throws(SQLException::class)
    override fun setBytes(pos: Long, theBytes: ByteArray): Int {
        return setBytes(pos, theBytes, 0, theBytes.size)
    }

    @Throws(SQLException::class)
    override fun setBytes(pos: Long, theBytes: ByteArray, offset: Int, len: Int): Int {
        throw SQLFeatureNotSupportedException("setBytes not supported")
    }

    @Throws(SQLException::class)
    override fun truncate(len: Long) {
        throw SQLFeatureNotSupportedException("truncate not supported")
    }

    @Throws(SQLException::class)
    override fun free() {
        throw SQLFeatureNotSupportedException("free not supported")
    }

    /** Print the length of the blob along with the first 10 characters.
     * @see Object.toString
     */
    override fun toString(): String {
        val sb = StringBuffer()
        sb.append("Blob length ")
        var length: Long = 0
        try {
            length = length()
        } catch (e: SQLException) {
            // never thrown
        }
        sb.append(length)
        sb.append(" ")
        if (length > 10) {
            length = 10
        }
        for (counter in 0 until length) {
            sb.append("0x")
            sb.append(Integer.toHexString(b[counter.toInt()].toInt()))
            sb.append(" ")
        }
        sb.append("(")
        for (counter in 0 until length) {
            sb.append(b[counter.toInt()].toInt().toChar().toString())
            if (counter == length - 1) {
                sb.append(")")
            } else {
                sb.append(" ")
            }
        }
        return sb.toString()
    }
}