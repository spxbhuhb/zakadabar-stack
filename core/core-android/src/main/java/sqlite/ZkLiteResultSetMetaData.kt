/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

import android.database.Cursor
import java.lang.reflect.Method
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Types

class ZkLiteResultSetMetaData(cursor: Cursor?) : ResultSetMetaData {
    private val cursor: Cursor

    companion object {
        private var getType: Method? = null
        fun getType(cursor: Cursor?, column: Int): Int {
            if (getType != null) {
                try {
                    return getType !!.invoke(cursor, column) as Int
                } catch (e: Exception) {
                }
            }
            return Types.OTHER // return something that can be used to understand that the type is unknown.
        }

        init {
            getType = try {
                Cursor::class.java.getMethod("getType", Int::class.javaPrimitiveType)
            } catch (e: Exception) {
                null
            }
        }
    }

    @Throws(SQLException::class)
    override fun getCatalogName(column: Int): String {
        return getTableName(column)
    }

    @Throws(SQLException::class)
    override fun getColumnClassName(column: Int): String {
        // TODO In Xerial this is implemented as return "java.lang.Object";
        // TODO To a lookup based on getColumnType
        throw UnsupportedOperationException("getColumnClassName not implemented yet")
    }

    @Throws(SQLException::class)
    override fun getColumnCount(): Int {
        return cursor.columnCount
    }

    @Throws(SQLException::class)
    override fun getColumnDisplaySize(column: Int): Int {
        return Int.MAX_VALUE
    }

    @Throws(SQLException::class)
    override fun getColumnLabel(column: Int): String {
        return cursor.getColumnName(column - 1)
    }

    @Throws(SQLException::class)
    override fun getColumnName(column: Int): String {
        return cursor.getColumnName(column - 1)
    }

    @Throws(SQLException::class)
    override fun getColumnType(column: Int): Int {
        val oldPos = cursor.position
        var moved = false
        if (cursor.isBeforeFirst || cursor.isAfterLast) {
            val resultSetEmpty = cursor.count == 0 || cursor.isAfterLast
            if (resultSetEmpty) {
                return Types.NULL
            }
            cursor.moveToFirst()
            moved = true
        }
        val nativeType = getType(cursor, column - 1)
        val type: Int
        type = when (nativeType) {
            0 -> Types.NULL
            1 -> Types.INTEGER
            2 -> Types.FLOAT
            3 -> Types.VARCHAR
            4 -> Types.BLOB
            else -> Types.NULL
        }
        if (moved) {
            cursor.moveToPosition(oldPos)
        }
        return type
    }

    @Throws(SQLException::class)
    override fun getColumnTypeName(column: Int): String {
        return when (getColumnType(column)) {
            Types.NULL -> "NULL"
            Types.INTEGER -> "INTEGER"
            Types.FLOAT -> "FLOAT"
            Types.VARCHAR -> "VARCHAR"
            Types.BLOB -> "BLOB"
            else -> "OTHER"
        }
    }

    @Throws(SQLException::class)
    override fun getPrecision(column: Int): Int {
        throw UnsupportedOperationException("getPrecision not implemented yet")
    }

    @Throws(SQLException::class)
    override fun getScale(column: Int): Int {
        throw UnsupportedOperationException("getScale not implemented yet")
    }

    @Throws(SQLException::class)
    override fun getSchemaName(column: Int): String {
        return ""
    }

    @Throws(SQLException::class)
    override fun getTableName(column: Int): String {
        // TODO Supported in Xerial driver with db.column_table_name(stmt.pointer, checkCol(col))
        throw UnsupportedOperationException("getTableName not implemented yet")
    }

    @Throws(SQLException::class)
    override fun isAutoIncrement(column: Int): Boolean {
        throw UnsupportedOperationException("isAutoIncrement not implemented yet")
    }

    @Throws(SQLException::class)
    override fun isCaseSensitive(column: Int): Boolean {
        return true
    }

    @Throws(SQLException::class)
    override fun isCurrency(column: Int): Boolean {
        return false
    }

    @Throws(SQLException::class)
    override fun isDefinitelyWritable(column: Int): Boolean {
        // TODO Evaluate if this is a sufficient implementation (if so, delete comment and log)
        Log.e(
            " ********************* not implemented @ " + fileName + " line "
                    + lineNumber
        )
        return false
    }

    @Throws(SQLException::class)
    override fun isNullable(column: Int): Int {
        throw UnsupportedOperationException("isNullable not implemented yet")
    }

    @Throws(SQLException::class)
    override fun isReadOnly(column: Int): Boolean {
        // TODO Evaluate if the implementation is sufficient (if so, delete comment and log)
        Log.e(
            " ********************* not implemented @ " + fileName + " line "
                    + lineNumber
        )
        return true
    }

    @Throws(SQLException::class)
    override fun isSearchable(column: Int): Boolean {
        // TODO Evaluate if the implementation is sufficient (if so, delete comment and log)
        Log.e(
            " ********************* not implemented @ " + fileName + " line "
                    + lineNumber
        )
        return true
    }

    @Throws(SQLException::class)
    override fun isSigned(column: Int): Boolean {
        // TODO Evaluate if the implementation is sufficient (if so, delete comment and log)
        Log.e(
            " ********************* not implemented @ " + fileName + " line "
                    + lineNumber
        )
        return true
    }

    @Throws(SQLException::class)
    override fun isWritable(column: Int): Boolean {
        // TODO Evaluate if the implementation is sufficient (if so, delete comment and log)
        Log.e(
            " ********************* not implemented @ " + fileName + " line "
                    + lineNumber
        )
        return false
    }

    @Throws(SQLException::class)
    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return iface != null && iface.isAssignableFrom(javaClass)
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>): T {
        if (isWrapperFor(iface)) {
            @Suppress("UNCHECKED_CAST") // isWrapperFor checks it
            return this as T
        }
        throw SQLException("$javaClass does not wrap $iface")
    }

    init {
        if (cursor == null) {
            throw NullPointerException("Cursor required to be not null.")
        }
        this.cursor = cursor
    }
}