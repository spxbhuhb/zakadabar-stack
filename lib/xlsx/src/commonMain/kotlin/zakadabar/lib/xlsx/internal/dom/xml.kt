/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.dom

internal fun Node.toXml(append: (String)->Unit) {
    append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n")
    xml(append)
}

private fun Node.xml(append: (String)->Unit) {
    append("<")
    append(name)
    xmlAttributes(append)
    if (text != null) {
        append(">")
        append(text.xml)
        append("</")
        append(name)
        append(">")
    } else if (childNodes.isNotEmpty()) {
        append(">")
        xmlChildNodes(append)
        append("</")
        append(name)
        append(">")
    } else {
        append("/>")
    }
}

private fun Node.xmlAttributes(append: (String)->Unit) {
    for((key, value) in attributes) {
        append(" ")
        append(key)
        append("=\"")
        append(value.xml)
        append("\"")
    }
}

private fun Node.xmlChildNodes(append: (String)->Unit) {
    for (n in childNodes) {
        n.xml(append)
    }
}

private val XML_CHARS = "[&<>'\"]".toRegex()

private val CharSequence.xml : String
    get() {
        return replace(XML_CHARS) {
            when (it.value) {
                "&" -> "&amp;"
                "<" -> "&lt;"
                ">" -> "&gt;"
                "'" -> "&apos;"
                "\"" -> "&quot;"
                else -> it.value
            }
        }
    }
