/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * write content into java.io.OutputStream
 */
internal actual fun ContentMap.generateZip(zipStream: OutputStream) {

    ZipOutputStream(zipStream).use { zip ->

        zip.setLevel(9)

        val writer = zip.bufferedWriter()

        for((path, content) in this) {
            zip.putNextEntry(ZipEntry(path))
            content(writer::write) // write xml parts directly to the Stream
            writer.flush()
            zip.closeEntry()
        }

    }

}

