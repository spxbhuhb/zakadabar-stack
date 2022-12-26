/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import kotlinx.datetime.Clock
import org.khronos.webgl.ArrayBuffer
import org.w3c.files.Blob
import kotlin.js.Json
import kotlin.js.Promise
import kotlin.js.json

internal actual fun ContentMap.generateZip(zipContent: Any) {

    val bc = (zipContent as? BlobConsumer)
        ?: throw IllegalArgumentException("Output type not supported : ${zipContent::class.simpleName}")

    val zip = JSZip()

    for ((path, content) in this) {
        val b = StringBuilder()
        content(b::append)
        val data = b.toString()
        zip.file(path, data, json("binary" to false))
    }

    zip.generateAsync<Blob>(json(
        "type" to "blob",
        "mimeType" to bc.contentType,
        "compression" to "DEFLATE",
        "compressionOptions" to json("level" to 9)
    )).then(bc.blob)
}

internal class BlobConsumer(val contentType: String, val blob: Blob.()->Unit)

@JsModule("jszip")
@JsNonModule
private external class JSZip {
    fun <T> file(path: String, content: T, options: Json) : Unit = definedExternally
    fun <T> generateAsync(options: Json) : Promise<T> = definedExternally

}
