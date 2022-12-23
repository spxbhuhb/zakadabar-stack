/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

fun Node.toXml() : ByteArray {
    val sb = StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n")
    sb.appendXml(this)
    return sb.toString().encodeToByteArray()
}
private fun Appendable.appendXml(node: Node) {
    append('<').append(node.name)
    appendAttributes(node)
    if (node.isEmpty()) {
        append("/>")
    } else {
        append('>')
        appendText(node)
        appendChildNodes(node)
        append("</").append(node.name).append('>')
    }
}

private fun Appendable.appendChildNodes(node: Node) {
    node.childNodes.forEach(::appendXml)
}
private fun Appendable.appendAttributes(node: Node) {
    node.attributes.forEach {
        append(' ').append(it.key).append("=\"").append(it.value).append('"')
    }
}
private fun Appendable.appendText(node: Node) {
    node.text?.forEach { char ->
        when(char) {
            '&' -> append("&amp;")
            '<' -> append("&lt;")
            '>' -> append("&gt;")
            else -> append(char)
        }
    }
}
