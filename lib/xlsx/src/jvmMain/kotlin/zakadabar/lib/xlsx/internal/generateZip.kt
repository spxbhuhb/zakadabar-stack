/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * write content into java.io.OutputStream
 */
internal actual fun <T> ContentMap.generateZip(writer: ContentWriter<T>) {

    ZipOutputStream(writer.out).use { zip ->

        zip.setLevel(9)

        val w = zip.bufferedWriter()

        for((path, content) in this) {
            zip.putNextEntry(ZipEntry(path))
            content(w::write)
            w.flush()
            zip.closeEntry()
        }

    }

}

/**
 * write xlsx zip data
 */
internal actual class ContentWriter<T>(val out: OutputStream)
