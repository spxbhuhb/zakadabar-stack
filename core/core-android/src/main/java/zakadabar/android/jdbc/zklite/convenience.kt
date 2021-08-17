/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.android.jdbc.zklite

import android.content.Context
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.vendors.SQLiteDialect
import zakadabar.core.util.PublicApi

/**
 * Connect to an SQLite database through the ZkLite JDBC driver.
 *
 * @param  context      The context to open the database in. This usually results
 *                      in a directory: `/data/data/$packageName/databases` where
 *                      packageName is the Android package name.
 * @param  databaseName Name of the database to connect to. The actual file name
 *                      is: `$databaseName.db`.
 */
@PublicApi
fun Database.Companion.connectSqlite(context: Context, databaseName: String): Database {

    val jdbcUrl = "jdbc:zklite:${context.filesDir.parent !! + "/databases"}/$databaseName.db"

    registerJdbcDriver(
        "jdbc:zklite",
        ZkLiteDriver.className,
        SQLiteDialect.dialectName
    )

    return connect(jdbcUrl, ZkLiteDriver.className)

}

/**
 * Get the file system path to the databases of this context.
 */
@PublicApi
val Context.databasesPath
    get() = this.filesDir.parent !! + "/databases"
