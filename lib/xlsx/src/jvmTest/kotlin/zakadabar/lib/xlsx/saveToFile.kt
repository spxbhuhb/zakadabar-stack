/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import java.io.FileOutputStream

actual fun ByteArray.saveToFile(name: String) {
    FileOutputStream(name).use {
        it.write(this)
    }
}
