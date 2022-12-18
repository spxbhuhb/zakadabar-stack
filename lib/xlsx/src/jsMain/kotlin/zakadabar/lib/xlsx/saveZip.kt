/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import zakadabar.core.browser.util.downloadBlob
import kotlin.js.json

actual fun FileContainer.saveZip(name: String, contentType: String) {

    val zip = JSZip();

    forEachSorted {
        when(it) {
            is FileContainer.DirectoryEntry -> zip.folder(it.path)
            is FileContainer.FileEntry -> {
                zip.file(it.path, it.content.toUint8Array(), json("binary" to true).asDynamic() )
            }
        }
    }

    zip.generateAsync(json(
        "type" to "blob",
        "mimeType" to contentType
    ).asDynamic()).then { content ->
        downloadBlob(name, content)
    }

}

@JsModule("jszip")
@JsNonModule
private external class JSZip {

    fun folder(path: String) : Unit = definedExternally
    fun file(path: String, content: Uint8Array, options: dynamic) : Unit = definedExternally
    fun generateAsync(options: dynamic) : dynamic = definedExternally

}

private fun ByteArray.toUint8Array() = Uint8Array(size).also {
    forEachIndexed { i, byte ->
        it[i] = byte
    }
}

