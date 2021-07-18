/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

import java.sql.*
import java.util.*

class ZkLiteDriver : Driver {
    companion object {
        /** Key passed when the SQLDroidConnection is created.  The value of this key should be an
         * String containing a numeric value which is the complete value of the flags to be passed to the Android SQLite database
         * when the database is opened. If this key is not set, or the passed properties are null then the values
         *
         *
         * android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY | android.database.sqlite.SQLiteDatabase.OPEN_READWRITE
         *
         *
         * will be passed.
         * @see android.database.sqlite.SQLiteDatabase.openDatabase
         */
        const val DATABASE_FLAGS = "DatabaseFlags"

        /** Key passed when the SQLDroidConnection is created.  The value of this key should be an
         * String containing a numeric value which is the value of any additional flags to be passed to the
         * Android SQLite database when the database is opened. Additional flags will be added to this set then the values
         *
         *
         * android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY | android.database.sqlite.SQLiteDatabase.OPEN_READWRITE
         *
         *
         * If the passed properties are null then just these default keys will be used.
         * @see android.database.sqlite.SQLiteDatabase.openDatabase
         */
        const val ADDITONAL_DATABASE_FLAGS = "AdditionalDatabaseFlags"

        // TODO(uwe):  Allow jdbc:sqlite: url as well
        var sqldroidPrefix = "jdbc:sqldroid:"

        /** Provide compatibility with the SQLite JDBC driver from Xerial:
         *
         *
         * http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC
         *
         *
         * by allowing the URLs to be jdbc:sqlite:
         */
        // this used to be "sqlitePrefix" but it looks too similar to sqldroidPrefix
        // making the code hard to read and easy to mistype.
        var xerialPrefix = "jdbc:sqlite:"

        var className = ZkLiteDriver::class.qualifiedName!!
        var driverName = "SQLDroid"
        var databaseProductName = "SQLite for Android"

        init {
            try {
                DriverManager.registerDriver(ZkLiteDriver())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /** Will accept any string that starts with sqldroidPrefix ("jdbc:sqldroid:") or
     * sqllitePrefix ("jdbc:sqlite").
     */
    @Throws(SQLException::class)
    override fun acceptsURL(url: String): Boolean {
        return if (url.startsWith(sqldroidPrefix) || url.startsWith(xerialPrefix)) {
            true
        } else false
    }

    @Throws(SQLException::class)
    override fun connect(url: String, info: Properties): Connection {
        return ZkLiteConnection(url, info)
    }

    override fun getMajorVersion(): Int {
        return 1
    }

    override fun getMinorVersion(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun getPropertyInfo(url: String, info: Properties): Array<DriverPropertyInfo> {
        // TODO Evaluate if implementation is sufficient (if so, delete comment and log)
        Log.e(
            " ********************* not implemented @ " + fileName + " line "
                    + lineNumber
        )
        return emptyArray()
    }

    override fun jdbcCompliant(): Boolean {
        return false
    }

}