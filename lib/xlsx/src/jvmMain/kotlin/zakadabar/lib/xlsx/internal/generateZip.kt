/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal actual fun ContentMap.generateZip(zipContent: Any) {

    val os = (zipContent as? OutputStream)
        ?: throw IllegalArgumentException("Output type not supported : ${zipContent::class.simpleName}")

    ZipOutputStream(os).use { zip ->

        zip.setLevel(9)

        val writer = zip.bufferedWriter()

        for((path, content) in this) {
            zip.putNextEntry(ZipEntry(path))
            content(writer::write)
            writer.flush()
            zip.closeEntry()
        }

    }

}
