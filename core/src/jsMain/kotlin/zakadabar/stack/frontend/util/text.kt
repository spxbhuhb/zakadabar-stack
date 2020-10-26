/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.util

import kotlinx.browser.document
import org.w3c.dom.HTMLElement

fun escape(source: String) = source.replace("&", "&amp;").replace("<", "&lt;")

fun makeString(source: List<Int>): String {
    // TODO if it is possible, change the toIntArray to something better
    return js("String.fromCodePoint").apply(null, source.toIntArray()) as String
}

/**
 * Pops a file download for the user, the file content will be the text passed.
 */
fun download(filename: String, text: String) {
    val element = document.createElement("A") as HTMLElement
    element.setAttribute("href", "data:text/plain;charset=utf-8," + js("encodeURIComponent")(text) as String)
    element.setAttribute("download", filename)

    element.style.display = "none"
    document.body?.appendChild(element) ?: throw IllegalStateException("document body is null")

    element.click()

    document.body !!.removeChild(element);
}