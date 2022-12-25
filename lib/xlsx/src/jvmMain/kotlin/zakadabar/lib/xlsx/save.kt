/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.lib.xlsx.buildFileContent
import zakadabar.lib.xlsx.model.XlsxDocument
import java.io.File

actual fun XlsxDocument.save(fileName: String) {
    buildFileContent {
        File(fileName).writeBytes(this)
    }
}