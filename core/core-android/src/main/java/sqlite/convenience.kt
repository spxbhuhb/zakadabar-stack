/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.android.jdbc.sqlite

import org.jetbrains.exposed.sql.Database
import java.sql.Driver
import java.sql.DriverManager

var initialized = false

fun Database.connectSqlite() {
    synchronized(initialized) {
        if (!initialized) DriverManager.registerDriver(ZkLiteDriver() as Driver)
        initialized = true
    }
}