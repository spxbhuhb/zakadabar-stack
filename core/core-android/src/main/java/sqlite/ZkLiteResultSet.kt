/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

import android.database.Cursor
import android.database.SQLException
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.Reader
import java.io.StringReader
import java.math.BigDecimal
import java.math.MathContext
import java.net.URL
import java.nio.charset.StandardCharsets
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ZkLiteResultSet(
    private val c: Cursor
) : ResultSet {

    private var lastColumnRead = 0 // JDBC style column index starting from 1

    // TODO: Implement behavior (as Xerial driver)
    private var limitRows = 0

    @Throws(java.sql.SQLException::class)
    private fun dumpResultSet() {
        val rs: ResultSet = this
        var headerDrawn = false
        while (rs.next()) {
            if (! headerDrawn) {
                for (i in 1..rs.metaData.columnCount) {
                    print(" | ")
                    print(rs.metaData.getColumnLabel(i))
                }
                println(" | ")
                headerDrawn = true
            }
            for (i in 1..rs.metaData.columnCount) {
                print(" | ")
                print(rs.getString(i))
                if (rs.getString(i) != null) {
                    print(" (" + rs.getString(i).length + ")")
                }
            }
            println(" | ")
        }
        rs.beforeFirst()
    }

    /**
     * convert JDBC column index (one-based) to sqlite column index (zero-based)
     * @param colID
     */
    private fun ci(colID: Int): Int {
        return colID - 1
    }

    @Throws(java.sql.SQLException::class)
    override fun absolute(row: Int): Boolean {
        // Not supported by SQLite
        throw SQLFeatureNotSupportedException("ResultSet is TYPE_FORWARD_ONLY")
    }

    @Throws(java.sql.SQLException::class)
    override fun afterLast() {
        try {
            c.moveToLast()
            c.moveToNext()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun beforeFirst() {
        try {
            c.moveToFirst()
            c.moveToPrevious()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun cancelRowUpdates() {
        // Not supported by SQLite
        throw SQLFeatureNotSupportedException("ResultSet.cancelRowUpdates not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun clearWarnings() {
        // TODO: Evaluate if implementation is sufficient (if so, delete comment and log)
        Log.e(" ********************* not implemented @ " + fileName + " line " + lineNumber)
    }

    @Throws(java.sql.SQLException::class)
    override fun close() {
        try {
            c.close()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun deleteRow() {
        // Not supported by SQLite
        throw SQLFeatureNotSupportedException("ResultSet.deleteRow not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun findColumn(columnName: String): Int {
        return try {
            // JDBC style column index starts from 1; Android database cursor has zero-based index
            c.getColumnIndexOrThrow(columnName) + 1
        } catch (e : IllegalArgumentException) {
            throw java.sql.SQLException("unknown column $columnName")
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun first(): Boolean {
        return try {
            c.moveToFirst()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getArray(colID: Int): Array {
        // Not supported by SQLite
        throw SQLFeatureNotSupportedException("getArray is not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun getArray(columnName: String): Array {
        return getArray(findColumn(columnName))
    }

    @Throws(java.sql.SQLException::class)
    override fun getAsciiStream(colID: Int): InputStream? {
        return getString(colID)?.let { ByteArrayInputStream(it.toByteArray(StandardCharsets.UTF_8)) }
    }

    @Throws(java.sql.SQLException::class)
    override fun getAsciiStream(columnName: String): InputStream? {
        return getAsciiStream(findColumn(columnName))
    }

    @Throws(java.sql.SQLException::class)
    override fun getBigDecimal(colID: Int): BigDecimal? {
        return getString(colID)?.let { BigDecimal(it) }
    }

    @Throws(java.sql.SQLException::class)
    override fun getBigDecimal(columnName: String): BigDecimal? {
        return getBigDecimal(findColumn(columnName))
    }

    @Throws(java.sql.SQLException::class)
    override fun getBigDecimal(colID: Int, scale: Int): BigDecimal? {
        return getString(colID)?.let { BigDecimal(it, MathContext(scale)) }
    }

    @Throws(java.sql.SQLException::class)
    override fun getBigDecimal(columnName: String, scale: Int): BigDecimal? {
        return getBigDecimal(findColumn(columnName), scale)
    }

    @Throws(java.sql.SQLException::class)
    override fun getBinaryStream(colID: Int): InputStream? {
        return getBytes(colID)?.let { ByteArrayInputStream(it) }
    }

    @Throws(java.sql.SQLException::class)
    override fun getBinaryStream(columnName: String): InputStream? {
        return getBinaryStream(findColumn(columnName))
    }

    @Throws(java.sql.SQLException::class)
    override fun getBlob(index: Int): Blob? {
        return try {
            getBytes(index)?.let { ZkLiteBlob(it) }
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getBlob(columnName: String): Blob? {
        val index = findColumn(columnName)
        return getBlob(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getBoolean(index: Int): Boolean {
        return try {
            lastColumnRead = index
            c.getInt(ci(index)) != 0
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getBoolean(columnName: String): Boolean {
        val index = findColumn(columnName)
        return getBoolean(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getByte(index: Int): Byte {
        return try {
            lastColumnRead = index
            c.getShort(ci(index)).toByte()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getByte(columnName: String): Byte {
        val index = findColumn(columnName)
        return getByte(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getBytes(index: Int): ByteArray? {
        return try {
            lastColumnRead = index
            var bytes: ByteArray? = c.getBlob(ci(index))
            // SQLite includes the zero-byte at the end for Strings.
            if (bytes != null && ZkLiteResultSetMetaData.getType(c, ci(index)) == 3) { //  Cursor.FIELD_TYPE_STRING
                bytes = Arrays.copyOf(bytes, bytes.size - 1)
            }
            bytes
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getBytes(columnName: String): ByteArray? {
        val index = findColumn(columnName)
        return getBytes(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getCharacterStream(colID: Int): Reader? {
        return getString(colID)?.let { StringReader(it) }
    }

    @Throws(java.sql.SQLException::class)
    override fun getCharacterStream(columnName: String): Reader? {
        return getCharacterStream(findColumn(columnName))
    }

    @Throws(java.sql.SQLException::class)
    override fun getClob(colID: Int): ZkLiteClob? {
        return getString(colID)?.let { ZkLiteClob(it) }
    }

    @Throws(java.sql.SQLException::class)
    override fun getClob(colName: String): Clob? {
        val index = findColumn(colName)
        return getClob(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getConcurrency(): Int {
        return ResultSet.CONCUR_READ_ONLY
    }

    @Throws(java.sql.SQLException::class)
    override fun getCursorName(): String {
        throw SQLFeatureNotSupportedException("getCursorName not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun getDate(index: Int): Date? {
        return try {
            lastColumnRead = index
            val md = metaData
            var date: Date? = null
            when (md.getColumnType(index)) {
                Types.NULL -> return null
                Types.INTEGER, Types.BIGINT -> date = Date(getLong(index))
                Types.DATE -> date = getDate(index)?.let { Date(it.time) }
                else ->           // format 2011-07-11 11:36:30.009
                    try {
                        val dateFormat = SimpleDateFormat(DATE_PATTERN)
                        val parsedDate = dateFormat.parse(getString(index))
                        date = Date(parsedDate.time)
                    } catch (any: Exception) {
                        any.printStackTrace()
                    }
            }
            date !!
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getDate(columnName: String): Date? {
        val index = findColumn(columnName)
        return getDate(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getDate(colID: Int, cal: Calendar): Date {
        // TODO: Implement, perhaps as Xerial driver:
        //  https://github.com/xerial/sqlite-jdbc/blob/master/src/main/java/org/sqlite/jdbc3/JDBC3ResultSet.java#L313
        throw UnsupportedOperationException("getDate(int, Calendar) not implemented yet")
    }

    @Throws(java.sql.SQLException::class)
    override fun getDate(columnName: String, cal: Calendar): Date {
        return getDate(findColumn(columnName), cal)
    }

    @Throws(java.sql.SQLException::class)
    override fun getDouble(index: Int): Double {
        return try {
            lastColumnRead = index
            c.getDouble(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getDouble(columnName: String): Double {
        val index = findColumn(columnName)
        return getDouble(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getFetchDirection(): Int {
        return ResultSet.FETCH_FORWARD
    }

    @Throws(java.sql.SQLException::class)
    override fun getFetchSize(): Int {
        return limitRows
    }

    @Throws(java.sql.SQLException::class)
    override fun getFloat(index: Int): Float {
        return try {
            lastColumnRead = index
            c.getFloat(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getFloat(columnName: String): Float {
        val index = findColumn(columnName)
        return getFloat(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getInt(index: Int): Int {
        return try {
            lastColumnRead = index
            c.getInt(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getInt(columnName: String): Int {
        val index = findColumn(columnName)
        return getInt(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getLong(index: Int): Long {
        return try {
            lastColumnRead = index
            c.getLong(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getLong(columnName: String): Long {
        val index = findColumn(columnName)
        return getLong(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getMetaData(): ResultSetMetaData {
        return ZkLiteResultSetMetaData(c)
    }

    @Throws(java.sql.SQLException::class)
    override fun getObject(colID: Int): Any? {
        lastColumnRead = colID
        val newIndex = ci(colID)
        return when (ZkLiteResultSetMetaData.getType(c, newIndex)) {
            4 ->                 //CONVERT TO BYTE[] OBJECT
                ZkLiteBlob(c.getBlob(newIndex))
            2 -> c.getFloat(newIndex)
            1 -> c.getInt(newIndex)
            3 -> c.getString(newIndex)
            0 -> null
            else -> c.getString(newIndex)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getObject(columnName: String): Any? {
        val index = findColumn(columnName)
        return getObject(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getObject(columnIndex: Int, clazz: Map<String?, Class<*>?>?): Any {
        throw SQLFeatureNotSupportedException("Conversion not supported.  No conversions are supported.  This method will always throw.")
    }

    @Throws(java.sql.SQLException::class)
    override fun getObject(columnName: String, clazz: Map<String?, Class<*>?>?): Any {
        return getObject(findColumn(columnName), clazz)
    }

//    @Throws(java.sql.SQLException::class)
//    override fun <T> getObject(columnIndex: Int, clazz: Class<T>): T {
//        // This method is entitled to throw if the conversion is not supported, so,
//        // since we don't support any conversions we'll throw.
//        // The only problem with this is that we're required to support certain conversion as specified in the docs.
//        throw SQLFeatureNotSupportedException("Conversion not supported.  No conversions are supported.  This method will always throw.")
//    }
//
//    @Throws(java.sql.SQLException::class)
//    override fun <T> getObject(columnName: String, clazz: Class<T>): T {
//        return getObject(findColumn(columnName), clazz)
//    }

    @Throws(java.sql.SQLException::class)
    override fun getRef(colID: Int): Ref {
        throw SQLFeatureNotSupportedException("getRef not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun getRef(columnName: String): Ref {
        return getRef(findColumn(columnName))
    }

    @Throws(java.sql.SQLException::class)
    override fun getRow(): Int {
        return try {
            // convert to jdbc standard (counting from one)
            c.position + 1
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getShort(index: Int): Short {
        return try {
            lastColumnRead = index
            c.getShort(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getShort(columnName: String): Short {
        val index = findColumn(columnName)
        return getShort(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getStatement(): Statement {
        // TODO: Implement as Xerial driver (which takes Statement as constructor argument)
        throw UnsupportedOperationException("Not implemented yet")
    }

    @Throws(java.sql.SQLException::class)
    override fun getString(index: Int): String? {
        return try {
            lastColumnRead = index
            c.getString(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getString(columnName: String): String? {
        val index = findColumn(columnName)
        return getString(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getTime(colID: Int): Time {
        // TODO: Implement as Xerial driver
        // https://github.com/xerial/sqlite-jdbc/blob/master/src/main/java/org/sqlite/jdbc3/JDBC3ResultSet.java#L449
        throw UnsupportedOperationException("Not implemented yet")
    }

    @Throws(java.sql.SQLException::class)
    override fun getTime(columnName: String): Time {
        return getTime(findColumn(columnName))
    }

    @Throws(java.sql.SQLException::class)
    override fun getTime(colID: Int, cal: Calendar): Time {
        // TODO: Implement as Xerial driver
        // https://github.com/xerial/sqlite-jdbc/blob/master/src/main/java/org/sqlite/jdbc3/JDBC3ResultSet.java#L449
        throw UnsupportedOperationException("Not implemented yet")
    }

    @Throws(java.sql.SQLException::class)
    override fun getTime(columnName: String, cal: Calendar): Time {
        return getTime(findColumn(columnName), cal)
    }

    @Throws(java.sql.SQLException::class)
    override fun getTimestamp(index: Int): Timestamp? {
        return try {
            lastColumnRead = index
            when (metaData.getColumnType(index)) {
                Types.NULL -> null
                Types.INTEGER, Types.BIGINT -> Timestamp(getLong(index))
                Types.DATE -> getDate(index)?.let { Timestamp(it.time) }
                else -> {
                    // format 2011-07-11 11:36:30.009 OR 2011-07-11 11:36:30 
                    val dateFormat = SimpleDateFormat(TIMESTAMP_PATTERN)
                    val dateFormatNoMillis = SimpleDateFormat(TIMESTAMP_PATTERN_NO_MILLIS)
                    try {
                        Timestamp(dateFormat.parse(getString(index)).time)
                    } catch (e: ParseException) {
                        try {
                            Timestamp(dateFormatNoMillis.parse(getString(index)).time)
                        } catch (e1: ParseException) {
                            throw java.sql.SQLException(e.toString())
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun getTimestamp(columnName: String): Timestamp? {
        val index = findColumn(columnName)
        return getTimestamp(index)
    }

    @Throws(java.sql.SQLException::class)
    override fun getTimestamp(colID: Int, cal: Calendar): Timestamp? {
        // TODO Implement with Calendar
        Log.e(" ********************* not implemented correctly - Calendar is ignored. @ " + fileName + " line " + lineNumber)
        return getTimestamp(colID)
    }

    @Throws(java.sql.SQLException::class)
    override fun getTimestamp(columnName: String, cal: Calendar): Timestamp? {
        return getTimestamp(findColumn(columnName), cal)
    }

    @Throws(java.sql.SQLException::class)
    override fun getType(): Int {
        return ResultSet.TYPE_SCROLL_SENSITIVE
    }

    @Throws(java.sql.SQLException::class)
    override fun getURL(colID: Int): URL {
        throw SQLFeatureNotSupportedException("ResultSet.getURL not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun getURL(columnName: String): URL {
        return getURL(findColumn(columnName))
    }

    @Deprecated("since JDBC 2.0, use getCharacterStream")
    @Throws(java.sql.SQLException::class)
    override fun getUnicodeStream(colID: Int): InputStream {
        throw SQLFeatureNotSupportedException("ResultSet.getUnicodeStream deprecated, use getCharacterStream instead")
    }

    @Deprecated("since JDBC 2.0, use getCharacterStream")
    @Throws(java.sql.SQLException::class)
    override fun getUnicodeStream(columnName: String): InputStream {
        return getUnicodeStream(findColumn(columnName))
    }

    @Throws(java.sql.SQLException::class)
    override fun getWarnings(): SQLWarning {
        // TODO: It may be that this is better implemented as "return null"
        throw UnsupportedOperationException("ResultSet.getWarnings not implemented yet")
    }

    @Throws(java.sql.SQLException::class)
    override fun insertRow() {
        throw SQLFeatureNotSupportedException("ResultSet.insertRow not implemented yet")
    }

    @Throws(java.sql.SQLException::class)
    override fun isAfterLast(): Boolean {
        return if (isClosed) {
            false
        } else try {
            c.isAfterLast
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun isBeforeFirst(): Boolean {
        return if (isClosed) {
            false
        } else try {
            c.isBeforeFirst
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun isFirst(): Boolean {
        return if (isClosed) {
            false
        } else try {
            c.isFirst
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun isLast(): Boolean {
        return if (isClosed) {
            false
        } else try {
            c.isLast
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun last(): Boolean {
        return try {
            c.moveToLast()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun moveToCurrentRow() {
        throw SQLFeatureNotSupportedException("moveToCurrentRow not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun moveToInsertRow() {
        throw SQLFeatureNotSupportedException("moveToCurrentRow not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun next(): Boolean {
        return try {
            c.moveToNext()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun previous(): Boolean {
        return try {
            c.moveToPrevious()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun refreshRow() {
        try {
            c.requery()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun relative(rows: Int): Boolean {
        throw SQLFeatureNotSupportedException("relative not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun rowDeleted(): Boolean {
        throw SQLFeatureNotSupportedException("rowDeleted not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun rowInserted(): Boolean {
        throw SQLFeatureNotSupportedException("rowInserted not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun rowUpdated(): Boolean {
        throw SQLFeatureNotSupportedException("rowUpdated not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun setFetchDirection(direction: Int) {
        if (direction != ResultSet.FETCH_FORWARD) {
            throw java.sql.SQLException("only FETCH_FORWARD direction supported")
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun setFetchSize(rows: Int) {
        // TODO: Implement as max row number for next()
        if (rows != 0) {
            throw UnsupportedOperationException("Not implemented yet")
        }
        limitRows = rows
    }

    @Throws(java.sql.SQLException::class)
    override fun updateArray(colID: Int, x: Array) {
        throw SQLFeatureNotSupportedException("updateArray not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateArray(columnName: String, x: Array) {
        throw SQLFeatureNotSupportedException("updateArray not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateAsciiStream(colID: Int, x: InputStream, length: Int) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateAsciiStream(columnName: String, x: InputStream, length: Int) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBigDecimal(colID: Int, x: BigDecimal) {
        throw SQLFeatureNotSupportedException("updateBigDecimal not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBigDecimal(columnName: String, x: BigDecimal) {
        throw SQLFeatureNotSupportedException("updateBigDecimal not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBinaryStream(colID: Int, x: InputStream, length: Int) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBinaryStream(columnName: String, x: InputStream, length: Int) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBlob(colID: Int, x: Blob) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBlob(columnName: String, x: Blob) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBoolean(colID: Int, x: Boolean) {
        throw SQLFeatureNotSupportedException("updateBoolean not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBoolean(columnName: String, x: Boolean) {
        throw SQLFeatureNotSupportedException("updateBoolean not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateByte(colID: Int, x: Byte) {
        throw SQLFeatureNotSupportedException("updateByte not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateByte(columnName: String, x: Byte) {
        throw SQLFeatureNotSupportedException("updateByte not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBytes(colID: Int, x: ByteArray) {
        throw SQLFeatureNotSupportedException("updateBytes not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBytes(columnName: String, x: ByteArray) {
        throw SQLFeatureNotSupportedException("updateBytes not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateCharacterStream(colID: Int, x: Reader, length: Int) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateCharacterStream(columnName: String, reader: Reader, length: Int) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateClob(colID: Int, x: Clob) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateClob(columnName: String, x: Clob) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateDate(colID: Int, x: Date) {
        throw SQLFeatureNotSupportedException("updateDate not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateDate(columnName: String, x: Date) {
        throw SQLFeatureNotSupportedException("updateDate not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateDouble(colID: Int, x: Double) {
        throw SQLFeatureNotSupportedException("updateDouble not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateDouble(columnName: String, x: Double) {
        throw SQLFeatureNotSupportedException("updateDouble not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateFloat(colID: Int, x: Float) {
        throw SQLFeatureNotSupportedException("updateFloat not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateFloat(columnName: String, x: Float) {
        throw SQLFeatureNotSupportedException("updateFloat not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateInt(colID: Int, x: Int) {
        throw SQLFeatureNotSupportedException("updateInt not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateInt(columnName: String, x: Int) {
        throw SQLFeatureNotSupportedException("updateInt not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateLong(colID: Int, x: Long) {
        throw SQLFeatureNotSupportedException("updateLong not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateLong(columnName: String, x: Long) {
        throw SQLFeatureNotSupportedException("updateLong not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNull(colID: Int) {
        throw SQLFeatureNotSupportedException("updateNull not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNull(columnName: String) {
        throw SQLFeatureNotSupportedException("updateNull not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateObject(colID: Int, x: Any) {
        throw SQLFeatureNotSupportedException("updateObject not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateObject(columnName: String, x: Any) {
        throw SQLFeatureNotSupportedException("updateObject not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateObject(colID: Int, x: Any, scale: Int) {
        throw SQLFeatureNotSupportedException("updateObject not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateObject(columnName: String, x: Any, scale: Int) {
        throw SQLFeatureNotSupportedException("updateObject not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateRef(colID: Int, x: Ref) {
        throw SQLFeatureNotSupportedException("updateRef not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateRef(columnName: String, x: Ref) {
        throw SQLFeatureNotSupportedException("updateRef not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateRow() {
        throw SQLFeatureNotSupportedException("updateRow not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateShort(colID: Int, x: Short) {
        throw SQLFeatureNotSupportedException("updateShort not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateShort(columnName: String, x: Short) {
        throw SQLFeatureNotSupportedException("updateShort not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateString(colID: Int, x: String) {
        throw SQLFeatureNotSupportedException("updateString not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateString(columnName: String, x: String) {
        throw SQLFeatureNotSupportedException("updateString not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateTime(colID: Int, x: Time) {
        throw SQLFeatureNotSupportedException("updateTime not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateTime(columnName: String, x: Time) {
        throw SQLFeatureNotSupportedException("updateTime not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateTimestamp(colID: Int, x: Timestamp) {
        throw SQLFeatureNotSupportedException("updateTimestamp not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateTimestamp(columnName: String, x: Timestamp) {
        throw SQLFeatureNotSupportedException("updateTimestamp not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun wasNull(): Boolean {
        return try {
            c.isNull(ci(lastColumnRead))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    @Throws(java.sql.SQLException::class)
    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return iface != null && iface.isAssignableFrom(javaClass)
    }

    @Throws(java.sql.SQLException::class)
    override fun <T> unwrap(iface: Class<T>): T {
        if (isWrapperFor(iface)) {
            @Suppress("UNCHECKED_CAST") // isWrapper checks for it
            return this as T
        }
        throw java.sql.SQLException("$javaClass does not wrap $iface")
    }

    @Throws(java.sql.SQLException::class)
    override fun getHoldability(): Int {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT
    }

    @Throws(java.sql.SQLException::class)
    override fun getNCharacterStream(columnIndex: Int): Reader? {
        return getCharacterStream(columnIndex)
    }

    @Throws(java.sql.SQLException::class)
    override fun getNCharacterStream(columnLabel: String): Reader? {
        return getNCharacterStream(findColumn(columnLabel))
    }

    @Throws(java.sql.SQLException::class)
    override fun getNClob(columnIndex: Int): NClob? {
        return getClob(columnIndex)
    }

    @Throws(java.sql.SQLException::class)
    override fun getNClob(columnLabel: String): NClob? {
        return getNClob(findColumn(columnLabel))
    }

    @Throws(java.sql.SQLException::class)
    override fun getNString(columnIndex: Int): String? {
        return getString(columnIndex)
    }

    @Throws(java.sql.SQLException::class)
    override fun getNString(columnLabel: String): String? {
        return getNString(findColumn(columnLabel))
    }

    @Throws(java.sql.SQLException::class)
    override fun getRowId(columnIndex: Int): RowId {
        throw SQLFeatureNotSupportedException("getRowId not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun getRowId(columnLabel: String): RowId {
        return getRowId(findColumn(columnLabel))
    }

    @Throws(java.sql.SQLException::class)
    override fun getSQLXML(columnIndex: Int): SQLXML {
        throw SQLFeatureNotSupportedException("getSQLXML not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun getSQLXML(columnLabel: String): SQLXML {
        return getSQLXML(findColumn(columnLabel))
    }

    @Throws(java.sql.SQLException::class)
    override fun isClosed(): Boolean {
        return c.isClosed
    }

    @Throws(java.sql.SQLException::class)
    override fun updateAsciiStream(columnIndex: Int, x: InputStream) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateAsciiStream(columnLabel: String, x: InputStream) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateAsciiStream(columnIndex: Int, x: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateAsciiStream(columnLabel: String, x: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBinaryStream(columnIndex: Int, x: InputStream) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBinaryStream(columnLabel: String, x: InputStream) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBinaryStream(columnIndex: Int, x: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBinaryStream(columnLabel: String, x: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBlob(columnIndex: Int, inputStream: InputStream) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBlob(columnLabel: String, inputStream: InputStream) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBlob(columnIndex: Int, inputStream: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateBlob(columnLabel: String, inputStream: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateCharacterStream(columnIndex: Int, x: Reader) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateCharacterStream(columnLabel: String, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateCharacterStream(columnIndex: Int, x: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateCharacterStream(columnLabel: String, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateClob(columnIndex: Int, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateClob(columnLabel: String, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateClob(columnIndex: Int, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateClob(columnLabel: String, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNCharacterStream(columnIndex: Int, x: Reader) {
        throw SQLFeatureNotSupportedException("updateNCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNCharacterStream(columnLabel: String, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateNCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNCharacterStream(columnIndex: Int, x: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateNCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNCharacterStream(columnLabel: String, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateNCharacterStream not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNClob(columnIndex: Int, nClob: NClob) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNClob(columnLabel: String, nClob: NClob) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNClob(columnIndex: Int, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNClob(columnLabel: String, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNClob(columnIndex: Int, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNClob(columnLabel: String, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNString(columnIndex: Int, nString: String) {
        throw SQLFeatureNotSupportedException("updateNString not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateNString(columnLabel: String, nString: String) {
        throw SQLFeatureNotSupportedException("updateNString not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateRowId(columnIndex: Int, value: RowId) {
        throw SQLFeatureNotSupportedException("updateRowId not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateRowId(columnLabel: String, value: RowId) {
        throw SQLFeatureNotSupportedException("updateRowId not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateSQLXML(columnIndex: Int, xmlObject: SQLXML) {
        throw SQLFeatureNotSupportedException("updateSQLXML not supported")
    }

    @Throws(java.sql.SQLException::class)
    override fun updateSQLXML(columnLabel: String, xmlObject: SQLXML) {
        throw SQLFeatureNotSupportedException("updateSQLXML not supported")
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private const val TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"
        private const val TIMESTAMP_PATTERN_NO_MILLIS = "yyyy-MM-dd HH:mm:ss"
        var dump = false
    }

    init {
        if (dump) {
            dumpResultSet()
        }
    }
}