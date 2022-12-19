/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

fun SimpleDomElement.toXml() : ByteArray {
    val sb = StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n")
    sb.appendXml(this)
    return sb.toString().encodeToByteArray()
}
private fun Appendable.appendXml(dom: SimpleDomElement) {
    append('<').append(dom.name)
    appendAttributes(dom)
    if (dom.isEmpty()) {
        append("/>")
    } else {
        append('>')
        appendText(dom)
        appendChildNodes(dom)
        append("</").append(dom.name).append('>')
    }
}

private fun Appendable.appendChildNodes(dom: SimpleDomElement) {
    dom.childNodes.forEach(::appendXml)
}
private fun Appendable.appendAttributes(dom: SimpleDomElement) {
    dom.attributes.forEach {
        append(' ').append(it.key).append("=\"").append(it.value).append('"')
    }
}
private fun Appendable.appendText(dom: SimpleDomElement) {
    dom.text?.forEach { char->
        when(char) {
            '&' -> append("&amp;")
            '<' -> append("&lt;")
            '>' -> append("&gt;")
            else -> append(char)
        }
    }
}
