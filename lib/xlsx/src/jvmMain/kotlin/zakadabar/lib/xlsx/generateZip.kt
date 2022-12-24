/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

actual fun ContentMap.generateZip(content: ByteArray.() -> Unit) {
    val os = ByteArrayOutputStream()

    ZipOutputStream(os).use { zip ->

        zip.setLevel(9)

        forEach { (path, content) ->
            zip.putNextEntry(ZipEntry(path))
            zip.write(content())
            zip.closeEntry()
        }

    }

    content(os.toByteArray())

}
