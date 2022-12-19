/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import org.w3c.files.Blob
import zakadabar.core.browser.util.downloadBlob
import kotlin.js.Json
import kotlin.js.Promise
import kotlin.js.json
actual fun ContentMap.saveZip(name: String, contentType: String) {
    val zip = JSZip()

    forEach {
        val path = it.key
        val content = it.value()
        zip.file(path, content.toUint8Array(), json("binary" to true) )
    }

    zip.generateAsync<Blob>(json(
        "type" to "blob",
        "mimeType" to contentType,
        "compression" to "DEFLATE",
        "compressionOptions" to json("level" to 9)
    )).then { downloadBlob(name, it) }

}

@JsModule("jszip")
@JsNonModule
private external class JSZip {

    fun file(path: String, content: Uint8Array, options: Json) : Unit = definedExternally
    fun <T> generateAsync(options: Json) : Promise<T> = definedExternally

}

private fun ByteArray.toUint8Array() = Uint8Array(size).also {
    forEachIndexed { i, byte ->
        it[i] = byte
    }
}

