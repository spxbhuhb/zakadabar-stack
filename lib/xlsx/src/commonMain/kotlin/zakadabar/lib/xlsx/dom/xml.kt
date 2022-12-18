/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

fun SimpleDomElement.toXml() : String {
    val sb = StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
    appendTo(sb)
    return sb.toString()
}

fun List<SimpleDomElement>.appendTo(sb: StringBuilder, level: Int) = forEach { it.appendTo(sb, level) }

fun SimpleDomElement.appendTo(sb: StringBuilder, level: Int = 0) {
    val indent = CharArray(1 + 2 * level) { if (it == 0) '\n' else ' ' }

    sb.append(indent).append('<').append(name)

    attributes.forEach {
        sb.append(' ').append(it.key).append('=').append('"').append(it.value).append('"')
    }

    if (text.isNullOrBlank() && childNodes.isEmpty()) sb.append("/>")
    else {
        sb.append('>').appendEscaped(text ?: "")

        childNodes.appendTo(sb, level + 1)

        if (childNodes.isNotEmpty()) sb.append(indent)

        sb.append("</").append(name).append('>')
    }

}

private fun StringBuilder.appendEscaped(s: String) = append(s
    .replace("&","&amp;")
    .replace("<","&lt;")
    .replace(">","&gt;")
)

