/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.sqlite

import java.io.FileNotFoundException
import java.io.PrintWriter
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.util.*
import java.util.logging.Logger
import javax.sql.DataSource

open class DroidDataSource : DataSource {

    var description = "Android Sqlite Data Source"
    var packageName: String? = null
    var databaseName: String? = null

    constructor()

    constructor(packageName: String?, databaseName: String?) {
        this.packageName = packageName
        this.databaseName = databaseName
    }

    override fun getConnection() : Connection {
        val url = "jdbc:sqldroid:/data/data/$packageName/$databaseName.db"
        return SQLDroidDriver().connect(url, Properties())
    }

    @Throws(SQLException::class)
    override fun getConnection(username: String, password: String): Connection {
        return getConnection()
    }

    @Throws(SQLException::class)
    override fun getLogWriter(): PrintWriter {
        var logWriter: PrintWriter? = null
        try {
            logWriter = PrintWriter("droid.log")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return logWriter !!
    }

    @Throws(SQLException::class)
    override fun getLoginTimeout(): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun setLogWriter(out: PrintWriter) {
        try {
            DriverManager.setLogWriter(PrintWriter("droid.log"))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    @Throws(SQLException::class)
    override fun setLoginTimeout(seconds: Int) {
    }

    @Throws(SQLException::class)
    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return iface != null && iface.isAssignableFrom(javaClass)
    }

    @Throws(SQLException::class)
    override fun <T> unwrap(iface: Class<T>): T {
        if (isWrapperFor(iface)) {
            @Suppress("UNCHECKED_CAST") // isWrapperFor checks
            return this as T
        }
        throw SQLException("$javaClass does not wrap $iface")
    }

    @Throws(SQLFeatureNotSupportedException::class)
    override fun getParentLogger(): Logger? {
        return null
    }
}