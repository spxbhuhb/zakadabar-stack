/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.dom

/**
 * This file contains conversion functions to max Xml text from Node data
 */

/**
 * Convert Node to Xml string, and write xml data to the specified appender
 * includes xml header
 */
internal fun Node.toXml(append: (String)->Unit) {
    append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n")
    xml(append)
}

/**
 * A node's xml data without xml header
 */
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

/**
 * a node attributzes in xml
 *
 * at1="v1" at2="v2"
 */
private fun Node.xmlAttributes(append: (String)->Unit) {
    for((key, value) in attributes) {
        append(" ")
        append(key)
        append("=\"")
        append(value.xml)
        append("\"")
    }
}

/**
 * A Node children xml data
 */
private fun Node.xmlChildNodes(append: (String)->Unit) {
    for (n in childNodes) {
        n.xml(append)
    }
}

/**
 * dangerous characters in xml <>'&"
 */
private val XML_CHARS = "[&<>'\"]".toRegex()

/**
 * A text with xml escape
 */
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
