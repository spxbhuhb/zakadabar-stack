/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import org.khronos.webgl.ArrayBuffer
import kotlin.js.Json
import kotlin.js.json

/**
 * generate and write zip data to OutputStream
 * uses JSZip nodejs library
 */
internal actual fun <T> ContentMap.generateZip(writer: ContentWriter<T>) {

    val zip = JSZip<String, ArrayBuffer>()

    // generating xml data and put them into zip
    for ((path, content) in this) {
        val b = StringBuilder()
        content(b::append) // collect xml data into a string
        val data = b.toString()
        zip.file(path, data, json("binary" to false))
    }

    // generating zip content
    zip.generateInternalStream(json(
        "type" to "arraybuffer",
        "compression" to "DEFLATE",
        "compressionOptions" to json("level" to 9),
        "streamFiles" to true
    )).on<T, dynamic>("data") { data, _ ->
        writer.out(data)
    }.on<Error>("error") { e->
        console.error("Error in zip process", e)
    }.on("end") {
        writer.close()
    }.resume()

}

/**
 * write xlsx zip data
 */
internal actual class ContentWriter<T>(val out: (T)->Unit, val close: ()->Unit)

@JsModule("jszip")
@JsNonModule
private external class JSZip<I, O> {
    fun file(path: String, content: I, options: Json) : Unit = definedExternally
    fun generateInternalStream(options: Json) : StreamHelper<O> = definedExternally

}


@JsModule("jszip")
@JsNonModule
private external class StreamHelper<T> {
    fun <E,M> on(event: String, callback: (E, M)->Unit) : StreamHelper<T> = definedExternally
    fun <E> on(event: String, callback: (E)->Unit) : StreamHelper<T> = definedExternally
    fun on(event: String, callback: ()->Unit) : StreamHelper<T> = definedExternally
    fun resume() : StreamHelper<T> = definedExternally

}


