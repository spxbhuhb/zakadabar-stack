/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import kotlin.js.Json
import kotlin.js.json

/**
 * generate and write zip data to OutputStream
 * uses JSZip nodejs library
 */
internal actual fun ContentMap.generateZip(zipStream: OutputStream) {

    val zip = JSZip()

    // generating xml data and put them into zip
    for ((path, content) in this) {
        val b = StringBuilder()
        content(b::append) // collect xml data into a string
        val data = b.toString()
        zip.file(path, data, json("binary" to false))
    }

    // generating zip content
    zip.generateInternalStream(json(
        "type" to "arraybuffer", // ArrayBuffer as same as ByteArray, but with different name
        "compression" to "DEFLATE",
        "compressionOptions" to json("level" to 9),
        "streamFiles" to true
    )).on("data") { data, _ ->
        @Suppress("UnsafeCastFromDynamic")
        zipStream.write(data) // do not cast!
    }.on("error") { e->
        console.error("Error in zip process", e)
    }.on("end") {
        zipStream.close()
    }.resume()

}

@JsModule("jszip")
@JsNonModule
private external class JSZip {
    fun <T> file(path: String, content: T, options: Json) : Unit = definedExternally
    fun generateInternalStream(options: Json) : dynamic = definedExternally

}

