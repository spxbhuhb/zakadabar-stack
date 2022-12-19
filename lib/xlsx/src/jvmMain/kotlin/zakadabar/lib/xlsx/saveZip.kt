/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

actual fun ContentMap.saveZip(name: String, contentType: String) {
    ZipOutputStream(FileOutputStream(name)).use { zip ->

        zip.setLevel(9)

        forEach {
            val path = it.key
            val content = it.value()

            zip.putNextEntry(ZipEntry(path))
            zip.write(content)
            zip.closeEntry()
        }

    }

}
