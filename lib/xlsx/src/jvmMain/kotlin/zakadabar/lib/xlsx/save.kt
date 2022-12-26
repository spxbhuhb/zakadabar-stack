/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.lib.xlsx.internal.OutputStream
import zakadabar.lib.xlsx.internal.buildFileContent
import zakadabar.lib.xlsx.model.XlsxDocument
import java.io.FileOutputStream

/**
 * Save xlsx file.
 */
actual fun XlsxDocument.save(fileName: String) {
    FileOutputStream(fileName).use(::writeTo)
}

/**
 * Generate XlsxFile and Write it to the OutputStream
 */
fun XlsxDocument.writeTo(os: java.io.OutputStream) {
    buildFileContent(OutputStream(os))
}
