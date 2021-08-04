/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.sqlite

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.util.PublicApi
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.absolute

/**
 * Convenience function for bundle build
 *
 * 1. creates or opens an SQLite database
 * 1. calls [builder]
 * 1. closes and unregisters the database
 * 1. reads the database file into a byte array
 * 1. returns with that byte array
 *
 * @param  path         The file path to the bundle file.
 * @param  append       When true, an existing file may be opened and updated.
 * @param  deleteAfter  When true, the SQLite DB file is deleted.
 *
 * @returns  The content of the SQLite database file.
 */
@PublicApi
fun buildBundle(
    path: String,
    tables : List<Table> = emptyList(),
    append: Boolean = false,
    deleteAfter: Boolean = true,
    builder: (Database) -> Unit
): ByteArray {

    val aPath = Paths.get(path).absolute()

    if (! append && Files.exists(aPath)) throw IllegalArgumentException("file at $aPath already exists")

    val bundleDb = Database.connect("jdbc:sqlite:$aPath")

    if (tables.isNotEmpty()) {
        transaction(bundleDb) {
            SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())
        }
    }

    builder(bundleDb)

    TransactionManager.closeAndUnregister(bundleDb)

    val bytes = Files.readAllBytes(aPath)

    if (deleteAfter) Files.delete(aPath)

    return bytes
}
