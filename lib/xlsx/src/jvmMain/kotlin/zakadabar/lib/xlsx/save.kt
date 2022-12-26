/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import zakadabar.lib.xlsx.internal.buildFileContent
import zakadabar.lib.xlsx.model.XlsxDocument
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Save xlsx file.
 */
actual fun XlsxDocument.save(fileName: String) {
    FileOutputStream(fileName).use {
        writeTo(it)
    }
}

/**
 * Generate XlsxFile and Write it to the OutputStream
 */
fun XlsxDocument.writeTo(os: OutputStream) = buildFileContent(os)
