/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

fun Node.toXml() = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n$xml"

private val Node.xml : String
    get() {
        return if (attributes.isEmpty()) {
            if (childNodes.isNotEmpty()) "<$name>$xmlChildNodes</$name>"
            else if (text != null) "<$name>${text.xml}</$name>"
            else "<$name/>"
        } else {
            if (childNodes.isNotEmpty()) "<$name$xmlAttributes>$xmlChildNodes</$name>"
            else if (text != null) "<$name$xmlAttributes>${text.xml}</$name>"
            else "<$name$xmlAttributes/>"
        }
    }

private val Node.xmlAttributes : CharSequence
    get() {
        val sb = StringBuilder()
        for((key, value) in attributes) {
            sb.append(" $key=\"${value.xml}\"")
        }
        return sb
    }

private val Node.xmlChildNodes : CharSequence
    get() {
        val sb = StringBuilder()
        for (n in childNodes) {
            sb.append(n.xml)
        }
        return sb
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
