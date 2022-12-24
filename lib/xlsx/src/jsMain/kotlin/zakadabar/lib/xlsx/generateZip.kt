/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import kotlin.js.Json
import kotlin.js.Promise
import kotlin.js.json

internal actual fun ContentMap.generateZip(content: ByteArray.() -> Unit) {
    val zip = JSZip()

    forEach { (path,content) ->
        zip.file(path, content().toUint8Array(), json("binary" to true) )
    }

    val options = json("level" to 9)

    zip.generateAsync<Uint8Array>(json(
        "type" to "uint8array",
//        "mimeType" to contentType,
        "compression" to "DEFLATE",
        "compressionOptions" to options
    )).then { content(it.toByteArray()) }

}

@JsModule("jszip")
@JsNonModule
private external class JSZip {
    fun file(path: String, content: Uint8Array, options: Json) : Unit = definedExternally
    fun <T> generateAsync(options: Json) : Promise<T> = definedExternally
}

private fun ByteArray.toUint8Array() = Uint8Array(size).also {
    for(i in 0..size) it[i] = this[i]
}

private fun Uint8Array.toByteArray() = ByteArray(length).also {
    for(i in 0..length) it[i] = this[i]
}

