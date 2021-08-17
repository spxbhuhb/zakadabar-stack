/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.persistence.sqlite

import zakadabar.core.util.PublicApi
import java.io.File

/**
 * Create a new database from a [databaseBundle]. Intended to save databases
 * transferred over the network.
 *
 * @param  path            The path to the directory that contains the database file.
 *                         If the directory does not exists, it will be created.
 * @param  databaseName    Name of the database to connect to. The actual file name
 *                         is: `$databaseName.db`.
 * @param  databaseBundle  The content of the database file. Typically built with
 *                         the `buildBundle` function in "Lib: Blobs".
 * @param  overwrite       When true, any existing files that belong to this db are
 *                         deleted from the database directory before creating a new
 *                         database. The delete is based on file name: `$databaseName.db*`.
 */
@PublicApi
fun deployBundle(path : String, databaseName: String, databaseBundle: ByteArray, overwrite: Boolean = true) {
    val dir = File(path)
    val fileName = "${databaseName}.db"
    val file = File(dir, fileName)

    if (!dir.exists()) {
        dir.mkdirs() // TODO permissions on this
    }

    if (file.exists()) {
        if (! overwrite) throw IllegalStateException("database file $file already exists")
        dir.list { _, s -> s.startsWith(fileName) }
    }

    file.outputStream().use { os ->
        os.write(databaseBundle)
    }
}