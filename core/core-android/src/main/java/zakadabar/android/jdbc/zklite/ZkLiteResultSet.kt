/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.zklite

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

    override fun absolute(row: Int): Boolean {
        // Not supported by SQLite
        throw SQLFeatureNotSupportedException("ResultSet is TYPE_FORWARD_ONLY")
    }

    override fun afterLast() {
        try {
            c.moveToLast()
            c.moveToNext()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun beforeFirst() {
        try {
            c.moveToFirst()
            c.moveToPrevious()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun cancelRowUpdates() {
        // Not supported by SQLite
        throw SQLFeatureNotSupportedException("ResultSet.cancelRowUpdates not supported")
    }

    override fun clearWarnings() {
        // TODO: Evaluate if implementation is sufficient (if so, delete comment and log)
        Log.e(" ********************* not implemented @ " + fileName + " line " + lineNumber)
    }

    override fun close() {
        try {
            c.close()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun deleteRow() {
        // Not supported by SQLite
        throw SQLFeatureNotSupportedException("ResultSet.deleteRow not supported")
    }

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

    override fun first(): Boolean {
        return try {
            c.moveToFirst()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getArray(colID: Int): Array {
        // Not supported by SQLite
        throw SQLFeatureNotSupportedException("getArray is not supported")
    }

    override fun getArray(columnName: String): Array {
        return getArray(findColumn(columnName))
    }

    override fun getAsciiStream(colID: Int): InputStream? {
        return getString(colID)?.let { ByteArrayInputStream(it.toByteArray(StandardCharsets.UTF_8)) }
    }

    override fun getAsciiStream(columnName: String): InputStream? {
        return getAsciiStream(findColumn(columnName))
    }

    override fun getBigDecimal(colID: Int): BigDecimal? {
        return getString(colID)?.let { BigDecimal(it) }
    }

    override fun getBigDecimal(columnName: String): BigDecimal? {
        return getBigDecimal(findColumn(columnName))
    }

    override fun getBigDecimal(colID: Int, scale: Int): BigDecimal? {
        return getString(colID)?.let { BigDecimal(it, MathContext(scale)) }
    }

    override fun getBigDecimal(columnName: String, scale: Int): BigDecimal? {
        return getBigDecimal(findColumn(columnName), scale)
    }

    override fun getBinaryStream(colID: Int): InputStream? {
        return getBytes(colID)?.let { ByteArrayInputStream(it) }
    }

    override fun getBinaryStream(columnName: String): InputStream? {
        return getBinaryStream(findColumn(columnName))
    }

    override fun getBlob(index: Int): Blob? {
        return try {
            getBytes(index)?.let { ZkLiteBlob(it) }
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getBlob(columnName: String): Blob? {
        val index = findColumn(columnName)
        return getBlob(index)
    }

    override fun getBoolean(index: Int): Boolean {
        return try {
            lastColumnRead = index
            c.getInt(ci(index)) != 0
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getBoolean(columnName: String): Boolean {
        val index = findColumn(columnName)
        return getBoolean(index)
    }

    override fun getByte(index: Int): Byte {
        return try {
            lastColumnRead = index
            c.getShort(ci(index)).toByte()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getByte(columnName: String): Byte {
        val index = findColumn(columnName)
        return getByte(index)
    }

    override fun getBytes(index: Int): ByteArray? {
        return try {
            lastColumnRead = index
            var bytes: ByteArray? = c.getBlob(ci(index))
            // SQLite includes the zero-byte at the end for Strings.
            if (bytes != null && c.getType(ci(index)) == Cursor.FIELD_TYPE_STRING) {
                bytes = Arrays.copyOf(bytes, bytes.size - 1)
            }
            bytes
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getBytes(columnName: String): ByteArray? {
        val index = findColumn(columnName)
        return getBytes(index)
    }

    override fun getCharacterStream(colID: Int): Reader? {
        return getString(colID)?.let { StringReader(it) }
    }

    override fun getCharacterStream(columnName: String): Reader? {
        return getCharacterStream(findColumn(columnName))
    }

    override fun getClob(colID: Int): ZkLiteClob? {
        return getString(colID)?.let { ZkLiteClob(it) }
    }

    override fun getClob(colName: String): Clob? {
        val index = findColumn(colName)
        return getClob(index)
    }

    override fun getConcurrency(): Int {
        return ResultSet.CONCUR_READ_ONLY
    }

    override fun getCursorName(): String {
        throw SQLFeatureNotSupportedException("getCursorName not supported")
    }

    override fun getDate(index: Int): Date? {
        return try {
            lastColumnRead = index
            val md = metaData
            var date: Date? = null
            when (md.getColumnType(index)) {
                Types.NULL -> return null
                Types.INTEGER, Types.BIGINT -> date = Date(getLong(index))
                Types.DATE -> date = c.getLong(index).let { Date(it) }
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

    override fun getDate(columnName: String): Date? {
        val index = findColumn(columnName)
        return getDate(index)
    }

    override fun getDate(colID: Int, cal: Calendar): Date {
        // TODO: Implement, perhaps as Xerial driver:
        //  https://github.com/xerial/sqlite-jdbc/blob/master/src/main/java/org/sqlite/jdbc3/JDBC3ResultSet.java#L313
        throw UnsupportedOperationException("getDate(int, Calendar) not implemented yet")
    }

    override fun getDate(columnName: String, cal: Calendar): Date {
        return getDate(findColumn(columnName), cal)
    }

    override fun getDouble(index: Int): Double {
        return try {
            lastColumnRead = index
            c.getDouble(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getDouble(columnName: String): Double {
        val index = findColumn(columnName)
        return getDouble(index)
    }

    override fun getFetchDirection(): Int {
        return ResultSet.FETCH_FORWARD
    }

    override fun getFetchSize(): Int {
        return limitRows
    }

    override fun getFloat(index: Int): Float {
        return try {
            lastColumnRead = index
            c.getFloat(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getFloat(columnName: String): Float {
        val index = findColumn(columnName)
        return getFloat(index)
    }

    override fun getInt(index: Int): Int {
        return try {
            lastColumnRead = index
            c.getInt(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getInt(columnName: String): Int {
        val index = findColumn(columnName)
        return getInt(index)
    }

    override fun getLong(index: Int): Long {
        return try {
            lastColumnRead = index
            c.getLong(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getLong(columnName: String): Long {
        val index = findColumn(columnName)
        return getLong(index)
    }

    override fun getMetaData(): ResultSetMetaData {
        return ZkLiteResultSetMetaData(c)
    }

    override fun getObject(colID: Int): Any? {
        lastColumnRead = colID
        val newIndex = ci(colID)
        return when (c.getType(newIndex)) {
            4 -> ZkLiteBlob(c.getBlob(newIndex)) //CONVERT TO BYTE[] OBJECT
            2 -> c.getFloat(newIndex)
            1 -> c.getLong(newIndex)
            3 -> c.getString(newIndex)
            0 -> null
            else -> c.getString(newIndex)
        }
    }

    override fun getObject(columnName: String): Any? {
        val index = findColumn(columnName)
        return getObject(index)
    }

    override fun getObject(columnIndex: Int, clazz: Map<String?, Class<*>?>?): Any {
        throw SQLFeatureNotSupportedException("Conversion not supported.  No conversions are supported.  This method will always throw.")
    }

    override fun getObject(columnName: String, clazz: Map<String?, Class<*>?>?): Any {
        return getObject(findColumn(columnName), clazz)
    }

////    override fun <T> getObject(columnIndex: Int, clazz: Class<T>): T {
//        // This method is entitled to throw if the conversion is not supported, so,
//        // since we don't support any conversions we'll throw.
//        // The only problem with this is that we're required to support certain conversion as specified in the docs.
//        throw SQLFeatureNotSupportedException("Conversion not supported.  No conversions are supported.  This method will always throw.")
//    }
//
////    override fun <T> getObject(columnName: String, clazz: Class<T>): T {
//        return getObject(findColumn(columnName), clazz)
//    }

    override fun getRef(colID: Int): Ref {
        throw SQLFeatureNotSupportedException("getRef not supported")
    }

    override fun getRef(columnName: String): Ref {
        return getRef(findColumn(columnName))
    }

    override fun getRow(): Int {
        return try {
            // convert to jdbc standard (counting from one)
            c.position + 1
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getShort(index: Int): Short {
        return try {
            lastColumnRead = index
            c.getShort(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getShort(columnName: String): Short {
        val index = findColumn(columnName)
        return getShort(index)
    }

    override fun getStatement(): Statement {
        // TODO: Implement as Xerial driver (which takes Statement as constructor argument)
        throw UnsupportedOperationException("Not implemented yet")
    }

    override fun getString(index: Int): String? {
        return try {
            lastColumnRead = index
            c.getString(ci(index))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun getString(columnName: String): String? {
        val index = findColumn(columnName)
        return getString(index)
    }

    override fun getTime(colID: Int): Time {
        // TODO: Implement as Xerial driver
        // https://github.com/xerial/sqlite-jdbc/blob/master/src/main/java/org/sqlite/jdbc3/JDBC3ResultSet.java#L449
        throw UnsupportedOperationException("Not implemented yet")
    }

    override fun getTime(columnName: String): Time {
        return getTime(findColumn(columnName))
    }

    override fun getTime(colID: Int, cal: Calendar): Time {
        // TODO: Implement as Xerial driver
        // https://github.com/xerial/sqlite-jdbc/blob/master/src/main/java/org/sqlite/jdbc3/JDBC3ResultSet.java#L449
        throw UnsupportedOperationException("Not implemented yet")
    }

    override fun getTime(columnName: String, cal: Calendar): Time {
        return getTime(findColumn(columnName), cal)
    }

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

    override fun getTimestamp(columnName: String): Timestamp? {
        val index = findColumn(columnName)
        return getTimestamp(index)
    }

    override fun getTimestamp(colID: Int, cal: Calendar): Timestamp? {
        // TODO Implement with Calendar
        Log.e(" ********************* not implemented correctly - Calendar is ignored. @ " + fileName + " line " + lineNumber)
        return getTimestamp(colID)
    }

    override fun getTimestamp(columnName: String, cal: Calendar): Timestamp? {
        return getTimestamp(findColumn(columnName), cal)
    }

    override fun getType(): Int {
        return ResultSet.TYPE_SCROLL_SENSITIVE
    }

    override fun getURL(colID: Int): URL {
        throw SQLFeatureNotSupportedException("ResultSet.getURL not supported")
    }

    override fun getURL(columnName: String): URL {
        return getURL(findColumn(columnName))
    }

    @Deprecated("since JDBC 2.0, use getCharacterStream")
    override fun getUnicodeStream(colID: Int): InputStream {
        throw SQLFeatureNotSupportedException("ResultSet.getUnicodeStream deprecated, use getCharacterStream instead")
    }

    @Deprecated("since JDBC 2.0, use getCharacterStream")
    override fun getUnicodeStream(columnName: String): InputStream {
        return getUnicodeStream(findColumn(columnName))
    }

    override fun getWarnings(): SQLWarning {
        // TODO: It may be that this is better implemented as "return null"
        throw UnsupportedOperationException("ResultSet.getWarnings not implemented yet")
    }

    override fun insertRow() {
        throw SQLFeatureNotSupportedException("ResultSet.insertRow not implemented yet")
    }

    override fun isAfterLast(): Boolean {
        return if (isClosed) {
            false
        } else try {
            c.isAfterLast
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun isBeforeFirst(): Boolean {
        return if (isClosed) {
            false
        } else try {
            c.isBeforeFirst
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun isFirst(): Boolean {
        return if (isClosed) {
            false
        } else try {
            c.isFirst
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun isLast(): Boolean {
        return if (isClosed) {
            false
        } else try {
            c.isLast
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun last(): Boolean {
        return try {
            c.moveToLast()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun moveToCurrentRow() {
        throw SQLFeatureNotSupportedException("moveToCurrentRow not supported")
    }

    override fun moveToInsertRow() {
        throw SQLFeatureNotSupportedException("moveToCurrentRow not supported")
    }

    override fun next(): Boolean {
        return try {
            c.moveToNext()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun previous(): Boolean {
        return try {
            c.moveToPrevious()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun refreshRow() {
        try {
            c.requery()
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun relative(rows: Int): Boolean {
        throw SQLFeatureNotSupportedException("relative not supported")
    }

    override fun rowDeleted(): Boolean {
        throw SQLFeatureNotSupportedException("rowDeleted not supported")
    }

    override fun rowInserted(): Boolean {
        throw SQLFeatureNotSupportedException("rowInserted not supported")
    }

    override fun rowUpdated(): Boolean {
        throw SQLFeatureNotSupportedException("rowUpdated not supported")
    }

    override fun setFetchDirection(direction: Int) {
        if (direction != ResultSet.FETCH_FORWARD) {
            throw java.sql.SQLException("only FETCH_FORWARD direction supported")
        }
    }

    override fun setFetchSize(rows: Int) {
        // TODO: Implement as max row number for next()
        if (rows != 0) {
            throw UnsupportedOperationException("Not implemented yet")
        }
        limitRows = rows
    }

    override fun updateArray(colID: Int, x: Array) {
        throw SQLFeatureNotSupportedException("updateArray not supported")
    }

    override fun updateArray(columnName: String, x: Array) {
        throw SQLFeatureNotSupportedException("updateArray not supported")
    }

    override fun updateAsciiStream(colID: Int, x: InputStream, length: Int) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    override fun updateAsciiStream(columnName: String, x: InputStream, length: Int) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    override fun updateBigDecimal(colID: Int, x: BigDecimal) {
        throw SQLFeatureNotSupportedException("updateBigDecimal not supported")
    }

    override fun updateBigDecimal(columnName: String, x: BigDecimal) {
        throw SQLFeatureNotSupportedException("updateBigDecimal not supported")
    }

    override fun updateBinaryStream(colID: Int, x: InputStream, length: Int) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    override fun updateBinaryStream(columnName: String, x: InputStream, length: Int) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    override fun updateBlob(colID: Int, x: Blob) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    override fun updateBlob(columnName: String, x: Blob) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    override fun updateBoolean(colID: Int, x: Boolean) {
        throw SQLFeatureNotSupportedException("updateBoolean not supported")
    }

    override fun updateBoolean(columnName: String, x: Boolean) {
        throw SQLFeatureNotSupportedException("updateBoolean not supported")
    }

    override fun updateByte(colID: Int, x: Byte) {
        throw SQLFeatureNotSupportedException("updateByte not supported")
    }

    override fun updateByte(columnName: String, x: Byte) {
        throw SQLFeatureNotSupportedException("updateByte not supported")
    }

    override fun updateBytes(colID: Int, x: ByteArray) {
        throw SQLFeatureNotSupportedException("updateBytes not supported")
    }

    override fun updateBytes(columnName: String, x: ByteArray) {
        throw SQLFeatureNotSupportedException("updateBytes not supported")
    }

    override fun updateCharacterStream(colID: Int, x: Reader, length: Int) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    override fun updateCharacterStream(columnName: String, reader: Reader, length: Int) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    override fun updateClob(colID: Int, x: Clob) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    override fun updateClob(columnName: String, x: Clob) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    override fun updateDate(colID: Int, x: Date) {
        throw SQLFeatureNotSupportedException("updateDate not supported")
    }

    override fun updateDate(columnName: String, x: Date) {
        throw SQLFeatureNotSupportedException("updateDate not supported")
    }

    override fun updateDouble(colID: Int, x: Double) {
        throw SQLFeatureNotSupportedException("updateDouble not supported")
    }

    override fun updateDouble(columnName: String, x: Double) {
        throw SQLFeatureNotSupportedException("updateDouble not supported")
    }

    override fun updateFloat(colID: Int, x: Float) {
        throw SQLFeatureNotSupportedException("updateFloat not supported")
    }

    override fun updateFloat(columnName: String, x: Float) {
        throw SQLFeatureNotSupportedException("updateFloat not supported")
    }

    override fun updateInt(colID: Int, x: Int) {
        throw SQLFeatureNotSupportedException("updateInt not supported")
    }

    override fun updateInt(columnName: String, x: Int) {
        throw SQLFeatureNotSupportedException("updateInt not supported")
    }

    override fun updateLong(colID: Int, x: Long) {
        throw SQLFeatureNotSupportedException("updateLong not supported")
    }

    override fun updateLong(columnName: String, x: Long) {
        throw SQLFeatureNotSupportedException("updateLong not supported")
    }

    override fun updateNull(colID: Int) {
        throw SQLFeatureNotSupportedException("updateNull not supported")
    }

    override fun updateNull(columnName: String) {
        throw SQLFeatureNotSupportedException("updateNull not supported")
    }

    override fun updateObject(colID: Int, x: Any) {
        throw SQLFeatureNotSupportedException("updateObject not supported")
    }

    override fun updateObject(columnName: String, x: Any) {
        throw SQLFeatureNotSupportedException("updateObject not supported")
    }

    override fun updateObject(colID: Int, x: Any, scale: Int) {
        throw SQLFeatureNotSupportedException("updateObject not supported")
    }

    override fun updateObject(columnName: String, x: Any, scale: Int) {
        throw SQLFeatureNotSupportedException("updateObject not supported")
    }

    override fun updateRef(colID: Int, x: Ref) {
        throw SQLFeatureNotSupportedException("updateRef not supported")
    }

    override fun updateRef(columnName: String, x: Ref) {
        throw SQLFeatureNotSupportedException("updateRef not supported")
    }

    override fun updateRow() {
        throw SQLFeatureNotSupportedException("updateRow not supported")
    }

    override fun updateShort(colID: Int, x: Short) {
        throw SQLFeatureNotSupportedException("updateShort not supported")
    }

    override fun updateShort(columnName: String, x: Short) {
        throw SQLFeatureNotSupportedException("updateShort not supported")
    }

    override fun updateString(colID: Int, x: String) {
        throw SQLFeatureNotSupportedException("updateString not supported")
    }

    override fun updateString(columnName: String, x: String) {
        throw SQLFeatureNotSupportedException("updateString not supported")
    }

    override fun updateTime(colID: Int, x: Time) {
        throw SQLFeatureNotSupportedException("updateTime not supported")
    }

    override fun updateTime(columnName: String, x: Time) {
        throw SQLFeatureNotSupportedException("updateTime not supported")
    }

    override fun updateTimestamp(colID: Int, x: Timestamp) {
        throw SQLFeatureNotSupportedException("updateTimestamp not supported")
    }

    override fun updateTimestamp(columnName: String, x: Timestamp) {
        throw SQLFeatureNotSupportedException("updateTimestamp not supported")
    }

    override fun wasNull(): Boolean {
        return try {
            c.isNull(ci(lastColumnRead))
        } catch (e: SQLException) {
            throw ZkLiteConnection.chainException(e)
        }
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return iface != null && iface.isAssignableFrom(javaClass)
    }

    override fun <T> unwrap(iface: Class<T>): T {
        if (isWrapperFor(iface)) {
            @Suppress("UNCHECKED_CAST") // isWrapper checks for it
            return this as T
        }
        throw java.sql.SQLException("$javaClass does not wrap $iface")
    }

    override fun getHoldability(): Int {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT
    }

    override fun getNCharacterStream(columnIndex: Int): Reader? {
        return getCharacterStream(columnIndex)
    }

    override fun getNCharacterStream(columnLabel: String): Reader? {
        return getNCharacterStream(findColumn(columnLabel))
    }

    override fun getNClob(columnIndex: Int): NClob? {
        return getClob(columnIndex)
    }

    override fun getNClob(columnLabel: String): NClob? {
        return getNClob(findColumn(columnLabel))
    }

    override fun getNString(columnIndex: Int): String? {
        return getString(columnIndex)
    }

    override fun getNString(columnLabel: String): String? {
        return getNString(findColumn(columnLabel))
    }

    override fun getRowId(columnIndex: Int): RowId {
        throw SQLFeatureNotSupportedException("getRowId not supported")
    }

    override fun getRowId(columnLabel: String): RowId {
        return getRowId(findColumn(columnLabel))
    }

    override fun getSQLXML(columnIndex: Int): SQLXML {
        throw SQLFeatureNotSupportedException("getSQLXML not supported")
    }

    override fun getSQLXML(columnLabel: String): SQLXML {
        return getSQLXML(findColumn(columnLabel))
    }

    override fun isClosed(): Boolean {
        return c.isClosed
    }

    override fun updateAsciiStream(columnIndex: Int, x: InputStream) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    override fun updateAsciiStream(columnLabel: String, x: InputStream) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    override fun updateAsciiStream(columnIndex: Int, x: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    override fun updateAsciiStream(columnLabel: String, x: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateAsciiStream not supported")
    }

    override fun updateBinaryStream(columnIndex: Int, x: InputStream) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    override fun updateBinaryStream(columnLabel: String, x: InputStream) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    override fun updateBinaryStream(columnIndex: Int, x: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    override fun updateBinaryStream(columnLabel: String, x: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateBinaryStream not supported")
    }

    override fun updateBlob(columnIndex: Int, inputStream: InputStream) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    override fun updateBlob(columnLabel: String, inputStream: InputStream) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    override fun updateBlob(columnIndex: Int, inputStream: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    override fun updateBlob(columnLabel: String, inputStream: InputStream, length: Long) {
        throw SQLFeatureNotSupportedException("updateBlob not supported")
    }

    override fun updateCharacterStream(columnIndex: Int, x: Reader) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    override fun updateCharacterStream(columnLabel: String, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    override fun updateCharacterStream(columnIndex: Int, x: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    override fun updateCharacterStream(columnLabel: String, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateCharacterStream not supported")
    }

    override fun updateClob(columnIndex: Int, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    override fun updateClob(columnLabel: String, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    override fun updateClob(columnIndex: Int, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    override fun updateClob(columnLabel: String, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateClob not supported")
    }

    override fun updateNCharacterStream(columnIndex: Int, x: Reader) {
        throw SQLFeatureNotSupportedException("updateNCharacterStream not supported")
    }

    override fun updateNCharacterStream(columnLabel: String, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateNCharacterStream not supported")
    }

    override fun updateNCharacterStream(columnIndex: Int, x: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateNCharacterStream not supported")
    }

    override fun updateNCharacterStream(columnLabel: String, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateNCharacterStream not supported")
    }

    override fun updateNClob(columnIndex: Int, nClob: NClob) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    override fun updateNClob(columnLabel: String, nClob: NClob) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    override fun updateNClob(columnIndex: Int, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    override fun updateNClob(columnLabel: String, reader: Reader) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    override fun updateNClob(columnIndex: Int, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    override fun updateNClob(columnLabel: String, reader: Reader, length: Long) {
        throw SQLFeatureNotSupportedException("updateNClob not supported")
    }

    override fun updateNString(columnIndex: Int, nString: String) {
        throw SQLFeatureNotSupportedException("updateNString not supported")
    }

    override fun updateNString(columnLabel: String, nString: String) {
        throw SQLFeatureNotSupportedException("updateNString not supported")
    }

    override fun updateRowId(columnIndex: Int, value: RowId) {
        throw SQLFeatureNotSupportedException("updateRowId not supported")
    }

    override fun updateRowId(columnLabel: String, value: RowId) {
        throw SQLFeatureNotSupportedException("updateRowId not supported")
    }

    override fun updateSQLXML(columnIndex: Int, xmlObject: SQLXML) {
        throw SQLFeatureNotSupportedException("updateSQLXML not supported")
    }

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