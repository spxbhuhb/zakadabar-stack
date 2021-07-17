/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

import java.io.*
import java.sql.Clob
import java.sql.NClob
import java.sql.SQLException

/**
 * The mapping in the Java programming language for the SQL `CLOB` type.
 * An SQL `CLOB` is a built-in type that stores a Character Large Object as a column value
 * in a row of a database table. By default drivers implement a `Clob` object using an SQL
 * `locator(CLOB)`, which means that a `Clob` object contains a logical pointer to the
 * SQL `CLOB` data rather than the data itself. A `Clob` object is valid for the duration
 * of the transaction in which it was created.
 *
 * The `Clob` interface provides methods for getting the length of an SQL `CLOB`
 * (Character Large Object) value, for materializing a `CLOB` value on the client, and for
 * searching for a substring or `CLOB` object within a `CLOB` value.
 * Methods in the interfaces ResultSet, CallableStatement, and PreparedStatement, such as
 * `getClob` and `setClob` allow a programmer to access an SQL `CLOB` value.
 * In addition, this interface has methods for updating a `CLOB` value.
 *
 * Based on ClobImpl from DataNucleus project. Thanks to DataNucleus contributors.
 * @see [ClobImpl from DataNucleus Project](https://github.com/datanucleus/datanucleus-rdbms/blob/master/src/main/java/org/datanucleus/store/rdbms/mapping/datastore/ClobImpl.java)
 *
 * @param string The string.
 */
class SQLDroidClob(string: String?) : Clob, NClob {
    private var string: String?
    private val length: Long

    /** Reader for the operations that work that way.  */
    private var reader: StringReader? = null

    /** InputStream for operations that work that way. TODO Rationalise with reader above.  */
    private var inputStream: InputStream? = null

    /** Whether we have already freed resources.  */
    private var freed = false
    @Throws(SQLException::class)
    override fun length(): Long {
        if (freed) {
            throw SQLException("free() has been called")
        }
        return length
    }

    @Throws(SQLException::class)
    override fun truncate(len: Long) {
        if (freed) {
            throw SQLException("free() has been called")
        }
        throw UnsupportedOperationException()
    }

    @Throws(SQLException::class)
    override fun getAsciiStream(): InputStream {
        if (freed) {
            throw SQLException("free() has been called")
        }
        if (inputStream == null) {
            inputStream = ByteArrayInputStream(string !!.toByteArray())
        }
        return inputStream !!
    }

    @Throws(SQLException::class)
    override fun setAsciiStream(pos: Long): OutputStream {
        if (freed) {
            throw SQLException("free() has been called")
        }
        throw UnsupportedOperationException()
    }

    @Throws(SQLException::class)
    override fun getCharacterStream(): Reader {
        if (freed) {
            throw SQLException("free() has been called")
        }
        if (reader == null) {
            reader = StringReader(string!!) // freed is set to true when string is set to null
        }
        return reader !!
    }

    @Throws(SQLException::class)
    override fun setCharacterStream(pos: Long): Writer {
        if (freed) {
            throw SQLException("free() has been called")
        }
        throw UnsupportedOperationException()
    }

    /**
     * Free the Blob object and releases the resources that it holds.
     * The object is invalid once the free method is called.
     * @throws SQLException if an error occurs
     */
    @Throws(SQLException::class)
    override fun free() {
        if (freed) {
            return
        }
        string = null
        if (reader != null) {
            reader !!.close()
        }
        if (inputStream != null) {
            try {
                inputStream !!.close()
            } catch (ioe: IOException) {
                // Do nothing
            }
        }
        freed = true
    }

    /**
     * Returns a Reader object that contains a partial Clob value, starting with the character specified by pos,
     * which is length characters in length.
     * @param pos the offset to the first byte of the partial value to be retrieved.
     * The first byte in the Clob is at position 1
     * @param length the length in bytes of the partial value to be retrieved
     */
    @Throws(SQLException::class)
    override fun getCharacterStream(pos: Long, length: Long): Reader {
        if (freed) {
            throw SQLException("free() has been called")
        }
        // TODO Use pos, length
        if (reader == null) {
            reader = StringReader(string!!) // freed is set to true when string is set to null
        }
        return reader !!
    }

    @Throws(SQLException::class)
    override fun getSubString(pos: Long, length: Int): String {
        if (freed) {
            throw SQLException("free() has been called")
        }
        require(pos <= Int.MAX_VALUE) { "Initial position cannot be larger than " + Int.MAX_VALUE }
        if (pos + length - 1 > length()) {
            throw IndexOutOfBoundsException("The requested substring is greater than the actual length of the Clob String.")
        }
        return string !!.substring(pos.toInt() - 1, pos.toInt() + length - 1)
    }

    @Throws(SQLException::class)
    override fun setString(pos: Long, str: String): Int {
        if (freed) {
            throw SQLException("free() has been called")
        }
        throw UnsupportedOperationException()
    }

    @Throws(SQLException::class)
    override fun setString(pos: Long, str: String, offset: Int, len: Int): Int {
        if (freed) {
            throw SQLException("free() has been called")
        }
        throw UnsupportedOperationException()
    }

    @Throws(SQLException::class)
    override fun position(searchstr: String, start: Long): Long {
        if (freed) {
            throw SQLException("free() has been called")
        }
        throw UnsupportedOperationException()
    }

    @Throws(SQLException::class)
    override fun position(searchstr: Clob, start: Long): Long {
        if (freed) {
            throw SQLException("free() has been called")
        }
        throw UnsupportedOperationException()
    }

    init {
        requireNotNull(string) { "String cannot be null" }
        this.string = string
        length = string.length.toLong()
    }
}