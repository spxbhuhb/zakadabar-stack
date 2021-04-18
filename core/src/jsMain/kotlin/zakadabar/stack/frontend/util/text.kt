/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import zakadabar.stack.frontend.builtin.ZkElement

fun escape(source: String) = source.replace("&", "&amp;").replace("<", "&lt;")

fun makeString(source: List<Int>): String {
    // TODO if it is possible, change the toIntArray to something better
    return js("String.fromCodePoint").apply(null, source.toIntArray()) as String
}

fun downloadCsv(filename: String, content: String) {
    val blob = Blob(arrayOf(content), BlobPropertyBag(type = "text/csv;charset=utf-8;"))

    val link = document.createElement("a") as HTMLElement
    val url = URL.createObjectURL(blob)
    link.setAttribute("href", url)
    link.setAttribute("download", filename)
    link.style.visibility = "hidden"
    document.body?.appendChild(link)
    link.click()
    document.body?.removeChild(link)
}

/**
 * Create a [ZkElement] with the content of the HTML element set to the string.
 *
 * @param  asText  When true the string is assigned to `innerText`. When false it is assigned to `innerHTML`
 */
fun String.asElement(asText: Boolean = true): ZkElement {
    return ZkElement() build {
        if (asText) {
            buildElement.innerText = this@asElement
        } else
            buildElement.innerText = this@asElement
    }
}