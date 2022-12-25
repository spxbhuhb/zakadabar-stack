/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import org.w3c.files.Blob
import kotlin.js.Json
import kotlin.js.Promise
import kotlin.js.json

internal actual fun ContentMap.generateZip(zipContent: ByteArray.() -> Unit) {
    val zip = JSZip()

    for ((path, content) in this) {
        val data = content()
        val blob = Blob(arrayOf(data))
        zip.file(path, blob, json("binary" to true))
    }

    zip.generateAsync(
        json(
            "type" to "blob",
            "compression" to "DEFLATE",
            "compressionOptions" to json("level" to 9)
        )
    ).then(zipContent)

}

@JsModule("jszip")
@JsNonModule
private external class JSZip {
    fun file(path: String, content: Blob, options: Json) : Unit = definedExternally
    fun generateAsync(options: Json) : Promise<ByteArray> = definedExternally
}
