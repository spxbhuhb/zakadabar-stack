/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal actual fun ContentMap.generateZip(zipContent: ByteArray.() -> Unit) {
    val os = ByteArrayOutputStream()

    ZipOutputStream(os).use { zip ->

        zip.setLevel(9)

        for( (path, content) in this) {
            val data = runBlocking { content() }

            zip.putNextEntry(ZipEntry(path))
            when(data) {
                is String -> zip.write(data.encodeToByteArray())
                is ByteArray -> zip.write(data)
                else -> IllegalArgumentException("Content not supported: ${data::class}")
            }

            zip.closeEntry()
        }

    }

    zipContent(os.toByteArray())

}
