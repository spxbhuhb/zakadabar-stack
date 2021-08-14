/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.util

import kotlinx.browser.document
import org.khronos.webgl.Uint8Array
import org.w3c.dom.HTMLElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

fun escape(source: String) = source.replace("&", "&amp;").replace("<", "&lt;")

fun makeString(source: List<Int>): String {
    // TODO if it is possible, change the toIntArray to something better
    return js("String.fromCodePoint").apply(null, source.toIntArray()) as String
}

fun downloadCsv(filename: String, content: String) {
    val bom = Uint8Array(byteArrayOf(0xef.toByte(), 0xbb.toByte(), 0xbf.toByte()).toTypedArray())
    val blob = Blob(arrayOf(bom, content), BlobPropertyBag(type = "text/csv;charset=utf-8;"))

    val link = document.createElement("a") as HTMLElement
    val url = URL.createObjectURL(blob)
    link.setAttribute("href", url)
    link.setAttribute("download", filename)
    link.style.visibility = "hidden"
    document.body?.appendChild(link)
    link.click()
    document.body?.removeChild(link)
}