/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.zklite

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverManager
import java.sql.DriverPropertyInfo
import java.util.*

class ZkLiteDriver : Driver {
    companion object {
        /** Key passed when the ZkLiteConnection is created.  The value of this key should be an
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

        /** Key passed when the ZkLiteConnection is created.  The value of this key should be an
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

        var zkLitePrefix = "jdbc:sqlite:"

        var className = ZkLiteDriver::class.qualifiedName!!
        var driverName = "SQLite JDBC" // Exposed fails without this
        var databaseProductName = "SQLite for Android"

        /**
         * When true [ZkLiteResultSet.getObject] returns with a [ByteArray] instead
         * of a [ZkLiteBlob].
         *
         * Default is false (returns with ZkLiteBlob).
         *
         * The default behaviour of the SQLDroid driver (on which this ZkLite driver based on)
         * is to return with [ZkLiteBlob].
         *
         * This flag was introduced to let Exposed handle UUID data types without the need
         * of a modified SQLite dialect.
         */
        var useByteArrayBlob = false

        init {
            DriverManager.registerDriver(ZkLiteDriver())
        }
    }

    override fun acceptsURL(url: String): Boolean {
        return url.startsWith(zkLitePrefix)
    }

    override fun connect(url: String, info: Properties): Connection {
        return ZkLiteConnection(url, info)
    }

    override fun getMajorVersion(): Int {
        return 1
    }

    override fun getMinorVersion(): Int {
        return 0
    }

    override fun getPropertyInfo(url: String, info: Properties): Array<DriverPropertyInfo> {
        // TODO Evaluate if implementation is sufficient (if so, delete comment and log)
        Log.e(" ********************* not implemented @ $fileName line $lineNumber")
        return emptyArray()
    }

    override fun jdbcCompliant(): Boolean {
        return false
    }

}