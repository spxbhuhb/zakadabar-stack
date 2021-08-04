/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.android.jdbc.zklite

import java.io.FileNotFoundException
import java.io.PrintWriter
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.util.*
import java.util.logging.Logger
import javax.sql.DataSource

open class ZkLiteDataSource : DataSource {

    var packageName: String? = null
    var databaseName: String? = null

    constructor()

    constructor(packageName: String?, databaseName: String?) {
        this.packageName = packageName
        this.databaseName = databaseName
    }

    override fun getConnection(): Connection {
        val url = "jdbc:sqlite:/data/data/$packageName/$databaseName.db"
        return ZkLiteDriver().connect(url, Properties())
    }

    override fun getConnection(username: String, password: String): Connection {
        return getConnection()
    }

    override fun getLogWriter(): PrintWriter {
        var logWriter: PrintWriter? = null
        try {
            logWriter = PrintWriter("droid.log")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return logWriter !!
    }

    override fun getLoginTimeout(): Int {
        return 0
    }

    override fun setLogWriter(out: PrintWriter) {
        try {
            DriverManager.setLogWriter(PrintWriter("droid.log"))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun setLoginTimeout(seconds: Int) {
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        return iface != null && iface.isAssignableFrom(javaClass)
    }

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